package com.cucumber.workshop.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * Date: 05.12.13
 * Time: 19:46
 *
 * @author Snorre E. Brekke
 */
public class BumpBatch implements Batch {
    private Mesh mesh;

    private final float[] vertices;
    private int idx = 0;
    private Texture lastTexture = null;
    private Texture lastNormalTexture = null;
    private float invTexWidth = 0, invTexHeight = 0;
    private boolean drawing = false;

    private final Matrix4 transformMatrix = new Matrix4();
    private final Matrix4 projectionMatrix = new Matrix4();
    private final Matrix4 combinedMatrix = new Matrix4();

    private boolean blendingDisabled = false;
    private int blendSrcFunc = Gdx.gl20.GL_SRC_ALPHA;
    private int blendDstFunc = Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA;

    private ShaderProgram shader;

    float color = Color.WHITE.toFloatBits();
    private Color tempColor = new Color(1, 1, 1, 1);

    /** Number of render calls since the last {@link #begin()}. **/
    public int renderCalls = 0;

    /** Number of rendering calls, ever. Will not be reset unless set manually. **/
    public int totalRenderCalls = 0;

    /** The maximum number of sprites rendered in one batch so far. **/
    public int maxSpritesInBatch = 0;

    private boolean hasLights;
    private boolean hasNormals;

    /** Constructs a new SpriteBatch. Sets the projection matrix to an orthographic projection with y-axis point upwards, x-axis
     * point to the right and the origin being in the bottom left corner of the screen. The projection will be pixel perfect with
     * respect to the current screen resolution.
     * <p>
     *             */
    public BumpBatch(int size, LightShader lightShader) {
        // 32767 is max index, so 32767 / 6 - (32767 / 6 % 3) = 5460.
        if (size > 5460) throw new IllegalArgumentException("Can't have more than 5460 sprites per batch: " + size);

        //the size of our batch
        int vertexCount = 4 * size;
        int indexCount = 6 * size;

        //create the mesh with our specific vertex attributes
        mesh = new Mesh(Mesh.VertexDataType.VertexArray, false, vertexCount, indexCount,
                        new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                        new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
                        new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0"),
                        new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"1"),
                        new VertexAttribute(VertexAttributes.Usage.Generic, 2, "a_spriteAngle"));

        projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        int len = size * 6;
        short[] indices = new short[len];
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i + 0] = (short)(j + 0);
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = (short)(j + 0);
        }

        mesh.setIndices(indices);

        final int numComponents = 9;
        //the number of floats per quad -- 4 verts
        final int quadSize = 4 * numComponents;

        //our vertex array needs to be able to hold enough floats for each vertex in our batch
        vertices = new float[size * quadSize];

        //the number of floats per vertex, as specified by our VertexAttributes

        shader = lightShader.getShaderProgram();
        hasLights = true;
        hasNormals = true;
    }



    @Override
    public void begin () {
        if (drawing) throw new IllegalStateException("SpriteBatch.end must be called before begin.");
        renderCalls = 0;

        Gdx.gl.glDepthMask(false);
        shader.begin();
        setupMatrices();

        drawing = true;
    }

    @Override
    public void end () {
        if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before end.");
        if (idx > 0) flush();
        lastTexture = null;
        drawing = false;

      //  Gdx.gl20.glDepthMask(true);
        if (isBlendingEnabled()) Gdx.gl20.glDisable(GL20.GL_BLEND);

        shader.end();
    }

    @Override
    public void setColor (Color tint) {
        color = tint.toFloatBits();
    }

    @Override
    public void setColor (float r, float g, float b, float a) {
        int intBits = (int)(255 * a) << 24 | (int)(255 * b) << 16 | (int)(255 * g) << 8 | (int)(255 * r);
        color = NumberUtils.intToFloatColor(intBits);
    }

    @Override
    public void setColor (float color) {
        this.color = color;
    }

    @Override
    public Color getColor () {
        int intBits = NumberUtils.floatToIntColor(color);
        Color color = this.tempColor;
        color.r = (intBits & 0xff) / 255f;
        color.g = ((intBits >>> 8) & 0xff) / 255f;
        color.b = ((intBits >>> 16) & 0xff) / 255f;
        color.a = ((intBits >>> 24) & 0xff) / 255f;
        return color;
    }

    @Override
    public float getPackedColor() {
        return 0;
    }

    @Override
    public void draw (Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
                      float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {

    }

    public void draw(TextureRegion region, TextureRegion normalTexture, float x, float y) {
        draw(region, normalTexture, x, y, region.getRegionWidth(), region.getRegionHeight());
    }

    public void draw(TextureRegion region, TextureRegion normalTexture, float x, float y, float width, float height) {
        if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

        float[] vertices = this.vertices;

        Texture texture = region.getTexture();
        Texture normal = normalTexture.getTexture();
        if (texture != lastTexture || normal != lastNormalTexture) {
            switchTexture(texture, normal);
        } else if (idx == vertices.length) {
            flush();
        }

        final float fx2 = x + width;
        final float fy2 = y + height;
        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        float color = this.color;
        int idx = this.idx;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;
        vertices[idx++] = normalTexture.getU();
        vertices[idx++] = normalTexture.getV2();
        vertices[idx++] = 1;
        vertices[idx++] = 0;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;
        vertices[idx++] = normalTexture.getU();
        vertices[idx++] = normalTexture.getV();
        vertices[idx++] = 1;
        vertices[idx++] = 0;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;
        vertices[idx++] = normalTexture.getU2();
        vertices[idx++] = normalTexture.getV();
        vertices[idx++] = 1;
        vertices[idx++] = 0;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        vertices[idx++] = normalTexture.getU2();
        vertices[idx++] = normalTexture.getV2();
        vertices[idx++] = 1;
        vertices[idx++] = 0;

        this.idx = idx;
    }

    public void draw (Texture texture, TextureRegion normalTexture, float[] spriteVertices, Vector2 spriteDirection) {
        if (!drawing) throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

        int verticesLength = vertices.length;
        int remainingVertices = verticesLength;
        Texture normal = normalTexture.getTexture();
        if (texture != lastTexture || normal != lastNormalTexture)
            switchTexture(texture, normal);
        else {
            remainingVertices -= idx;
            if (remainingVertices == 0) {
                flush();
                remainingVertices = verticesLength;
            }
        }
        int spriteSize = 20;
        int normalSize = 16;
        int copyCount = Math.min(remainingVertices, spriteSize + normalSize);

        //Copy sprite vertices and add normal texture coordinates
        System.arraycopy(spriteVertices, 0, vertices, idx, 5);
        vertices[idx+5] = normalTexture.getU();
        vertices[idx+6] = normalTexture.getV2();
        vertices[idx+7] = spriteDirection.x;
        vertices[idx+8] = spriteDirection.y;
        System.arraycopy(spriteVertices, 5, vertices, idx+9, 5);
        vertices[idx+5+9] = normalTexture.getU();
        vertices[idx+6+9] = normalTexture.getV();
        vertices[idx+7+9] = spriteDirection.x;
        vertices[idx+8+9] = spriteDirection.y;
        System.arraycopy(spriteVertices, 10, vertices, idx+18, 5);
        vertices[idx+5+18] = normalTexture.getU2();
        vertices[idx+6+18] = normalTexture.getV();
        vertices[idx+7+18] = spriteDirection.x;
        vertices[idx+8+18] = spriteDirection.y;
        System.arraycopy(spriteVertices, 15, vertices, idx+27, 5);
        vertices[idx+5+27] = normalTexture.getU2();
        vertices[idx+6+27] = normalTexture.getV2();
        vertices[idx+7+27] = spriteDirection.x;
        vertices[idx+8+27] = spriteDirection.y;

        idx += copyCount;

    }


    @Override
    public void flush () {
        if (idx == 0) return;

        renderCalls++;
        totalRenderCalls++;
        int spritesInBatch = idx / 36;
        if (spritesInBatch > maxSpritesInBatch) maxSpritesInBatch = spritesInBatch;
        int count = spritesInBatch * 6;

        lastNormalTexture.bind(1);
        lastTexture.bind(0);
        Mesh mesh = this.mesh;
        mesh.setVertices(vertices, 0, idx);
        mesh.getIndicesBuffer().position(0);
        mesh.getIndicesBuffer().limit(count);

        if (blendingDisabled) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            if (blendSrcFunc != -1) Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc);
        }

        mesh.render(shader, GL20.GL_TRIANGLES, 0, count);

        idx = 0;
    }

    @Override
    public void disableBlending () {
        if (blendingDisabled) return;
        flush();
        blendingDisabled = true;
    }

    @Override
    public void enableBlending () {
        if (!blendingDisabled) return;
        flush();
        blendingDisabled = false;
    }

    @Override
    public void setBlendFunction (int srcFunc, int dstFunc) {
        if (blendSrcFunc == srcFunc && blendDstFunc == dstFunc) return;
        flush();
        blendSrcFunc = srcFunc;
        blendDstFunc = dstFunc;
    }

    @Override
    public int getBlendSrcFunc () {
        return blendSrcFunc;
    }

    @Override
    public int getBlendDstFunc () {
        return blendDstFunc;
    }

    @Override
    public void dispose () {
        mesh.dispose();
    }

    @Override
    public Matrix4 getProjectionMatrix () {
        return projectionMatrix;
    }

    @Override
    public Matrix4 getTransformMatrix () {
        return transformMatrix;
    }

    @Override
    public void setProjectionMatrix (Matrix4 projection) {
        if (drawing) flush();
        projectionMatrix.set(projection);
        if (drawing) setupMatrices();
    }

    @Override
    public void setTransformMatrix (Matrix4 transform) {
        if (drawing) flush();
        transformMatrix.set(transform);
        if (drawing) setupMatrices();
    }

    private void setupMatrices() {
        combinedMatrix.set(projectionMatrix).mul(transformMatrix);
        shader.setUniformMatrix("u_projTrans", combinedMatrix);
        //      shader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setUniformi("u_texture", 0);
        if (hasNormals) {
            shader.setUniformi("u_normals", 1);
        }
        if (hasLights) {

            shader.setUniformi("u_lightmap", 2);
        }
    }

    private void switchTexture(Texture texture, Texture normal) {
        flush();
        lastTexture = texture;
        lastNormalTexture = normal;
        invTexWidth = 1.0f / texture.getWidth();
        invTexHeight = 1.0f / texture.getHeight();
    }

    @Override
    public void setShader (ShaderProgram shader) {
        if (drawing) {
            flush();
            this.shader.end();
        }
        this.shader = shader;
        if (drawing) {
             this.shader.begin();
            setupMatrices();
        }
    }

    @Override
    public ShaderProgram getShader() {
        return this.shader;
    }

    public boolean isHasNormals() {
        return hasNormals;
    }

    public boolean isHasLights() {
        return hasLights;
    }

    @Override
    public boolean isBlendingEnabled () {
        return !blendingDisabled;
    }

    @Override
    public boolean isDrawing() {
        return true;
    }


    //NOT SUPPORTED


    @Override
    public void draw(Texture texture, float[] spriteVertices, int offset, int count) {

    }

    @Override
    public void draw(TextureRegion region, float x, float y) {

    }

    @Override
    public void draw(TextureRegion region, float x, float y, float width, float height) {

    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth,
                     int srcHeight, boolean flipX, boolean flipY) {

    }

    @Override
    public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {

    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2,
                     float v2) {

    }

    @Override
    public void draw(Texture texture, float x, float y) {

    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height) {

    }


    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation) {

    }

    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation, boolean clockwise) {

    }

    @Override
    public void draw(TextureRegion region, float width, float height, Affine2 transform) {

    }
}
