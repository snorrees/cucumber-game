package com.cucumber.workshop.render.shader;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.cucumber.workshop.logic.basic.Fade;
import com.cucumber.workshop.logic.spatial.Angle;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import com.cucumber.workshop.render.basic.CameraConstants;
import com.cucumber.workshop.render.basic.Light;
import com.cucumber.workshop.render.basic.Tint;
import com.cucumber.workshop.render.texture.NormalTextureComponent;
import com.cucumber.workshop.render.texture.TextureComponent;

import static com.cucumber.workshop.logic.basic.Fade.ALPHA_ONE;
import static com.cucumber.workshop.render.basic.Tint.WHITE;

@Wire(injectInherited = true)
public class ShaderTextureRenderSystem extends IteratingSystem {

    private Vector2 tempAngle = new Vector2();

    @Wire(name = CameraConstants.GAME_CAM)
    private OrthographicCamera camera;
    @Wire
    private BumpBatch batch;

    private Texture lightTexture;

    private ComponentMapper<TextureComponent> textures;
    private ComponentMapper<NormalTextureComponent> normalTextures;
    private ComponentMapper<Position> positions;
    private ComponentMapper<Angle> angles;
    private ComponentMapper<Radius> radii;
    private ComponentMapper<Fade> fades;
    private ComponentMapper<Tint> tints;

    TextureRegion textureRegion;
    Sprite textureSprite;
    Sprite normalSprite;

    @SuppressWarnings("unchecked")
    public ShaderTextureRenderSystem(Texture lightTexture) {
        super(Aspect.all(Position.class,
                         Radius.class,
                         Angle.class,
                         TextureComponent.class,
                         NormalTextureComponent.class)
                    .exclude(Light.class)
        );
        this.lightTexture = lightTexture;
        textureSprite = new Sprite();
        normalSprite = new Sprite();
        textureRegion = new TextureRegion();
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        lightTexture.bind(2);
    }

    @Override
    protected void process(int entityId) {
        TextureComponent textureComp = textures.get(entityId);
        Texture texture = textureComp.texture;
        NormalTextureComponent normal = normalTextures.get(entityId);

        Position p = positions.get(entityId);
        Angle angle = angles.getSafe(entityId);

        Radius radius = radii.get(entityId);
        Fade fade = fades.getSafe(entityId, ALPHA_ONE);
        Tint tint = tints.getSafe(entityId, WHITE);

        textureSprite.setRegion(texture);
        textureSprite.setPosition(p.x - radius.r, p.y - radius.r);
        textureSprite.setOrigin(radius.r, radius.r);
        textureSprite.setRotation(angle.degrees);
        textureSprite.setSize(radius.r * 2, radius.r * 2);
        textureSprite.setColor(tint.color.r, tint.color.g, tint.color.b, fade.alpha);

        normalSprite.setRegion(normal.texture);

        tempAngle.set(MathUtils.cosDeg(-angle.degrees), MathUtils.sinDeg(-angle.degrees));

        batch.draw(texture, normalSprite, textureSprite.getVertices(), tempAngle);
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    public void dispose() {
    }
}