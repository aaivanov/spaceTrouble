package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by aaivanov on 12/6/15.
 */
public class Background {
    private Bitmap background;
    private Rect src, dst;

    private static final String TAG = Background.class.getSimpleName();

    public Background (mainView gameView, Bitmap bmp) {
        background = bmp;
        src = new Rect(0, 0, background.getWidth(), background.getHeight());
        dst = new Rect(0, 0, gameView.getWidth(), gameView.getHeight());
    }

    public Background(MainMenuView mainMenuView, Bitmap bmp) {
        this.background = bmp;
        this.src = new Rect(0, 0, this.background.getWidth(), this.background.getHeight());
        this.dst = new Rect(0, 0, mainMenuView.getWidth(), mainMenuView.getHeight());
    }

    public void setBackground(Bitmap newBitmap) {
        this.background = newBitmap;
    }

    public void onDraw (Canvas canvas) {
        canvas.drawBitmap(
                this.background,
                this.src,
                this.dst,
                null
        );
    }


    // Анимация затемнения экрана
    public void backgroundDarkerAnimation() {
        // TODO ЗАТЫК. Чтобы затемнить, надо научить скрещивать бэкграунд с Альфа-прямоугольником
        Log.i(TAG,"Animation starts");
        Bitmap mutableBitmap = this.background.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(mutableBitmap);

        // Берём данные бэкграунда и накладываем чёрный прямоугольник поверх
        int width = this.background.getWidth();
        int height = this.background.getHeight();

        // Инициализируем цвет каждого пикселя (это вообще оптимально?)
        // Цвет - чёрный. Альфа канал на максимум (временно)
        int[] colors = new int[width * height];
        Arrays.fill(colors, 0, width * height, Color.argb(255, 0, 0, 0));

        Log.i(TAG,"BKGRND Size: " + width + ", " + height);

        Bitmap BlackBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Рисуем новый бэкграунд поверх старого
        this.src = new Rect(0, 0, BlackBitmap.getWidth(), BlackBitmap.getHeight());
        this.dst = new Rect(0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight());

        // Рисуем бэкграунд на холсте с учётом всех настроек (Так нельзя, канва занята)
        canvas.drawBitmap(BlackBitmap, this.src, this.dst, null);

        // Делаем новый бэкграунд основным
//        setBackground(BlackBitmap);

        Log.i(TAG,"Animation complete");
    }

}
