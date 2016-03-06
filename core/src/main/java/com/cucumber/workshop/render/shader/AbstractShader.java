package com.cucumber.workshop.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Date: 25.05.14
 * Time: 12:33
 *
 * @author Snorre E. Brekke
 */
public abstract class AbstractShader implements Shader, Disposable {
    protected ShaderProgram shaderProgram;

    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    protected void createShader() {
        String vertexFile = getVertexFileName();
        String vertexShader = Gdx.files.internal(vertexFile).readString();
        String fragmentFile =  getFragmentFileName();
        String fragmentShader = Gdx.files.internal(fragmentFile).readString();
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

        ShaderProgram.pedantic = false;
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("couldn't compile shader: "  + getVertexFileName() + " / " + getFragmentFileName() + "\n"
                                               + shaderProgram.getLog());
        }

        initProgram(shaderProgram);
    }

    protected abstract String getFragmentFileName();

    protected abstract String getVertexFileName();

    protected void initProgram(ShaderProgram program) {

    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
