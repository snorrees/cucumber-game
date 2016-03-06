package com.cucumber.workshop.render.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Date: 07.12.13
 * Time: 15:17
 *
 * @author Snorre E. Brekke
 */
public interface Shader extends Disposable {
    void update();

    ShaderProgram getShaderProgram();

    void dispose();
}
