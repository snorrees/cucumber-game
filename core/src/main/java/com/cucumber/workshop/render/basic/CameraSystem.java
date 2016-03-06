package com.cucumber.workshop.render.basic;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraSystem extends BaseSystem {

    @Wire(name = CameraConstants.GAME_CAM, failOnNull = true)
    public OrthographicCamera camera;
    @Wire(name = CameraConstants.GUI_CAM)
    public OrthographicCamera guiCamera;
    public float zoom = 1.0f;

    @Override
    protected void initialize() {
        float zoomFactorInverter = 1f / zoom;
//        setupViewport(
//                Gdx.graphics.getWidth() * zoomFactorInverter,
//                Gdx.graphics.getHeight() * zoomFactorInverter
//        );
    }

    protected void setupViewport(float width, float height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.setToOrtho(false, width, height);
        camera.update();

        guiCamera.viewportWidth = width;
        guiCamera.viewportHeight = height;
        guiCamera.setToOrtho(false, width, height);
        guiCamera.update();
    }

    @Override
    protected void processSystem() {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS) && camera.zoom > 0.3) {
            camera.zoom -= world.delta * 1;
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS) && camera.zoom < 1.1) {
            camera.zoom += world.delta * 1;
            camera.update();
        }
    }
}
