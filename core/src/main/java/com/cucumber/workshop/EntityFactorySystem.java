package com.cucumber.workshop;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.cucumber.workshop.logic.basic.Damage;
import com.cucumber.workshop.logic.basic.Fade;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.basic.Player;
import com.cucumber.workshop.logic.enemy.InteractionType;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.enemy.Homing;
import com.cucumber.workshop.logic.enemy.TargetPositon;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.logic.operation.HpControlledRadius;
import com.cucumber.workshop.logic.operation.Operations;
import com.cucumber.workshop.logic.operation.TweenFadeOperation;
import com.cucumber.workshop.logic.spatial.Angle;
import com.cucumber.workshop.logic.spatial.AngleRotation;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import com.cucumber.workshop.logic.spatial.Velocity;
import com.cucumber.workshop.render.basic.Light;
import com.cucumber.workshop.render.basic.Renderable;
import com.cucumber.workshop.render.basic.Tint;
import com.cucumber.workshop.render.texture.NormalRef;
import com.cucumber.workshop.render.texture.TextureRef;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.cucumber.workshop.logic.operation.OperationsFactory.tweenFade;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class EntityFactorySystem extends BaseSystem {
    private ComponentMapper<TextureRef> textureRefs;
    private ComponentMapper<NormalRef> normalRefs;
    private ComponentMapper<Position> positions;
    private ComponentMapper<Velocity> velocities;
    private ComponentMapper<Radius> radiuses;
    private ComponentMapper<HpControlledRadius> hpControl;
    private ComponentMapper<HitPoints> hps;
    private ComponentMapper<Enemy> enemies;
    private ComponentMapper<Damage> damage;
    private ComponentMapper<Fade> fade;
    private ComponentMapper<Operations> operations;
    private ComponentMapper<AngleRotation> rotations;
    private ComponentMapper<Homing> homing;
    private ComponentMapper<Tint> tints;

    @SuppressWarnings("unchecked")
    public Entity enemy(float x, float y){
        Entity entity = gameobject()
                .with(Enemy.class,
                      HitPoints.class,
                      HpControlledRadius.class,
                      AngleRotation.class,
                      Homing.class,
                      TargetPositon.class,
                      Tint.class,
                      Fade.class
                )
                .build();

        setTextureRefs(entity, "crystal-ball");
        pos(entity, x, y);

        velocities.get(entity).dx(random(-15, 15)).dy(random(-15, 15));
        hpControl.get(entity).baseRadius(15).hpMultiplier(8);
        rotations.get(entity).degreesPerSecond(MathUtils.random(-360, 360));
        homing.get(entity).turnrate(MathUtils.random(10, 300));

        fadeIn(entity);

        setRandomType(entity);

        int hp = random(1, 4);
        hps.get(entity).hp(hp);
        return entity;
    }

    private void fadeIn(Entity entity) {
        TweenFadeOperation operation =
                tweenFade(0, 1, MathUtils.random(0.5f, 1), Interpolation.linear);
        operations.get(entity).add(operation);
    }

    private void setRandomType(Entity entity) {
        Type type = Type.randomType();
        enemies.get(entity).type(type);
        setTextureRefs(entity, type.image);
    }


    public Entity player(float x, float y){
        Entity entity = player().build();
        setTextureRefs(entity, "radio_circle");
        pos(entity, x, y);
        radiuses.get(entity).r(25);
        hps.get(entity).hp(10);
        return entity;
    }

    public Entity light(float x, float y){
        Entity entity = gameobject().with(Light.class).build();
        setTextureRefs(entity, "light");
        pos(entity, x, y);
        radiuses.get(entity).r(150);
        return entity;
    }


    public void setTextureRefs(Entity entity, final String textureBaseName) {
        textureRefs.get(entity).id(textureBaseName + ".png");
        normalRefs.get(entity).id(textureBaseName + "_n.png");
    }

    private Position pos(Entity entity, float x, float y) {
        return positions.get(entity).x(x).y(y);
    }

    public Entity damage(float x, float y){
        Entity entity = builder()
                .with(Position.class,
                      Radius.class,
                      Damage.class).build();
        pos(entity, x, y);
        return entity;
    }

    private EntityBuilder builder() {
        return new EntityBuilder(world);
    }

    private EntityBuilder gameobject() {
        return builder()
                //logic
                .with(Position.class,
                      Velocity.class,
                      Angle.class,
                      Operations.class,
//                      AngleFollowVelocity.class,
                      Radius.class)
                //render
                .with(TextureRef.class,
                      NormalRef.class,
                      Renderable.class);
    }

    private EntityBuilder player() {
        return gameobject()
                .with(Player.class,
                      HitPoints.class,
                      InteractionType.class);
    }

    @Override
    protected void processSystem() {

    }
}
