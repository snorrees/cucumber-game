package com.cucumber.workshop;

import com.badlogic.gdx.Game;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class GdxArtemisGame extends Game {

    private final ResourceDirectory resourceDirectory;

    public GdxArtemisGame(ResourceDirectory resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }

    @Override
    public void create() {
        restart();
    }

    public void restart() {
        setScreen(new GameScreen(resourceDirectory));
    }

}
