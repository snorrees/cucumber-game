package com.cucumber.workshop.render.texture;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cucumber.workshop.logic.spatial.Angle;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import com.cucumber.workshop.render.basic.CameraConstants;
import com.cucumber.workshop.render.basic.Culled;
import com.cucumber.workshop.render.basic.Light;
import com.cucumber.workshop.render.basic.Renderable;
import com.cucumber.workshop.render.basic.Tint;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class TextureRenderSystem extends EntityProcessingSystem {

    private final Radius fallbackRadius = new Radius();

    @Wire
    private SpriteBatch spriteBatch;

    @Wire(name = CameraConstants.GAME_CAM)
    private OrthographicCamera camera;

    private ComponentMapper<TextureComponent> textures;
    private ComponentMapper<Position> positions;
    private ComponentMapper<Angle> angles;
    private ComponentMapper<Radius> radii;
    private ComponentMapper<Tint> tints;

    @SuppressWarnings("unchecked")
    public TextureRenderSystem() {
        super(Aspect.all(Position.class,
                         Renderable.class,
                         TextureComponent.class)
                    .exclude(Culled.class,
                             Light.class));
    }

    @Override
    protected void begin() {
        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    protected void process(Entity e) {
        TextureComponent textureComp = textures.get(e);
        Position p = positions.get(e);
        Angle angle = angles.getSafe(e, Angle.ZERO);

        Texture texture = textureComp.texture;
        int width = texture.getWidth();
        int height = texture.getHeight();

        Radius radius = radii.getSafe(e, fallbackRadius.r(Math.max(width, height)));

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
    }
}
