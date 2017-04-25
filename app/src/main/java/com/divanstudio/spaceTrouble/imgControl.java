package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Lik on 19.04.2017.
 */
public class imgControl extends Control {
    private int frameCount = 0; //image self number on bitmap

    private Bitmap controlBitmap;           // TODO SourceControlsBitmap

    public imgControl(mainView gameView, Bitmap control, int frameCount, int canvX, int canvY, String soundName, int sound_res_id, Function callBack, StateManager.States existedState) {
        this.controlBitmap = control;
        this.frameCount = frameCount;
        this.width = control.getWidth() / globalBitmapInfo.getInstance().NAV_CONTROLS_BMP_COLUMNS;
        this.height = control.getHeight() / globalBitmapInfo.getInstance().NAV_CONTROLS_BMP_ROWS;
        this.canvX = canvX;
        this.canvY = canvY + gameView.getHeight() - height * 5;
        this.collisionSound = soundName;
        this.collisionSFX.addMedia(gameView.getContext(), soundName, sound_res_id);
        this.callBack = callBack;
        this.existedState = existedState;
    }

    public imgControl(mainView gameView, Bitmap control, int frameCount, int canvX, int canvY, String soundName, int sound_res_id, Function callBack) {
        this(gameView, control, frameCount, canvX, canvY, soundName, sound_res_id, callBack, null);
    }

    public imgControl(mainView gameView, Bitmap control, int frameCount, int canvX, int canvY, String soundName, int sound_res_id) {
        this(gameView, control, frameCount, canvX, canvY, soundName, sound_res_id, null);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if ( state.getState() == existedState || existedState == null ) {
            Rect src = new Rect(width * frameCount, 0, width * (frameCount + 1), height); //part of src bitmap
            Rect dst = new Rect(canvX, canvY, canvX + width, canvY + height); // screen area
            canvas.drawBitmap(controlBitmap, src, dst, null);
        }
    }
}
