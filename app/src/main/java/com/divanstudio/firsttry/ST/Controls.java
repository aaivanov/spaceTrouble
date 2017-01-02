package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/1/15.
 */
public class Controls {

    private List<Control> Controls = new ArrayList<Control>();
    private Control menu;
    private Enemies meteors;

    private State state;

    private Player player;

//    public Controls (mainView GameView, Bitmap bmp, Enemies meteors) {
//        Controls.add(new Control(GameView, bmp, 0, 30, 50));
//        Controls.add(new Control(GameView, bmp, 1, 30, 150));
//        this.player = Player.getInstance();
//        this.state = State.getInstance();
//        menu = new Control(300, 50, 100, 100, "START");
//        this.meteors = meteors;
//
//    }

    //TODO WJ
    // Создаёт кнопочку старта игры при создании контрола
    public Controls (MainGamePanel gamePanel, Bitmap bmp, Enemies meteors) {
        Controls.add(new Control(gamePanel, bmp, 0, 30, 50));
        Controls.add(new Control(gamePanel, bmp, 1, 30, 150));
        this.player = Player.getInstance();
        this.state = State.getInstance();
        menu = new Control(300, 50, 100, 100, "START");
        this.meteors = meteors;

    }

    public void onDraw(Canvas canvas) {
        if(state.getState() != "Menu") {
        //if(!state.getState().equals("Menu")) {
            for (Control control : Controls) {
                control.onDraw(canvas);
            }
        }
        if (state.getState() == "Menu") {
        //if (state.getState().equals("Menu")) {
            menu.onDraw(canvas);
        }
    }

    // Обработка нажатий на контролы управления
    public void isCollision( MotionEvent event ) {
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:             // нажатие
                mouseEventHandler(event);
                break;

            case MotionEvent.ACTION_MOVE:             //движение
                mouseEventHandler(event);
                break;

            case MotionEvent.ACTION_UP:               //отпускание
            case MotionEvent.ACTION_CANCEL:
                if(state.getState() == "Menu") {
                //if(state.getState().equals("Menu")) {
                    state.setState("Play");
                } else {
                    meteors.moveStop();
                }
                break;
        }
    }

    public void mouseEventHandler ( MotionEvent event) {
        for (int i = Controls.size() - 1; i >= 0; i--) {
            Control control = Controls.get(i);
            if (control.isCollision(event.getX(), event.getY())) {
                if (i == 0){
                    meteors.moveDown();
                }
                if (i == 1) {
                    meteors.moveUp();
                }
            }
        }
    }
}
