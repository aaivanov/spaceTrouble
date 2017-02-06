package com.divanstudio.spaceTrouble;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.divanstudio.firsttry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ_DDA on 28.09.2016.
 */

public class GameMenu extends GameControl {
    // TODO Добавить название меню или другой идентификатор
    // TODO Добавить функционал выравнивания кнопок относительно первой кнопки

    private List<GameButton> Buttons = new ArrayList<>(); // Список кнопок в меню

    private String MenuDirection = "left";                // Направление расположения контролов
    private int distance = 5;                             // Дистанция между контролами в px

    // TODO Я не знаю какой тип переменной лучше
    private String touchTypes = "UP";

    private static final String TAG = GameMenu.class.getSimpleName();


    // Конструктор по умолчанию
    // Создадим в верху справа экрана меню с кнопками "Start" и "Exit"
    public GameMenu(mainView gamePanel, Context context){
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

        // Координаты кнопки с учётом отступов меню
        int button_def_canv_x = gamePanel.getWidth() - sourceExitButton.getWidth() +
                this.getLeftIndent() - this.getRightIndent();
        int button_def_canv_y = this.getTopIndent() - this.getBottomIndent();

        this.Buttons.add(
                new GameButton(
                        sourceExitButton,
                        button_def_canv_x,
                        button_def_canv_y,
                        sourceExitButton.getWidth(),
                        sourceExitButton.getHeight(),
                        "Exit_Button",
                        "Exit",
                        context,
                        State.getInstance(),
                        null
                )
        );

        Log.i(TAG,"Button 'Exit_Button' added to menu.");

        // Отсчитаем от созданной кнопки расстояние
        // По-умолчанию направление меню - справа, налево. Всегда выполняется.
        int next_button_canv_x = 0;
        if (this.MenuDirection.equals("left")) {
            next_button_canv_x = button_def_canv_x - this.distance - sourceStartButton.getWidth();
        }

        // Добавляем новую кнопку
        // TODO Рисуется от точки xy до x1y1. а не по длине.
        this.Buttons.add(
                new GameButton(
                        sourceStartButton,
                        next_button_canv_x,
                        button_def_canv_y,
                        sourceStartButton.getWidth(),
                        sourceStartButton.getHeight(),
                        "Start_Button",
                        "Play",
                        context,
                        State.getInstance(),
                        null
                )
        );

        Log.i(TAG,"Button 'Start_Button' added to menu.");
    }

    // Конструктор меню с выстраиваемыми кнопками
    public GameMenu(
            mainView gamePanel,
            Context context,
            List<GameButton> GameButtons,
            String MenuDirection,
            String menuToucheTypes,
            int distance,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b
    ) {
        // Инициализация меню как контрола
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.MenuDirection = MenuDirection;
        this.distance      = distance;
        this.touchTypes    = menuToucheTypes;

        // Добавление кнопок в меню
        // Необходимо учесть параметры меню и расположить кнопки как надо
        // Кнопки выстраиваем от координат контрола меню
        // Дистанцию между кнопками расчитываем от первой кнопки
        GameButton firstButton = GameButtons.get(0);

        // Первую кнопку рисуем в ту сторону, куда указывает параметр MenuDirection
        // Например, если у меню начало координат 0, 0 и направление меню "left",
        // то начало координат кнопки сдивигается влево на длину этой кнопки
        int button_canv_x = 0;
        int button_canv_y = 0;
        int button_width  = 0;
        int button_height = 0;

        if (firstButton.getBitmap() != null){
            button_width = firstButton.getBitmap().getWidth();
            button_height = firstButton.getBitmap().getHeight();
        } else {
            button_width = firstButton.getActivity_w();
            button_height = firstButton.getActivity_h();
        }

        if ((this.MenuDirection.toLowerCase().equals("left"))) {
            button_canv_x = this.getX() - button_width;
            button_canv_y = this.getY();
        }

        if ((this.MenuDirection.toLowerCase().equals("up"))) {
            button_canv_x = this.getX();
            button_canv_y = this.getY() - button_height;
        }

        if ((this.MenuDirection.toLowerCase().equals("right"))) {
            button_canv_x = this.getX();
            button_canv_y = this.getY();
        }

        if ((this.MenuDirection.toLowerCase().equals("down"))) {
            button_canv_x = this.getX();
            button_canv_y = this.getY();
        }

        firstButton.setX(button_canv_x);
        firstButton.setY(button_canv_y);

        // Добавляем кнопку в список
        this.Buttons.add(firstButton);

        GameButton prevButton = firstButton;
        for (GameButton Button : GameButtons) {
            // Первую кнопку уже добавили, пропускаем
            if (Button.equals(this.Buttons.get(0))) { continue; }

            int next_button_canv_x = 0;
            int next_button_canv_y = 0;
            int next_button_width  = 0;
            int next_button_height = 0;
            int prev_button_width  = 0;
            int prev_button_height = 0;

            if (prevButton.getBitmap() != null){
                prev_button_width = Button.getBitmap().getWidth();
                prev_button_height = Button.getBitmap().getHeight();
            } else {
                prev_button_width = Button.getActivity_w();
                prev_button_height = Button.getActivity_h();
            }

            if (Button.getBitmap() != null){
                next_button_width = Button.getBitmap().getWidth();
                next_button_height = Button.getBitmap().getHeight();
            } else {
                next_button_width = Button.getActivity_w();
                next_button_height = Button.getActivity_h();
            }

            // Если меню рисуем влево, то кнопка рисуется через свою ширину и дистанцию
            if ((this.MenuDirection.toLowerCase().equals("left"))) {
                next_button_canv_x = prevButton.getX() - this.distance - next_button_width;
                next_button_canv_y = prevButton.getY();
            }

            // Если меню рисуем вверх, то кнопка рисуется через свою высоту и дистанцию
            if ((this.MenuDirection.toLowerCase().equals("up"))) {
                next_button_canv_x = prevButton.getX();
                next_button_canv_y = prevButton.getY() - this.distance - next_button_height;
            }

            // Рисуем вправо - кнопка рисуется через ширину пред. кнопки и через дистанцию
            if ((this.MenuDirection.toLowerCase().equals("right"))) {
                next_button_canv_x = prevButton.getX() + this.distance + prev_button_width;
                next_button_canv_y = prevButton.getY();
            }

            // Рисуем вниз - кнопка рисуется через высоту пред. кнопки и через дистанцию
            if ((this.MenuDirection.toLowerCase().equals("down"))) {
                next_button_canv_x = prevButton.getX();
                next_button_canv_y = prevButton.getY() + this.distance + prev_button_height;
            }

            Button.setX(next_button_canv_x);
            Button.setY(next_button_canv_y);

            // Добавляем кнопку в меню
            this.Buttons.add(Button);
            Log.i(TAG,"Button '" + Button.getName() + "' added to menu.");

            // Переприсваиваем обработанную кнопку
            prevButton = Button;
        }
    }


    /*
     * Извлечение данных меню
     */
    // Извлечение первой кнопки меню
    // Можно использовать для выравнивания остальных кнопок относительно первой
    public GameButton getFirstButton() { return this.Buttons.get(0); }

    /*
     * Модификация меню
     */
    // Вставить кнопку в меню
    public void pushButton(GameButton Button){ this.Buttons.remove(Button); }


    // Удалить кнопку из меню
    public void popButton(GameButton Button){ this.Buttons.remove(Button); }


    public void disableButton(GameButton Button){
        int index;

        if (this.Buttons.contains(Button)){
            index = this.Buttons.indexOf(Button);
            this.Buttons.get(index).setInactive();
        }
    }
    /*
     * Рисование меню на экране игры
     */
    public void onDraw(Canvas canvas) {
        if (this.Buttons != null) {
            // Каждую кнопку списка рисуем на экране
            for (GameButton Button : this.Buttons){
                Button.onDraw(canvas);
                Log.i(TAG,"Button " + Button.getName() + " drew.");
            }
        }
    }


    /*
     * Обработка событий с кнопками меню
     */
    public void menuHandler(MotionEvent event){
        if (this.Buttons != null) {
            // Каждую кнопку списка анализируем на прикосновение и заставляем реагировать
            for (GameButton Button : this.Buttons){
                Button.onTouch(event, this.touchTypes);

                //TODO Нужно придумать, как блокировать другие кнопки, если одна уже нажата
                //TODO нужно придумать, как прерывать циклы обработки кнопок при разных реакциях
            }
        }
    }
}
