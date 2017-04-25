package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.divanstudio.firsttry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemies {
    private List<Enemy> meteors = new ArrayList<Enemy>();
    private StateManager state;
    private Paint paint;

    public Enemies(mainView GameView, Bitmap bmp) {
        this.state = StateManager.getInstance();
        this.meteors.add(new Enemy(GameView, bmp, 0, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 1, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 2, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 3, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 0, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 1, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 2, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 3, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 0, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 1, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 2, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 3, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 0, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 1, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 2, "ship_punch", R.raw.medium_punch_ship));
        this.meteors.add(new Enemy(GameView, bmp, 3, "ship_punch", R.raw.medium_punch_ship));
        paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.STROKE);
    }

    public boolean isCollision () {
        for(Enemy meteor : meteors) {
            if(meteor.isCollision()){
                // Коллизия метеора издаёт звук удара метеора
                meteor.playSound();

                return true;
            }
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        for(Enemy meteor : meteors) {
            meteor.onDraw(canvas);
        }
        if (state.getState() == StateManager.States.PLAY) {
            if (isCollision()) {
                state.setState(StateManager.States.GAMEOVER);
                canvas.drawRect(0, 0, 40, 40, paint);
            } else {
                // state.setState("Play");
            }
        }
    }

    public void moveUp () {
        for(Enemy meteor : meteors) {
            meteor.moveUp();
        }
    }

    public void moveDown () {
        for(Enemy meteor : meteors) {
            meteor.moveDown();
        }
    }

    public void moveStop () {
        for(Enemy meteor : meteors) {
            meteor.moveStop();
        }
    }
}
