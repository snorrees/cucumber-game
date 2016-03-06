package com.cucumber.workshop;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.render.shader.BumpBatch;
import com.cucumber.workshop.render.shader.LightManager;
import com.cucumber.workshop.render.shader.LightShader;
import net.mostlyoriginal.api.screen.core.WorldScreen;

import static com.cucumber.workshop.WorldConfigurationFactory.renderableWorld;
import static com.cucumber.workshop.render.basic.CameraConstants.GAME_CAM;
import static com.cucumber.workshop.render.basic.CameraConstants.GUI_CAM;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class GameScreen extends WorldScreen {

    private OrthographicCamera camera;
    private Viewport viewport;

    private final ResourceDirectory resourceDirectory;

    public GameScreen(ResourceDirectory resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }


    @Override
    protected World createWorld() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);

        camera = new OrthographicCamera();
        OrthographicCamera guiCamera = new OrthographicCamera();
        viewport = new ExtendViewport(500, 500, 2000, 2000, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        multiplexer.addProcessor(new RestartListener());
        multiplexer.addProcessor(new InteractionEventSource());

        LightManager lightManager = new LightManager(camera, resourceDirectory);

        return new World(
                renderableWorld(lightManager)
                        .with(new GameStateSetupSystem())
                        .build()
                        .register(new SpriteBatch())
                        .register(createBumpBatch(lightManager))
                        .register(GAME_CAM, camera)
                        .register(GUI_CAM, guiCamera)
                        .register(resourceDirectory)
        );
    }

    private BumpBatch createBumpBatch(LightManager lightManager) {
        LightShader lightShader = lightManager.getLightShader();
        return new BumpBatch(5000, lightShader);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private class InteractionEventSource extends InputAdapter {
        private final Vector3 pos = new Vector3();

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.NUM_1) {
                interactionSystem().typeChange(Type.GREEN);
                return true;
            }
            if (keycode == Input.Keys.NUM_2) {
                interactionSystem().typeChange(Type.BLUE);
                return true;
            }
            if (keycode == Input.Keys.NUM_3) {
                interactionSystem().typeChange(Type.RED);
                return true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (button != Input.Buttons.LEFT || pointer > 0) {
                return false;
            }

            camera.unproject(pos.set(screenX, screenY, 0));
            interactionSystem().interaction(pos.x, pos.y);
            return true;
        }

        private InteractionSystem interactionSystem() {
            return world.getSystem(InteractionSystem.class);
        }
    }

    private class RestartListener extends InputAdapter {
        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.F5) {
                if (world != null) {
                    world.dispose();
                }
                world = createWorld();
                return true;
            }
            return false;
        }
    }

}
