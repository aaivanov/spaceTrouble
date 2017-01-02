package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by aaivanov on 12/6/15.
 */
public class Background {
    private Bitmap background;
    private Rect src, dst;

//    public Background (mainView gameView, Bitmap bmp) {
//        background = bmp;
//        src = new Rect(0, 0, background.getWidth(), background.getHeight());
//        dst = new Rect(0, 0, gameView.getWidth(), gameView.getHeight());
//    }

    //TODO WJ
    public Background (MainGamePanel gamePanel, Bitmap bmp) {
        background = bmp;
        src = new Rect(0, 0, background.getWidth(), background.getHeight());
        dst = new Rect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
    }

    public void onDraw (Canvas canvas) {
        canvas.drawBitmap(background, src, dst, null);
    }

}
