package com.cucumber.workshop.logic.basic;

import com.cucumber.workshop.logic.enemy.Type;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public interface Action {

    void act(Type type,
             float fromX, float fromY,
             float toX, float toY);


}
