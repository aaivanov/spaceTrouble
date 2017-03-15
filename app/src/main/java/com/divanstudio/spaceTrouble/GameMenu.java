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

    // TODO убрать этот параметр
    private List<GameButton> Buttons = new ArrayList<>();      // Список кнопок в меню

    private List<GameControl> GameControls = new ArrayList<>();// Список любых контролов игры

    private String MenuDirection = "left";                     // Направление расположения контролов

    private int distance = 5;                                  // Дистанция между контролами в px

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


    // Конструктор меню с выстраиваемыми элементами
    public GameMenu(
            List<GameControl> GameControls,
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

        // Добавление элементов UI в меню
        // За основу размерности элемента берём его зону активности.
        // Нужно определить элемент с максимальной шириной или высотой.
        int max_width = 0;
        int max_height = 0;
        for (GameControl control : GameControls) {
            int control_width = 0;
            int control_height = 0;

            // Высчитываем размеры контрола в зависимости от его типа:
            switch(control.getClass().getSimpleName()) {
                case "GameMenu":
                    GameMenu tmpMenu = (GameMenu) control;

                    control_width = tmpMenu.getMenuWidth();
                    control_height = tmpMenu.getMenuHeight();

                    break;

                case "GameButton":
                    control_width = control.getActivity_w();
                    control_height = control.getActivity_h();

                    break;
            }

            if (max_width < control_width){
                max_width = control_width;
            }

            if (max_height < control_height){
                max_height = control_height;
            }
        }

        // Кнопки выстраиваем от координат контрола меню
        // Дистанцию между кнопками расчитываем от первой кнопки
        GameControl firstControl = GameControls.get(0);

        int control_canv_x = 0;
        int control_canv_y = 0;
        int control_width  = 0;
        int control_height = 0;

        // Высчитываем размеры контрола в зависимости от его типа:
        switch(firstControl.getClass().getSimpleName()) {
            case "GameMenu":
                GameMenu tmpMenu = (GameMenu) firstControl;

                control_width = tmpMenu.getMenuWidth();
                control_height = tmpMenu.getMenuHeight();

                break;

            case "GameButton":
                control_width = firstControl.getActivity_w();
                control_height = firstControl.getActivity_h();

                break;
        }

        // Первую кнопку рисуем в ту сторону, куда указывает параметр MenuDirection
        // Например, если у меню начало координат 0, 0 и направление меню "left",
        // то начало координат кнопки сдивигается влево на длину этой кнопки.
        // Так же учтём выравнивание.
        // Если нужно выравнивать по центру, к координате Y кнопки нужно прибавить нужный отступ,
        // Который высчитвыается от max_width и max_height.
        if ((this.MenuDirection.toLowerCase().equals("left"))) {
            control_canv_x = this.getX() - control_width;
            control_canv_y = this.getY();

            // В этом случае выравниваем кнопку по высоте
            if (align.toLowerCase().equals("center")) {
                control_canv_y += (max_height - control_height)/2;
            }

            if (align.toLowerCase().equals("bottom")) {
                control_canv_y += max_height - control_height;
            }
        }

        if ((this.MenuDirection.toLowerCase().equals("up"))) {
            control_canv_x = this.getX();
            control_canv_y = this.getY() - control_height;

            // В этом случае выравниваем кнопку по ширине
            if (align.toLowerCase().equals("center")) {
                control_canv_x += (max_width - control_width)/2;
            }

            if (align.toLowerCase().equals("right")) {
                control_canv_x += max_width - control_width;
            }
        }

        if ((this.MenuDirection.toLowerCase().equals("right"))) {
            control_canv_x = this.getX();
            control_canv_y = this.getY();

            // В этом случае выравниваем кнопку по высоте
            if (align.toLowerCase().equals("center")) {
                control_canv_y += (max_height - control_height)/2;
            }

            if (align.toLowerCase().equals("bottom")) {
                control_canv_y += max_height - control_height;
            }
        }

        if ((this.MenuDirection.toLowerCase().equals("down"))) {
            control_canv_x = this.getX();
            control_canv_y = this.getY();

            // В этом случае выравниваем кнопку по ширине
            if (align.toLowerCase().equals("center")) {
                control_canv_x += (max_width - control_width)/2;
            }

            if (align.toLowerCase().equals("right")) {
                control_canv_x += max_width - control_width;
            }
        }

        firstControl.setX(control_canv_x);
        firstControl.setY(control_canv_y);

        // Добавляем контрол в список
        this.GameControls.add(firstControl);

        GameControl prevControl = firstControl;
        for (GameControl control : GameControls) {
            // Первую кнопку уже добавили, пропускаем
            if (control.equals(this.GameControls.get(0))) { continue; }

            int next_control_canv_x = 0;
            int next_control_canv_y = 0;
            int next_control_width  = 0;
            int next_control_height = 0;
            int prev_control_width  = 0;
            int prev_control_height = 0;

            // Высчитываем размеры предыдущего и следующего контрола в зависимости от типа:
            switch(firstControl.getClass().getSimpleName()) {
                case "GameMenu":
                    GameMenu tmpMenu = (GameMenu) firstControl;

                    prev_control_width = tmpMenu.getMenuWidth();
                    prev_control_height = tmpMenu.getMenuHeight();

                    next_control_width = tmpMenu.getMenuWidth();
                    next_control_height = tmpMenu.getMenuHeight();

                    break;

                case "GameButton":
                    prev_control_width = prevControl.getActivity_w();
                    prev_control_height = prevControl.getActivity_h();

                    next_control_width = control.getActivity_w();
                    next_control_height = control.getActivity_h();

                    break;
            }

            // Рисуем кнопки. Для этого перессчитываем нужную координату,
            // учитывая дистанцию между кнопками и направление.
            // Вторая координата отвечает за выравнивание. Её пересчитываем согласно максимальной
            if ((this.MenuDirection.toLowerCase().equals("left"))) {
                next_control_canv_x = prevControl.getX() - this.distance - next_control_width;
                next_control_canv_y = this.getY();

                if (align.toLowerCase().equals("center")) {
                    next_control_canv_y += (max_height - next_control_height)/2;
                }

                if (align.toLowerCase().equals("bottom")) {
                    next_control_canv_y += max_height - next_control_height;
                }
            }

            if ((this.MenuDirection.toLowerCase().equals("up"))) {
                next_control_canv_x = this.getX();
                next_control_canv_y = prevControl.getY() - this.distance - next_control_height;

                if (align.toLowerCase().equals("center")) {
                    next_control_canv_x += (max_width - next_control_width)/2;
                }

                if (align.toLowerCase().equals("right")) {
                    next_control_canv_x += max_width - next_control_width;
                }
            }

            if ((this.MenuDirection.toLowerCase().equals("right"))) {
                next_control_canv_x = prevControl.getX() + this.distance + prev_control_width;
                next_control_canv_y = this.getY();

                if (align.toLowerCase().equals("center")) {
                    next_control_canv_y += (max_height - next_control_height)/2;
                }

                if (align.toLowerCase().equals("bottom")) {
                    next_control_canv_y += max_height - next_control_height;
                }
            }

            if ((this.MenuDirection.toLowerCase().equals("down"))) {
                next_control_canv_x = this.getX();
                next_control_canv_y = prevControl.getY() + this.distance + prev_control_height;

                if (align.toLowerCase().equals("center")) {
                    next_control_canv_x += (max_width - next_control_width)/2;
                }

                if (align.toLowerCase().equals("right")) {
                    next_control_canv_x += max_width - next_control_width;
                }
            }

            control.setX(next_control_canv_x);
            control.setY(next_control_canv_y);

            // Добавляем кнопку в меню
            this.GameControls.add(control);

            // Переприсваиваем обработанную кнопку
            prevControl = control;
        }
    }


    /*
     * Извлечение данных меню
     */
    // TODO Обобщить на GameControl
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


    // Вернуть ширину меню (считается по элементам меню)
    // ВНИМАНИЕ! Рекурсивная функция
    public int getMenuWidth(){
        int menu_width = 0;
        if (this.GameControls != null){
            for (int i = 0; i < this.GameControls.size(); i++) {
                switch (this.GameControls.get(i).getClass().getSimpleName()){
                    case "GameButton":
                        GameButton Button = (GameButton) this.GameControls.get(i);

                        // Считаем ширину меню по ширине зоны активности кнопки
                        menu_width += Button.getActivity_w();

                        // Если направление меню "влево" или "вправо", добавляем дистанцию
                        if (this.MenuDirection.equals("left") || this.MenuDirection.equals("right")) {
                            // А так же проверяем, что кнопка не последняя
                            if (i != (this.GameControls.size() - 1)) {
                                menu_width += this.distance;
                            }
                        }

                        break;

                    case "GameMenu":
                        GameMenu Menu = (GameMenu) this.GameControls.get(i);

                        // Определяем рекурсивно размер меню
                        menu_width += Menu.getMenuWidth();

                        // Если направление меню "влево" или "вправо", добавляем дистанцию
                        if (this.MenuDirection.equals("left") || this.MenuDirection.equals("right")) {
                            // А так же проверяем, что кнопка не последняя
                            if (i != (this.GameControls.size() - 1)) {
                                menu_width += this.distance;
                            }
                        }

                        break;
                }
            }
        }

        return menu_width;
    }


    // Вернуть высоту меню (считается по элементам меню)
    // ВНИМАНИЕ! Рекурсивная функция
    public int getMenuHeight(){
        int menu_height = 0;
        if (this.GameControls != null){
            for (int i = 0; i < this.GameControls.size(); i++) {
                switch (this.GameControls.get(i).getClass().getSimpleName()){
                    case "GameButton":
                        GameButton Button = (GameButton) this.GameControls.get(i);

                        // Считаем ширину меню по ширине зоны активности кнопки
                        menu_height += Button.getActivity_w();

                        // Если направление меню "влево" или "вправо", добавляем дистанцию
                        if (this.MenuDirection.equals("up") || this.MenuDirection.equals("down")) {
                            // А так же проверяем, что кнопка не последняя
                            if (i != (this.GameControls.size() - 1)) {
                                menu_height += this.distance;
                            }
                        }

                        break;

                    case "GameMenu":
                        GameMenu Menu = (GameMenu) this.GameControls.get(i);

                        // Определяем рекурсивно размер меню
                        menu_height += Menu.getMenuHeight();

                        // Если направление меню "влево" или "вправо", добавляем дистанцию
                        if (this.MenuDirection.equals("up") || this.MenuDirection.equals("down")) {
                            // А так же проверяем, что кнопка не последняя
                            if (i != (this.GameControls.size() - 1)) {
                                menu_height += this.distance;
                            }
                        }

                        break;
                }
            }
        }

        return menu_height;
    }



    //Рисование меню на экране игры
    // ВНИМАНИЕ! Рекурсивная функция
    public void onDraw(Canvas canvas) {
        // Если есть контролы, проверяем их тип и рисуем
        if (this.GameControls != null) {
            for (GameControl control : this.GameControls) {
                switch (control.getClass().getSimpleName()){
                    case "GameButton":
                        // Создаём объект, чтобы нарисовать кнопку
                        GameButton Button = (GameButton) control;

                        // Рисуем
                        Button.onDraw(canvas);

                        break;

                    case "GameMenu":
                        // Создаём объект меню
                        GameMenu Menu = (GameMenu) control;

                        // Рисуем
                        Menu.onDraw(canvas);

                        break;
                }
            }
        }
    }


    // Возвращает объект меню, к которому прикоснулся юзер
    // ВНИМАНИЕ! Рекурсивная функция
    // TODO доработать до всех контролов, которые нажимаются
    public GameButton getTouchedButton(MotionEvent event) {
        // Декларируем кнопку
        GameButton Button = null;

        if (this.GameControls != null) {
            for (GameControl control : this.GameControls) {
                switch (control.getClass().getSimpleName()) {
                    case "GameButton":
                        // Если в меню элемент - кнопка, запоминаем её
                        Button = (GameButton) control;

                        break;

                    case "GameMenu":
                        // Если в меню элемент - меню
                        GameMenu Menu = (GameMenu) control;

                        // Определяем кнопку рекурсивным вызовом метода getTouchedButton()
                        Button = Menu.getTouchedButton(event);

                        break;
                }

                // Если кнопка таки нашлась, проверяем нажата ли она и возвращаем
                if (Button != null) {
                    if (Button.isPressed(event)) {
                        return Button;
                    }
                }
            }
        }

        return null;
    }
}
