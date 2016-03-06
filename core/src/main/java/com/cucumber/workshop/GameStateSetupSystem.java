package com.cucumber.workshop;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.cucumber.workshop.logic.collision.EnemyChange;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Velocity;
import com.cucumber.workshop.render.basic.CameraConstants;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class GameStateSetupSystem extends BaseSystem {

    @Wire(name = CameraConstants.GAME_CAM)
    private OrthographicCamera camera;

    private ComponentMapper<Position> positions;
    private ComponentMapper<Velocity> velocities;

    private EntityFactorySystem entityFactory;
    private EnemySpawner enemySpawner;

    @Override
    protected void initialize() {
        enemySpawner = new EnemySpawner(entityFactory);
        createInitialState();
    }

    @Override
    protected void processSystem() {
    }

    private void createInitialState() {
        float px = camera.position.x;
        float py = camera.position.y;
        entityFactory.player(px, py);
        entityFactory.light(px, py);

        for (int i = 0; i < 5; i++) {
            enemySpawner.spawnRelativeToPlayer(px, py);
        }

        world.createEntity().edit().create(EnemyChange.class);
    }
}
