package com.cucumber.workshop.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.cucumber.workshop.logic.basic.Player;
import com.cucumber.workshop.logic.enemy.InteractionType;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import com.cucumber.workshop.render.basic.CameraConstants;
import com.cucumber.workshop.render.basic.Culled;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class PlayerInteractionRenderSystem extends IteratingSystem implements Disposable {

    public static final Radius RADIUS_FALLBACK = new Radius().r(10);

    @Wire(name = CameraConstants.GAME_CAM)
    private OrthographicCamera camera;

    private final ShapeRenderer shapeRenderer;

    private ComponentMapper<Position> positions;
    private ComponentMapper<Radius> radiuses;
    private ComponentMapper<Enemy> enemies;
    private ComponentMapper<InteractionType> activeTypes;

    private Vector3 temp = new Vector3();

    private EntitySubscription players;

    @SuppressWarnings("unchecked")
    public PlayerInteractionRenderSystem() {
        super(Aspect.all(Position.class,
                         Radius.class)
                    .exclude(Culled.class));
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void initialize() {
        players = world.getAspectSubscriptionManager().get(Aspect.all(Player.class));
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (players.getEntities().size() > 0) {
            renderPlayerInteraction();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    private void renderPlayerInteraction() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int playerId = players.getEntities().get(0);
        Type type = activeTypes.get(playerId).type;
        shapeRenderer.setColor(type.r, type.g, type.b, 1);

        camera.unproject(temp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        shapeRenderer.circle(temp.x, temp.y, 10);

        shapeRenderer.end();
    }

    @Override
    protected void process(int id) {
//        Position pos = positions.get(id);
//        Radius radius = radiuses.getSafe(id, RADIUS_FALLBACK);
//
//        Enemy enemy = enemies.getSafe(id);
//        if (enemy != null) {
//            Type type = enemy.type;
//            shapeRenderer.setColor(type.r, type.g, type.b, 1);
//        } else {
//            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
//        }
//
//        shapeRenderer.circle(pos.x, pos.y, radius.r);
    }

    @Override
    protected void end() {
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
