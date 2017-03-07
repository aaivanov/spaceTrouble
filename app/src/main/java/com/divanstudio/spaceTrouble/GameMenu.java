package com.divanstudio.spaceTrouble;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

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
                        "Exit"
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
        this.Buttons.add(
                new GameButton(
                        sourceStartButton,
                        next_button_canv_x,
                        button_def_canv_y,
                        sourceStartButton.getWidth(),
                        sourceStartButton.getHeight(),
                        "Start_Button",
                        "Play"
                )
        );

        Log.i(TAG,"Button 'Start_Button' added to menu.");
    }

    // Конструктор меню с выстраиваемыми кнопками
    public GameMenu(
//            mainView gamePanel,
//            Context context,
            List<GameButton> GameButtons,
            String MenuDirection,
            String align,
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

        // Добавление кнопок в меню
        // Чтобы выровнять кнопки по направлению расположения их в меню,
        // нужно определить кнопку с максимальной шириной или высотой.
        int max_width = 0;
        int max_height = 0;
        for (GameButton Button : GameButtons) {
            if (max_width < Button.getBitmap().getWidth()){
                max_width = Button.getBitmap().getWidth();
            }

            if (max_height < Button.getBitmap().getHeight()){
                max_height = Button.getBitmap().getHeight();
            }
        }
        // Кнопки выстраиваем от координат контрола меню
        // Дистанцию между кнопками расчитываем от первой кнопки
        GameButton firstButton = GameButtons.get(0);

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

        // Первую кнопку рисуем в ту сторону, куда указывает параметр MenuDirection
        // Например, если у меню начало координат 0, 0 и направление меню "left",
        // то начало координат кнопки сдивигается влево на длину этой кнопки.
        // Так же учтём выравнивание.
        // Если нужно выравнивать по центру, к координате Y кнопки нужно прибавить нужный отступ,
        // Который высчитвыается от max_width и max_height.
        if ((this.MenuDirection.toLowerCase().equals("left"))) {
            button_canv_x = this.getX() - button_width;
            button_canv_y = this.getY();

            // В этом случае выравниваем кнопку по высоте
            if (align.toLowerCase().equals("center")) {
                button_canv_y += (max_height - button_height)/2;
            }

            if (align.toLowerCase().equals("bottom")) {
                button_canv_y += max_height - button_height;
            }
        }

        if ((this.MenuDirection.toLowerCase().equals("up"))) {
            button_canv_x = this.getX();
            button_canv_y = this.getY() - button_height;

            // В этом случае выравниваем кнопку по ширине
            if (align.toLowerCase().equals("center")) {
                button_canv_x += (max_width - button_width)/2;
            }

            if (align.toLowerCase().equals("right")) {
                button_canv_x += max_width - button_width;
            }
        }

        if ((this.MenuDirection.toLowerCase().equals("right"))) {
            button_canv_x = this.getX();
            button_canv_y = this.getY();

            // В этом случае выравниваем кнопку по высоте
            if (align.toLowerCase().equals("center")) {
                button_canv_y += (max_height - button_height)/2;
            }

            if (align.toLowerCase().equals("bottom")) {
                button_canv_y += max_height - button_height;
            }
        }

        if ((this.MenuDirection.toLowerCase().equals("down"))) {
            button_canv_x = this.getX();
            button_canv_y = this.getY();

            // В этом случае выравниваем кнопку по ширине
            if (align.toLowerCase().equals("center")) {
                button_canv_x += (max_width - button_width)/2;
            }

            if (align.toLowerCase().equals("right")) {
                button_canv_x += max_width - button_width;
            }
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

            // Рисуем кнопки. Для этого перессчитываем нужную координату,
            // учитывая дистанцию между кнопками и направление.
            // Вторая координата отвечает за выравнивание. Её пересчитываем согласно максимальной
            if ((this.MenuDirection.toLowerCase().equals("left"))) {
                next_button_canv_x = prevButton.getX() - this.distance - next_button_width;
                next_button_canv_y = this.getY();

                if (align.toLowerCase().equals("center")) {
                    next_button_canv_y += (max_height - next_button_height)/2;
                }

                if (align.toLowerCase().equals("bottom")) {
                    next_button_canv_y += max_height - next_button_height;
                }
            }

            if ((this.MenuDirection.toLowerCase().equals("up"))) {
                next_button_canv_x = this.getX();
                next_button_canv_y = prevButton.getY() - this.distance - next_button_height;

                if (align.toLowerCase().equals("center")) {
                    next_button_canv_x += (max_width - next_button_width)/2;
                }

                if (align.toLowerCase().equals("right")) {
                    next_button_canv_x += max_width - next_button_width;
                }
            }

            if ((this.MenuDirection.toLowerCase().equals("right"))) {
                next_button_canv_x = prevButton.getX() + this.distance + prev_button_width;
                next_button_canv_y = this.getY();

                if (align.toLowerCase().equals("center")) {
                    next_button_canv_y += (max_height - next_button_height)/2;
                }

                if (align.toLowerCase().equals("bottom")) {
                    next_button_canv_y += max_height - next_button_height;
                }
            }

            if ((this.MenuDirection.toLowerCase().equals("down"))) {
                next_button_canv_x = this.getX();
                next_button_canv_y = prevButton.getY() + this.distance + prev_button_height;

                if (align.toLowerCase().equals("center")) {
                    next_button_canv_x += (max_width - next_button_width)/2;
                }

                if (align.toLowerCase().equals("right")) {
                    next_button_canv_x += max_width - next_button_width;
                }
            }

            Button.setX(next_button_canv_x);
            Button.setY(next_button_canv_y);

            // Добавляем кнопку в меню
            this.Buttons.add(Button);

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
    public void pushButton(GameButton Button){ this.Buttons.add(Button); }


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
            }
        }
    }


    public GameButton getTouchedButton(MotionEvent event) {
        for (GameButton Button : this.Buttons) {
            if (Button.isPressed(event)) return Button;
        }

        return null;
    }
}
