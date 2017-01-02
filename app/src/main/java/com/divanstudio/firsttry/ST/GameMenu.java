package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.divanstudio.firsttry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ_DDA on 28.09.2016.
 */

public class GameMenu extends GameControl {
    private List<GameButton> Buttons = new ArrayList<>(); // Список кнопок управления геймплеем

    private static final String TAG = GameMenu.class.getSimpleName();

    // TODO Добавить отступы от экрана и кнопок друг от друга


    // Конструктор по умолчанию
    // Создадим в верху справа экрана меню с кнопками "Start" и "Exit"
    public GameMenu(MainGamePanel gamePanel){
        // Инициализация меню как контрола
        super();

        // Задаём список контролов по умолчанию
        Bitmap sourceExitButton = BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.tmp_menu_exit
        );

        Log.i(TAG,"Bitmap 'tmp_menu_exit' added.");

        Bitmap sourceStartButton = BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.tmp_menu_start
        );

        Log.i(TAG,"Bitmap 'tmp_menu_start' added.");

        this.Buttons.add(
                new GameButton(
                        sourceExitButton,
                        gamePanel.getWidth() - sourceExitButton.getWidth(),
                        0,
                        sourceExitButton.getWidth(),
                        sourceExitButton.getHeight(),
                        "Exit_Button"
                )
        );

        Log.i(TAG,"Button 'Exit_Button' added.");

        // Отсчитаем от созданной кнопки расстояние
        // TODO По-умолчанию направление меню - справа, налево. А может нет?
        int canv_x_prev_button = gamePanel.getWidth() - sourceExitButton.getWidth();

        // Добавляем новую кнопку
        // TODO Рисуется от точки xy до x1y1. а не по длине.
        this.Buttons.add(
                new GameButton(
                        sourceStartButton,
                        canv_x_prev_button - sourceStartButton.getWidth(),
                        0,
                        //canv_x_prev_button,
                        sourceStartButton.getWidth(),
                        sourceStartButton.getHeight(),
                        "Start_Button"
                )
        );

        Log.i(TAG,"Button 'Start_Button' added.");
    }


    /*
     * Рисование меню на экране игры
     */
    public void onDraw(Canvas canvas) {
        // TODO проверить, что лист не пуст, может я неправильно написал
        if (Buttons != null) {
            // Каждую кнопку списка рисуем на экране
            for (GameButton Button : Buttons){
                Button.onDraw(canvas);
                Log.i(TAG,"Button " + Button.getName() + " drew.");
            }
        }
    }
}
