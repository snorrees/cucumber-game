package com.cucumber.workshop;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.WorldConfigurationBuilder.Priority;
import com.badlogic.gdx.graphics.Color;
import com.cucumber.workshop.logic.basic.DeathSystem;
import com.cucumber.workshop.logic.basic.ElapsedTimeSystem;
import com.cucumber.workshop.logic.collision.DamageCleanupSystem;
import com.cucumber.workshop.logic.collision.DamageCollisionSystem;
import com.cucumber.workshop.logic.collision.PlayerCollisionSystem;
import com.cucumber.workshop.logic.enemy.HomingSystem;
import com.cucumber.workshop.logic.operation.HpControlledRadiusSystem;
import com.cucumber.workshop.logic.operation.OperationsSystem;
import com.cucumber.workshop.logic.spatial.AngleRotationSystem;
import com.cucumber.workshop.logic.spatial.VelocityAngleSystem;
import com.cucumber.workshop.logic.spatial.VelocitySystem;
import com.cucumber.workshop.render.PlayerInteractionRenderSystem;
import com.cucumber.workshop.render.basic.CameraSystem;
import com.cucumber.workshop.render.debug.EntityDebugRenderSystem;
import com.cucumber.workshop.render.shader.LightManager;
import com.cucumber.workshop.render.shader.LightSystem;
import com.cucumber.workshop.render.shader.ShaderTextureRenderSystem;
import com.cucumber.workshop.render.texture.NormalAssetManager;
import com.cucumber.workshop.render.texture.TextureAssetManager;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class WorldConfigurationFactory {

    private static final String BACKGROUND_COLOR_HEX = "000000";

    public static WorldConfiguration headlessWorldConfiguration() {
        return headlessWorldBuilder().build()
                                     .register(new ResourceDirectory("."));
    }

    public static WorldConfigurationBuilder headlessWorldBuilder() {
        return new WorldConfigurationBuilder()
                .with(
                        new ElapsedTimeSystem(),
                        new DifficultySystem(),
                        new OperationsSystem(),
                        new EntityFactorySystem(),
                        new InteractionSystem(),
                        new AngleRotationSystem(),
                        new HomingSystem(),
                        new VelocitySystem(),
                        new VelocityAngleSystem(),
                        new DamageCollisionSystem(),
                        new PlayerCollisionSystem(),
                        new HpControlledRadiusSystem(),
                        new DamageCleanupSystem(),
                        new DeathSystem(),
                        new EnemyChangeSystem(),
                        new EnemyTypeTintSystem(),
                        new TextureAssetManager(),
                        new NormalAssetManager()
                );
    }

    public static WorldConfigurationBuilder renderableWorld(LightManager lightManager) {
        LightSystem lightSystem = new LightSystem(lightManager);
        ShaderTextureRenderSystem renderSystem =
                new ShaderTextureRenderSystem(lightSystem.getLightTexture());

        return headlessWorldBuilder()
                .with(Priority.LOW,
                        new CameraSystem(),
                        new ClearScreenSystem(Color.valueOf(BACKGROUND_COLOR_HEX)),
                        lightSystem,
                        renderSystem,
                        new PlayerInteractionRenderSystem(),
                        new EntityDebugRenderSystem()
                );
    }
}
