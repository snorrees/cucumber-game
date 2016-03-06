package com.cucumber.workshop;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.cucumber.workshop.logic.basic.Damage;
import com.cucumber.workshop.logic.basic.Player;
import com.cucumber.workshop.logic.enemy.InteractionType;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class InteractionSystem extends BaseSystem {

    private EntityFactorySystem entityFactory;
    private EntitySubscription players;

    private ComponentMapper<Position> positions;
    private ComponentMapper<Radius> radii;
    private ComponentMapper<Damage> damages;
    private ComponentMapper<InteractionType> activeType;

    @Override
    protected void initialize() {
        players = world.getAspectSubscriptionManager().get(Aspect.all(Player.class));
    }

    @Override
    protected void processSystem() {

    }

    public void typeChange(Type type){
        IntBag entities = players.getEntities();
        for (int i = 0, s = entities.size(); i < s; i++) {
            activeType.get(entities.get(i)).type(type);
        }
    }

    public void interaction(float x, float y){
        if (players.getEntities().size() > 0) {
            int playerId = players.getEntities().get(0);
            Type activeType = this.activeType.getSafe(playerId, InteractionType.FALLBACK).type;

            Entity damage = entityFactory.damage(x, y);
            damages.get(damage).type(activeType);
            radii.get(damage).r(20);
        }
    }
}
