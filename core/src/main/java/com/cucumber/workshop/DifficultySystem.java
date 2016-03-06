package com.cucumber.workshop;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.cucumber.workshop.logic.basic.Player;
import com.cucumber.workshop.logic.spatial.Position;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class DifficultySystem extends BaseSystem {

    private EntityFactorySystem entityFactory;
    private ComponentMapper<Position> postions;

    private Position playerPosition;
    private EnemySpawner enemySpawner;
    private EntitySubscription players;

    public int difficulty = 1;
    public float difficultyTime = 0;
    public float spawnTime = 0;

    @Override
    protected void initialize() {
        enemySpawner = new EnemySpawner(entityFactory);
        players = world.getAspectSubscriptionManager().get(Aspect.all(Player.class));
    }

    @Override
    protected void begin() {
        playerPosition = postions.get(players.getEntities().get(0));
    }

    @Override
    protected boolean checkProcessing() {
        return players.getEntities().size() > 0;
    }

    @Override
    protected void processSystem() {
        difficultyTime += world.delta;
        spawnTime += world.delta;

        if (difficultyTime >= 10) {
            difficulty++;
            difficultyTime -= difficultyTime;
        }

        float spawnInterval = 2.0f / difficulty;
        if (spawnTime > spawnInterval) {
            spawnTime -= spawnInterval;
            enemySpawner.spawnRelativeToPlayer(playerPosition.x, playerPosition.y);
        }

    }

}
