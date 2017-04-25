package com.divanstudio.spaceTrouble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by aaivanov on 19.04.2017.
 */
public class textControl extends Control {
    private Paint paint;

    private String text;
    private int testPosX;
    private int textPosY;

    public textControl (Context context, int width, int height, int canvX, int canvY, String text, String soundName, int sound_id, Function callBack, StateManager.States existedState) {
        this.width = width;
        this.height = height;
        this.canvX = canvX;
        this.canvY = canvY;
        this.callBack = callBack;
        this.existedState = existedState;

        this.paint = new Paint();
        this.text = text;

        this.collisionSound = soundName;
        this.collisionSFX = new MediaPlaylist();
        this.collisionSFX.addMedia(context, soundName, sound_id);

        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(0);
        this.paint.setStyle(Paint.Style.STROKE);
        this.testPosX = (int) ((width / 2)  - paint.measureText(text) / 2 );
        this.textPosY = (int) ((height / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
    }

    public textControl (Context context, int width, int height, int canvX, int canvY, String text, String soundName, int sound_id, Function callBack) {
        this(context, width, height, canvX, canvY, text, soundName, sound_id, callBack, null);
    }

    public textControl (Context context, int width, int height, int canvX, int canvY, String text, String soundName, int sound_id) {
        this (context, width, height, canvX, canvY, text, soundName, sound_id, null);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if ( state.getState() == existedState || existedState == null ) {
            canvas.drawRect(canvX, canvY, canvX + width, canvY + height, paint);
            canvas.drawText(text, canvX + testPosX, canvY + textPosY, paint);
        }
    }
}
