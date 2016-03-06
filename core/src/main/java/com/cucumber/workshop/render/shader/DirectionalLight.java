package com.cucumber.workshop.render.shader;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;


public class DirectionalLight {
    final public Color color = new Color(
            random(0.6f, 1f),
            random(0.6f, 1f),
            random(0.6f, 1f),
            1f
    );
    final public Vector3 direction = new Vector3(
            random(-1f, 1f),
            random(-1f, 1f),
            random(0.2f, 1.2f)
    );
}
