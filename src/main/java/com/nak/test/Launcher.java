package com.nak.test;

import com.nak.core.EngineManager;
import com.nak.core.WindowManager;
import com.nak.core.util.Constants;

public class Launcher {

    private static WindowManager window;
    private static TestGame game;

    // *********************************************************************************
    // ***********************************ENTRY*****************************************
    // *********************************************************************************
    public static void main(String[] args) throws Exception {
        // Create new window
        window = new WindowManager(Constants.TITLE, 1600, 900, true);
        // Create new game instance
        game = new TestGame();
        // Create new engine.  Handles game loop
        EngineManager engine = new EngineManager();
        // Start loop
        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // *********************************************************************************
    // ***********************************GETTERS/SETTERS*******************************
    // *********************************************************************************
    public static TestGame getGame() {
        return game;
    }

    public static WindowManager getWindow() {
        return window;
    }
}
