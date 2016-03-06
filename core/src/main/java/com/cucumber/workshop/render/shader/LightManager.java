package com.cucumber.workshop.render.shader;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.cucumber.workshop.ResourceDirectory;

/**
 * Date: 16.05.13
 * Time: 23:50
 *
 * @author Snorre E. Brekke
 */

public class LightManager implements Disposable {

    private final DirectionalLight directionalLight = new DirectionalLight();
    public AmbientLight ambientLight = new AmbientLight();

    private OrthographicCamera camera;

    private LightShader lightShader;
    private final ResourceDirectory resources;

    public LightManager(OrthographicCamera camera, ResourceDirectory resources) {
        this.camera = camera;
        this.resources = resources;
        lightShader = new LightShader(this, camera, resources);
    }

    public void reload() {
        lightShader = new LightShader(this, camera, resources);
    }

    public void update() {
        lightShader.getShaderProgram().begin();
        calculateAndApplyLights(lightShader.getShaderProgram());
        lightShader.update();
        lightShader.getShaderProgram().end();
    }

    public void calculateAndApplyLights(ShaderProgram shader) {
        this.applyGlobalLights(shader);
    }

    public LightShader getLightShader() {
        return lightShader;
    }


    public void applyGlobalLights(ShaderProgram shader) {
        shader.setUniformf("ambientColor", ambientLight.color);
        shader.setUniformf("dirColor", directionalLight.color);
        shader.setUniformf("dirVector", directionalLight.direction);

    }
    public void dispose() {
        lightShader.dispose();
    }

    public void setAmbientLightColor(float r, float g, float b, float a) {
        ambientLight.color.set(r, g, b, a);
    }

    public void setDirLightColor(float r, float g, float b, float a) {
        directionalLight.color.set(r, g, b, a);
    }

    public void setDirLightDirection(float x, float y, float z, int index) {
        if (Math.abs(x) < 0.1) {
            x += Math.signum(x) * 0.1;
        }
        if (Math.abs(y) < 0.1) {
            y += Math.signum(y) * 0.1;
        }
        directionalLight.direction.set(x, y, z);
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
}
