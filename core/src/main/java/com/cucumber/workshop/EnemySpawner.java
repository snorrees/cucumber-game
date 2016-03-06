package com.cucumber.workshop;

import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.cucumber.workshop.logic.enemy.TargetPositon;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class EnemySpawner {
    private EntityFactorySystem entityFactory;

    public EnemySpawner(EntityFactorySystem entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void spawnRelativeToPlayer(float px, float py){
        float distance = MathUtils.random() * 100 + 150;
        float angle = MathUtils.random() * 360;

        float x = px + MathUtils.cosDeg(angle) * distance;
        float y = py + MathUtils.sinDeg(angle) * distance;

        Entity enemy = entityFactory.enemy(x, y);

        enemy.getComponent(TargetPositon.class).x(px).y(py);
    }
}
