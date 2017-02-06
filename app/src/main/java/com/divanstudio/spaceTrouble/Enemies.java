package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemies {
    private List<Enemy> meteors = new ArrayList<Enemy>();
    private State state;
    private Paint paint;

    public Enemies(mainView GameView, Bitmap bmp) {
        this.state = State.getInstance();
        this.meteors.add(new Enemy(GameView, bmp, 0));
        this.meteors.add(new Enemy(GameView, bmp, 1));
        this.meteors.add(new Enemy(GameView, bmp, 2));
        this.meteors.add(new Enemy(GameView, bmp, 3));
        this.meteors.add(new Enemy(GameView, bmp, 0));
        this.meteors.add(new Enemy(GameView, bmp, 1));
        this.meteors.add(new Enemy(GameView, bmp, 2));
        this.meteors.add(new Enemy(GameView, bmp, 3));

        paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.STROKE);


    }

    public boolean isCollision () {
        for(Enemy meteor : meteors) {
            if(meteor.isCollision()){
                return true;
            }
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        for(Enemy meteor : meteors) {
            meteor.onDraw(canvas);
        }
        if (isCollision()) {
           // state.setState("Menu");
            canvas.drawRect(0, 0, 40, 40, paint);
        } else {
           // state.setState("Play");
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
