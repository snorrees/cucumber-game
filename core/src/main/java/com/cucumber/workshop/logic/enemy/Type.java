package com.cucumber.workshop.logic.enemy;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public enum Type {
    GREEN("green", 0, 1, 0),
    BLUE("blue", 0, 0, 1),
    RED("red", 1, 0, 0);

    public final float r, g, b;
    public final String image;

    Type(String image, float r, float g, float b) {
        this.image = image;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Type nextType(){
        final int nextIndex = (ordinal() + 1) % values().length;
        return values()[nextIndex];
    }

    public Type previousType(){
        int nextIndex = (ordinal() - 1) % values().length;
        if (nextIndex < 0){
            nextIndex += values().length;
        }
        return values()[nextIndex];
    }

    public static Type randomType() {
        return values()[(int) (values().length * MathUtils.random())];
    }
}
