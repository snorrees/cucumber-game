package com.cucumber.workshop.render.debug;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.cucumber.workshop.ResourceDirectory;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.basic.Lifetime;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.spatial.Angle;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import com.cucumber.workshop.render.basic.CameraConstants;
import com.cucumber.workshop.render.basic.Culled;


/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class EntityDebugRenderSystem extends IteratingSystem implements Disposable {

    public static final Radius RADIUS_FALLBACK = new Radius().r(10);
    public static final Color ENEMY_COLOR = Color.RED;
    public static final Color FRIENDLY_COLOR = Color.GREEN;

    @Wire(name = CameraConstants.GAME_CAM)
    private OrthographicCamera camera;
    @Wire
    private SpriteBatch spriteBatch;
    @Wire
    private ResourceDirectory resources;

    private final ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Vector3 temp = new Vector3();

    private ComponentMapper<Position> positions;
    private ComponentMapper<Angle> angles;
    private ComponentMapper<Radius> radiuses;
    private ComponentMapper<Lifetime> lifetime;
    private ComponentMapper<Enemy> enemies;
    private ComponentMapper<HitPoints> hps;

    @SuppressWarnings("unchecked")
    public EntityDebugRenderSystem() {
        super(Aspect.all(Position.class)
                    .exclude(Culled.class));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void initialize() {
        this.font = new BitmapFont(Gdx.files.internal(resources.dir + "/fonts/fnt_arialbld_12.fnt"),
                                   Gdx.files.internal(resources.dir + "/fonts/fnt_arialbld_12_00.png"),
                                   false);
    }

    @Override
    protected void begin() {
        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(camera.combined);

        font.setColor(1, 1, 1, 1);
        font.draw(spriteBatch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()),
                  50,
                  Gdx.graphics.getHeight() - 50);

        font.setColor(0.3f, 0.3f, 1, 1);
        camera.unproject(temp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        font.draw(spriteBatch, String.format("%.1f, %.1f", temp.x, temp.y), temp.x + 20, temp.y);
    }

    @Override
    protected void process(int id) {
        Position pos = positions.get(id);
        Angle angle = angles.getSafe(id, Angle.ZERO);
        Radius radius = radiuses.getSafe(id, RADIUS_FALLBACK);

        if (enemies.getSafe(id) != null) {
            shapeRenderer.setColor(ENEMY_COLOR);
        } else {
            shapeRenderer.setColor(FRIENDLY_COLOR);
        }

        shapeRenderer.circle(pos.x, pos.y, radius.r);

        float dx = MathUtils.cosDeg(angle.degrees) * radius.r;
        float dy = MathUtils.sinDeg(angle.degrees) * radius.r;
        shapeRenderer.line(pos.x, pos.y, pos.x + dx, pos.y + dy);

        font.draw(spriteBatch, String.format("%.1f, %.1f", pos.x, pos.y), pos.x + radius.r + 1, pos.y);

        HitPoints hitPoints = hps.getSafe(id);
        if (hitPoints != null) {
            font.draw(spriteBatch, String.format("Hp: %.0f", hitPoints.hp), pos.x + radius.r + 1, pos.y + 10);
        }
    }

    @Override
    protected void end() {
        spriteBatch.end();
        shapeRenderer.end();
    }

    @Override
    protected boolean checkProcessing() {
        return Gdx.input.isKeyPressed(Input.Keys.F2);
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
