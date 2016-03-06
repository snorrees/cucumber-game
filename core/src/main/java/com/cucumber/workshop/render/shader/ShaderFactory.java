package com.cucumber.workshop.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Date: 25.05.14
 * Time: 12:33
 *
 * @author Snorre E. Brekke
 */
public abstract class ShaderFactory {

    public static ShaderProgram createShader(String vertexFile, String fragmentFile) {
        String vertexShader = Gdx.files.internal(vertexFile).readString();
        String fragmentShader = Gdx.files.internal( fragmentFile).readString();

        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

        ShaderProgram.pedantic = false;
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("couldn't compile shader: "  + vertexFile + " / " +
                                               fragmentFile + "\n"
                                               + shaderProgram.getLog());
        }

        return shaderProgram;
    }
}
