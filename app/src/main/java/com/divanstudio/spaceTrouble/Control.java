package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by aaivanov on 11/29/15.
 */

public class Control {
    // Размерность элементов битмапа с рисунками кнопок
    private static final int BMP_ROWS = 1;
    private static final int BMP_COLUMNS = 4;

    // Число элементов битмапа
    private int frameCount = 0;

    private Bitmap control;           // TODO SourceControlsBitmap

    // Если будем рисовать сами свой контрол, то задаим раскраску
    private Paint paint;

    // И текст, если будем писать текст контрола
    private String text;

    // Откуда рисуем контрол
    private int canvX;  //convas coords
    private int canvY;  //convas coords

    // Размер контрола
    private int width;
    private int height;

    public Control(mainView gameView, Bitmap control, int frameCount, int canvX, int canvY) {
        this.control = control;
        this.frameCount = frameCount;
        this.width = control.getWidth() / BMP_COLUMNS;
        this.height = control.getHeight() / BMP_ROWS;
        this.canvX = canvX;
        this.canvY = canvY + gameView.getHeight() - height * 5;
    }

    public Control (int width, int height, int canvX, int canvY, String text) {
        this.width = width;
        this.height = height;
        this.canvX = canvX;
        this.canvY = canvY;

        this.paint = new Paint();
        this.text = text;
        this.control = null;

        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(0);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public void onDraw(Canvas canvas) {
        Rect src = new Rect(width * frameCount, 0, width * ( frameCount + 1), height); //part of src bitmap
        Rect dst = new Rect(canvX, canvY, canvX + width, canvY + height); // screen area
        if (control !=  null) {
            canvas.drawBitmap(control, src, dst, null);
        } else {
            canvas.drawRect(canvX, canvY, canvX + width, canvY + height, paint);
            canvas.drawText(text, canvX+width/2, canvY+height/2, paint);
        }
    }

    // Реакция на прикосновение пальцем к контролу
    public boolean isCollision(float touchEventX, float touchEventY) {
        boolean isCollision = touchEventX > canvX
                           && touchEventX < canvX + width
                           && touchEventY > canvY
                           && touchEventY < canvY + height;
        return isCollision ;
    }
}
