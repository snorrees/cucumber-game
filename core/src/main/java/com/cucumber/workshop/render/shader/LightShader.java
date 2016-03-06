package com.cucumber.workshop.render.shader;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.cucumber.workshop.ResourceDirectory;

/**
 * Date: 13.05.13
 * Time: 22:20
 *
 * @author Snorre E. Brekke
 */
public class LightShader extends AbstractShader {
    private final LightManager lightManager;
    private final Vector2 resolution = new Vector2();
    private final OrthographicCamera camera;
    private final ResourceDirectory resources;

    public LightShader(LightManager lightManager, OrthographicCamera camera, ResourceDirectory resources) {
        this.lightManager = lightManager;
        this.camera = camera;
        this.resources = resources;
        createShader();
    }

    @Override
    public void update() {

    }

    @Override
    protected void initProgram(ShaderProgram program) {
        program.begin();
        program.setUniformi("u_texture", 0);
        program.setUniformi("u_normals", 1);
        program.setUniformf("resolution", resolution);
        lightManager.calculateAndApplyLights(program);
        program.end();
    }

    @Override
    protected String getFragmentFileName() {
        return shaderDir() + "light_specular_frag.glsl";
    }

    @Override
    protected String getVertexFileName() {
        return shaderDir() + "light_vert.glsl";
    }

    private String shaderDir() {
        return resources.dir + "/shaders/";
    }
}
