package com.divanstudio.spaceTrouble;

import android.util.Log;

/**
 * Created by aaivanov on 12/22/15.
 */

// Класс, который определяет состояние игры
public class State {
    private static volatile State instance;

    private String state = new String();

    private static final String TAG = State.class.getSimpleName();

    public static State getInstance() {
        State localInstance = instance;
        if (localInstance == null) {
            synchronized (State.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new State();
                }
            }
        }
        return  localInstance;
    }

    private State () {
        this.state = "Menu";
    }

    public void setState ( String state ) {
        this.state = state;
        Log.i(TAG, "Setting the '" + this.state + "' of main game state");
    }

    public String getState () {
        return this.state;
    }

}
