package com.divanstudio.firsttry.ST;

/**
 * Created by aaivanov on 12/22/15.
 */

// Класс, который определяет состояние игры
public class State {
    private static volatile State instance;    // Экземпляр состояния (что это значит?)

    //TODO упростил
    //private String state = new String();
    private String state = "";    // Состяние игры
                                  // Принимает значения:
                                  // "Menu" - Меню игры
                                  // "Gameplay" - Игровой процесс игры

    private State () {
        this.state = "Menu";
    }

    // TODO. WJ написал. Возможно, не нужно
    private State (String state) {
        this.state = state;
    }

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

    public void setState ( String state ) {
        this.state = state;
    }

    public String getState () {
        return this.state;
    }

}
