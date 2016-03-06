package com.cucumber.workshop.render.shader;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.cucumber.workshop.ResourceDirectory;
import com.cucumber.workshop.logic.spatial.Angle;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import com.cucumber.workshop.render.basic.CameraConstants;
import com.cucumber.workshop.render.basic.Light;
import com.cucumber.workshop.render.texture.NormalTextureComponent;
import com.cucumber.workshop.render.texture.TextureComponent;

@Wire(injectInherited = true)
public class LightSystem extends IteratingSystem implements Disposable{
    private static final int SIZE_DIVISOR = 8;
    private final LightManager lightManager;

    @Wire(name = CameraConstants.GAME_CAM)
    private OrthographicCamera camera;
    @Wire
    private SpriteBatch spriteBatch;
    @Wire
    ResourceDirectory resources;

    protected ComponentMapper<Position> positions;
    protected ComponentMapper<Angle> angles;
    protected ComponentMapper<Radius> radii;
    protected ComponentMapper<TextureComponent> textures;

    protected ComponentMapper<NormalTextureComponent> normalTextures;
    private FrameBuffer frameBuffer;

    private TextureRegion textureRegion;
    private final int width;

    private final int height;

    private Texture flatTexture;

    public LightSystem(LightManager lightManager) {
        super(Aspect.all(
                Position.class,
                Radius.class,
                Angle.class,
                TextureComponent.class,
                NormalTextureComponent.class,
                Light.class
              )
        );

        this.lightManager = lightManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,
                                      width * 2 / SIZE_DIVISOR,
                                      height / SIZE_DIVISOR,
                                      false);
        textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        textureRegion.flip(false, true);
    }

    @Override
    protected void initialize() {
        flatTexture = new Texture(Gdx.files.internal(resources.dir + "/img/miss.png"));
    }

    @Override
    protected void begin() {
        lightManager.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        spriteBatch.enableBlending();
        frameBuffer.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        setBlend();

        Gdx.gl.glViewport(0, 0, width / SIZE_DIVISOR, height / SIZE_DIVISOR);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
    }

    private void setBlend() {
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA,
                                     GL20.GL_ONE);
    }

    @Override
    protected void process(int e) {
        TextureComponent textureComp = textures.get(e);
        Texture texture = textureComp.texture;

        renderTexture(e, texture);
    }

    private void renderTexture(int e, Texture texture) {
        Position p = positions.get(e);
        Angle angle = angles.getSafe(e, Angle.ZERO);

        int width = texture.getWidth();
        int height = texture.getHeight();

        Radius radius = radii.get(e);

        float r = radius.r * 2;
        float hw = r / 2;
        float hh = r / 2;
        spriteBatch.draw(texture,
                         p.x - hw,
                         p.y - hh,
                         hw, hh,
                         r,
                         r,
                         1.0f,
                         1.0f,
                         angle.degrees,
                         0, 0,
                         width,
                         height,
                         false,
                         false
        );
    }

    @Override
    protected void end() {
        spriteBatch.end();

        renderLightDirMap();

        frameBuffer.end();
        Gdx.gl.glClearColor(1, 0, 0, 1);
    }

    private void renderLightDirMap() {
        Gdx.gl.glViewport(width / SIZE_DIVISOR, 0, width / SIZE_DIVISOR, height / SIZE_DIVISOR);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch.begin();
        spriteBatch.draw(flatTexture,
                         camera.position.x - width / 2,
                         camera.position.y - height / 2,
                          width *1.25f,
                         height * 1.25f);

        IntBag entityIds = getEntityIds();
        for (int i = 0, s = entityIds.size(); i < s; i++) {
            int entityId = entityIds.get(i);
            NormalTextureComponent normal = normalTextures.get(entityId);
            renderTexture(entityId, normal.texture);
        }
        spriteBatch.end();
    }

    public Texture getLightTexture() {
        return textureRegion.getTexture();
    }

    @Override
    public void dispose() {
        frameBuffer.dispose();
        flatTexture.dispose();
    }


}