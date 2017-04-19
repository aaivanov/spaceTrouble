package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

import java.util.Arrays;

/**
 * Created by aaivanov on 12/6/15.
 */
public class Background {
    private Bitmap background;
    private Rect src, dst;

    private static final String TAG = Background.class.getSimpleName();

    public Background () {
        this.background = null;
        this.src = null;
        this.dst = null;
    }


    public Background (SurfaceView View, Bitmap bmp) {
        background = bmp;
        src = new Rect(0, 0, background.getWidth(), background.getHeight());
        dst = new Rect(0, 0, View.getWidth(), View.getHeight());
    }


    public void setBackgroundSourceOnView(SurfaceView View, Bitmap newBitmap) {
        this.background = newBitmap;
        this.src = new Rect(0, 0, this.background.getWidth(), this.background.getHeight());
        this.dst = new Rect(0, 0, View.getWidth(), View.getHeight());
    }


    public void onDraw (Canvas canvas) {
        try {
            canvas.drawBitmap(
                    this.background,
                    this.src,
                    this.dst,
                    null
            );
        }
        catch (NullPointerException e) {
            Log.e(TAG, "Background unset");
            e.printStackTrace();
        }
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


    public int getWidth() {
        return this.background.getWidth();
    }


    public int getHeight() {
        return this.background.getHeight();
    }

}
