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
    private String Align = "";                                 // Выравнивание элементов
    private String Centering = "";                             // Центрирование меню по осям

    private int distance = 5;                                  // Дистанция между контролами в px

    private static final String TAG = GameMenu.class.getSimpleName();


    // Конструктор по умолчанию
    // Создадим в верху справа экрана меню с кнопками "Start" и "Exit"
    // TODO убрать список кнопок, использоватть список контролов
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
            String Align,
            String Centering,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            int distance
    ) {
        // Инициализация меню как контрола
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.GameControls  = GameControls;
        this.MenuDirection = MenuDirection;
        this.Align         = Align;
        this.Centering     = Centering;
        this.distance      = distance;

        this.relocate(this.getX(), this.getY());
    }


    // Конструктор меню с выстраиваемыми элементами без зоны активности и отступов
    public GameMenu(
            List<GameControl> GameControls,
            String MenuDirection,
            String Align,
            String Centering,
            int canv_x,
            int canv_y,
            int distance
    ) {
        // Инициализация меню как контрола
        super(canv_x, canv_y, 0, 0, 0, 0, 0, 0);

        this.GameControls  = GameControls;
        this.MenuDirection = MenuDirection;
        this.Align         = Align;
        this.Centering     = Centering;
        this.distance      = distance;

        this.relocate(this.getX(), this.getY());
    }


    // Упрощённый Конструктор меню с кнопками
    // Подразумевает вывзов метода relocate()
    public GameMenu(
            List<GameControl> GameControls,
            String MenuDirection,
            String Align,
            String Centering
    ) {
        // Инициализация меню как контрола
        super(0, 0, 0, 0, 0, 0, 0, 0);

        this.GameControls  = GameControls;
        this.MenuDirection = MenuDirection;
        this.Align         = Align;
        this.Centering     = Centering;
        this.distance      = 0;

        Log.w(TAG, "Created menu needs relocation of elements. Use 'relocate()' method");
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
                    case "GameMenu":
                        GameMenu Menu = (GameMenu) this.GameControls.get(i);

                        // Если меню горизонтальной направленности...
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            // Определяем рекурсивно размер суб-меню и добавляем в ширину меню
                            menu_width += Menu.getMenuWidth();

                            // А так же проверяем, что суб-меню не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к ширине меню
                                menu_width += this.distance;
                            }
                        }

                        // Если меню вертикальной направленности,
                        // считаем рекурсивно ширину максимального суб-меню
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_width < Menu.getMenuWidth()){
                                menu_width = Menu.getMenuWidth();
                            }
                        }

                        break;

                    case "GameButton":
                        GameButton Button = (GameButton) this.GameControls.get(i);

                        // Если меню горизонтальной направленности...
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем ширину меню по ширине зоны активности кнопки
                            menu_width += Button.getActivity_w();

                            // А так же проверяем, что кнопка не последняя
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к ширине меню
                                menu_width += this.distance;
                            }
                        }

                        // Если меню вертикальной направленности,
                        // берём ширину кнопки как максимальную
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_width < Button.getActivity_w()){
                                menu_width = Button.getActivity_w();
                            }
                        }

                        break;

                    case "GameLabel":
                        GameLabel Label = (GameLabel) this.GameControls.get(i);

                        // Если меню горизонтальной направленности...
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем ширину меню по высоте зоны активности лейбла
                            menu_width += Label.getActivity_w();

                            // А так же проверяем, что лейбл не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к ширине меню
                                menu_width += this.distance;
                            }
                        }

                        // Если меню вертикальной направленности,
                        // берём ширину лейбла как максимальную
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_width < Label.getActivity_w()){
                                menu_width = Label.getActivity_w();
                            }
                        }

                        break;

                    case "GameCheckbox":
                        GameCheckbox Checkbox = (GameCheckbox) this.GameControls.get(i);

                        // Если меню горизонтальной направленности...
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем ширину меню по высоте чекбокса
                            menu_width += Checkbox.getWidth();

                            // А так же проверяем, что чекбокс не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к ширине меню
                                menu_width += this.distance;
                            }
                        }

                        // Если меню вертикальной направленности,
                        // берём ширину чекбокса как максимальную
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_width < Checkbox.getWidth()){
                                menu_width = Checkbox.getWidth();
                            }
                        }

                        break;

                    case "GameGroupCheckbox":
                        GameGroupCheckbox GroupCheckbox = (GameGroupCheckbox) this.GameControls.get(i);

                        // Если меню горизонтальной направленности...
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем ширину меню по высоте группового чекбокса
                            menu_width += GroupCheckbox.getWidth();

                            // А так же проверяем, что групповой чекбокс не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к ширине меню
                                menu_width += this.distance;
                            }
                        }

                        // Если меню вертикальной направленности,
                        // берём ширину группового чекбокса как максимальную
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_width < GroupCheckbox.getWidth()){
                                menu_width = GroupCheckbox.getWidth();
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
                switch (this.GameControls.get(i).getClass().getSimpleName()) {
                    case "GameMenu":
                        GameMenu Menu = (GameMenu) this.GameControls.get(i);

                        // Если меню вертикальной направленности...
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            // Определяем рекурсивно высоту суб-меню и добавляем к высоте меню
                            menu_height += Menu.getMenuHeight();

                            // А так же проверяем, что суб-меню не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к высоте меню
                                menu_height += this.distance;
                            }
                        }

                        // Если меню горизонтальной направленности,
                        // считаем рекурсивно высоту максимального суб-меню
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_height < Menu.getMenuHeight()){
                                menu_height = Menu.getMenuHeight();
                            }
                        }

                        break;

                    case "GameButton":
                        GameButton Button = (GameButton) this.GameControls.get(i);

                        // Если меню вертикальной направленности...
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем высоту меню по высоте зоны активности кнопки
                            menu_height += Button.getActivity_h();

                            // А так же проверяем, что кнопка не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к высоте меню
                                menu_height += this.distance;
                            }
                        }

                        // Если меню горизонтальной направленности,
                        // берём высоту кнопки как максимальную
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_height < Button.getActivity_h()){
                                menu_height = Button.getActivity_h();
                            }
                        }

                        break;

                    case "GameLabel":
                        GameLabel Label = (GameLabel) this.GameControls.get(i);

                        // Если меню вертикальной направленности...
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем высоту меню по высоте зоны активности лейбла
                            menu_height += Label.getActivity_h();

                            // А так же проверяем, что лейбл не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к высоте меню
                                menu_height += this.distance;
                            }
                        }

                        // Если меню горизонтальной направленности,
                        // берём высоту лейбла как максимальную
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_height < Label.getActivity_h()){
                                menu_height = Label.getActivity_h();
                            }
                        }

                        break;

                    case "GameCheckbox":
                        GameCheckbox Checkbox = (GameCheckbox) this.GameControls.get(i);

                        // Если меню вертикальной направленности...
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем высоту меню по высоте чекбокса
                            menu_height += Checkbox.getHeight();

                            // А так же проверяем, что чекбокс не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к высоте меню
                                menu_height += this.distance;
                            }
                        }

                        // Если меню горизонтальной направленности,
                        // берём высоту чекбокса как максимальную
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_height < Checkbox.getHeight()){
                                menu_height = Checkbox.getHeight();
                            }
                        }

                        break;

                    case "GameGroupCheckbox":
                        GameGroupCheckbox GroupCheckbox = (GameGroupCheckbox) this.GameControls.get(i);

                        // Если меню вертикальной направленности...
                        if ("up down".contains(this.MenuDirection.toLowerCase())) {
                            // Считаем высоту меню по высоте группового чекбокса
                            menu_height += GroupCheckbox.getHeight();

                            // А так же проверяем, что групповой чекбокс не последний элемент
                            if (i != (this.GameControls.size() - 1)) {
                                // Добавляем дистанцию к высоте меню
                                menu_height += this.distance;
                            }
                        }

                        // Если меню горизонтальной направленности,
                        // берём высоту группового чекбокса как максимальную
                        if ("left right".contains(this.MenuDirection.toLowerCase())) {
                            if (menu_height < GroupCheckbox.getHeight()){
                                menu_height = GroupCheckbox.getHeight();
                            }
                        }

                        break;
                }
            }
        }

        return menu_height;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }


    // Рисование меню на экране игры
    // ВНИМАНИЕ! Рекурсивная функция
    public void onDraw(Canvas canvas) {
        try {
            // Если есть контролы, проверяем их тип и рисуем
            if (this.GameControls != null) {
                for (GameControl control : this.GameControls) {
                    switch (control.getClass().getSimpleName()) {
                        case "GameMenu":
                            // Создаём объект меню
                            GameMenu Menu = (GameMenu) control;

                            // Рисуем
                            Menu.onDraw(canvas);

                            break;

                        case "GameButton":
                            // Создаём объект, чтобы нарисовать кнопку
                            GameButton Button = (GameButton) control;

                            // Рисуем
                            Button.onDraw(canvas);

                            break;

                        case "GameLabel":
                            // Создаём объект, чтобы нарисовать лейбл
                            GameLabel Label = (GameLabel) control;

                            // Рисуем
                            Label.onDraw(canvas);

                            break;

                        case "GameCheckbox":
                            // Создаём объект, чтобы нарисовать чекбокс
                            GameCheckbox Checkbox = (GameCheckbox) control;

                            // Рисуем
                            Checkbox.onDraw(canvas);

                            break;

                        case "GameGroupCheckbox":
                            // Создаём объект, чтобы нарисовать чекбокс
                            GameGroupCheckbox GroupCheckbox = (GameGroupCheckbox) control;

                            // Рисуем
                            GroupCheckbox.onDraw(canvas);

                            break;
                    }
                }
            }
        }
        catch (NullPointerException e) {
            Log.e(TAG, "Menu draw error");
            e.printStackTrace();
        }
    }


    // Пересчитывает координаты всех элементов меню по дистанции между элементами,
    // центрированию и выравниванию элементов относительно друг друга.
    // Пересчёт ведётся относительно координат самого меню
    // TODO Метод слишком сложный, нужно разбить на простые
    public void relocate(int x, int y) {
        super.relocate(x, y);

        // Сохраним контролы в список неотредактированных. Старый список обнулим.
        List<GameControl> uneditedControls = this.GameControls;
        this.GameControls = null;
        this.GameControls = new ArrayList<>();

        /*  Добавление элементов UI в меню
         *  За основу размерности элемента берём его зону активности.
         */

        // Нужно определить элемент с максимальной шириной или высотой.
        int max_width = 0;
        int max_height = 0;
        for (GameControl control : uneditedControls) {
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

                case "GameLabel":
                    GameLabel tmpLabel = (GameLabel) control;

                    control_width = tmpLabel.getActivity_w();
                    control_height = tmpLabel.getActivity_h();

                    break;

                case "GameCheckbox":
                    GameCheckbox tmpCheckbox = (GameCheckbox) control;

                    control_width = tmpCheckbox.getWidth();
                    control_height = tmpCheckbox.getHeight();

                    break;

                case "GameGroupCheckbox":
                    // Создаём объект, чтобы нарисовать чекбокс
                    GameGroupCheckbox tmpGroupCheckbox = (GameGroupCheckbox) control;

                    control_width = tmpGroupCheckbox.getWidth();
                    control_height = tmpGroupCheckbox.getHeight();

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
        GameControl firstControl = uneditedControls.get(0);

        int control_canv_x;
        int control_canv_y;
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

            case "GameLabel":
                GameLabel tmpLabel = (GameLabel) firstControl;

                control_width = tmpLabel.getActivity_w();
                control_height = tmpLabel.getActivity_h();

                break;

            case "GameCheckbox":
                GameCheckbox tmpCheckbox = (GameCheckbox) firstControl;

                control_width = tmpCheckbox.getWidth();
                control_height = tmpCheckbox.getHeight();

                break;

            case "GameGroupCheckbox":
                GameGroupCheckbox tmpGroupCheckbox = (GameGroupCheckbox) firstControl;

                control_width = tmpGroupCheckbox.getWidth();
                control_height = tmpGroupCheckbox.getHeight();

                break;
        }

        // Перемещаем контрол от начала координат меню
        control_canv_x = this.getX();
        control_canv_y = this.getY();

        // Если меню направлено влево, кнопку смещаем влево
        if ((this.MenuDirection.toLowerCase().equals("left"))) {
            control_canv_x -= control_width;
        }

        // Если меню направлено вверх, кнопку смещаем вверх
        if ((this.MenuDirection.toLowerCase().equals("up"))) {
            control_canv_y -= control_height;
        }

        // Если меню горизонтальной направленности...
        if ("left right".contains(this.MenuDirection.toLowerCase())) {
            // Выравниваем кнопку по высоте от максимальной высоты элемента
            if (this.Align.toLowerCase().equals("center")) {
                Log.w(TAG, "NAH!");
                control_canv_y += (max_height - control_height) / 2;
            }

            if (this.Align.toLowerCase().equals("bottom")) {
                Log.w(TAG, "NAH!");
                control_canv_y += max_height - control_height;
            }
        }

        // Если меню вертикальной направленности...
        if ("up down".contains(this.MenuDirection.toLowerCase())) {
            // Выравниваем кнопку по ширине от максимальной ширины элемента
            if (this.Align.toLowerCase().equals("center")) {
                control_canv_x += (max_width - control_width) / 2;
            }

            if (this.Align.toLowerCase().equals("right")) {
                control_canv_x += max_width - control_width;
            }
        }

        // Назначение новых координат первого контрола
        firstControl.relocate(control_canv_x, control_canv_y);

        // Добавляем контрол в список
        this.GameControls.add(firstControl);

        // Сохраняем первый контрол, как "предыдущий" и запускаем цикл выравнивания
        GameControl prevControl = firstControl;
        for (GameControl nextControl : uneditedControls) {
            // Первый контрол уже добавили, пропускаем
            if (nextControl.equals(this.GameControls.get(0))) { continue; }

            int next_control_canv_x = 0;
            int next_control_canv_y = 0;
            int next_control_width  = 0;
            int next_control_height = 0;
            int prev_control_width  = 0;
            int prev_control_height = 0;

            // Высчитываем размеры предыдущего контрола в зависимости от его типа
            switch(prevControl.getClass().getSimpleName()) {
                case "GameMenu":
                    GameMenu tmpMenu = (GameMenu) prevControl;

                    prev_control_width = tmpMenu.getMenuWidth();
                    prev_control_height = tmpMenu.getMenuHeight();

                    break;

                case "GameButton":
                    prev_control_width = prevControl.getActivity_w();
                    prev_control_height = prevControl.getActivity_h();

                    break;

                case "GameLabel":
                    GameLabel tmpLabel = (GameLabel) prevControl;

                    prev_control_width = tmpLabel.getActivity_w();
                    prev_control_height = tmpLabel.getActivity_h();

                    break;

                case "GameCheckbox":
                    GameCheckbox tmpCheckbox = (GameCheckbox) prevControl;

                    prev_control_width = tmpCheckbox.getWidth();
                    prev_control_height = tmpCheckbox.getHeight();

                    break;

                case "GameGroupCheckbox":
                    GameGroupCheckbox tmpGroupCheckbox = (GameGroupCheckbox) prevControl;

                    prev_control_width = tmpGroupCheckbox.getWidth();
                    prev_control_height = tmpGroupCheckbox.getHeight();

                    break;
            }

            // Высчитываем размеры следующего контрола в зависимости от его типа
            switch(nextControl.getClass().getSimpleName()) {
                case "GameMenu":
                    GameMenu tmpMenu = (GameMenu) nextControl;

                    next_control_width = tmpMenu.getMenuWidth();
                    next_control_height = tmpMenu.getMenuHeight();

                    break;

                case "GameButton":
                    next_control_width = nextControl.getActivity_w();
                    next_control_height = nextControl.getActivity_h();

                    break;

                case "GameLabel":
                    GameLabel tmpLabel = (GameLabel) nextControl;

                    next_control_width = tmpLabel.getActivity_w();
                    next_control_height = tmpLabel.getActivity_h();

                    break;

                case "GameCheckbox":
                    GameCheckbox tmpCheckbox = (GameCheckbox) nextControl;

                    next_control_width = tmpCheckbox.getWidth();
                    next_control_height = tmpCheckbox.getHeight();

                    break;

                case "GameGroupCheckbox":
                    GameGroupCheckbox tmpGroupCheckbox = (GameGroupCheckbox) nextControl;

                    next_control_width = tmpGroupCheckbox.getWidth();
                    next_control_height = tmpGroupCheckbox.getHeight();

                    break;
            }


            // По аналогии с первой кнопкой рисуем остальные по направленю
            if ((this.MenuDirection.toLowerCase().equals("left"))) {
                next_control_canv_x = prevControl.getX() - this.distance - next_control_width;
                next_control_canv_y = this.getY();
            }

            if ((this.MenuDirection.toLowerCase().equals("up"))) {
                next_control_canv_x = this.getX();
                next_control_canv_y = prevControl.getY() - this.distance - next_control_height;
            }

            if ((this.MenuDirection.toLowerCase().equals("right"))) {
                next_control_canv_x = prevControl.getX() + this.distance + prev_control_width;
                next_control_canv_y = this.getY();
            }

            if ((this.MenuDirection.toLowerCase().equals("down"))) {
                next_control_canv_x = this.getX();
                next_control_canv_y = prevControl.getY() + this.distance + prev_control_height;
            }

            // Выравниваем элементы меню горизонтальной направленности
            if ("left right".contains(this.MenuDirection.toLowerCase())) {
                if (this.Align.toLowerCase().equals("center")) {
                    next_control_canv_y += (max_height - next_control_height)/2;
                }

                if (this.Align.toLowerCase().equals("bottom")) {
                    next_control_canv_y += max_height - next_control_height;
                }
            }

            // Выравниваем элементы меню вертикальной направленности
            if ("up down".contains(this.MenuDirection.toLowerCase())) {
                if (this.Align.toLowerCase().equals("center")) {
                    next_control_canv_x += (max_width - next_control_width)/2;
                }

                if (this.Align.toLowerCase().equals("right")) {
                    next_control_canv_x += max_width - next_control_width;
                }
            }

            nextControl.relocate(next_control_canv_x, next_control_canv_y);

            // Добавляем кнопку в меню
            this.GameControls.add(nextControl);

            // Переприсваиваем обработанную кнопку
            prevControl = nextControl;
        }

        // Если центрирование задано, центрируем все элементы меню
        if (this.Centering.toLowerCase().length() > 0) {
            for (int i = 0; i < this.GameControls.size(); i++) {
                // Высчитываем размеры контрола в зависимости от типа:
                switch (this.GameControls.get(i).getClass().getSimpleName()) {
                    case "GameMenu":
                        GameMenu tmpMenu = (GameMenu) this.GameControls.get(i);

                        control_width = tmpMenu.getMenuWidth();
                        control_height = tmpMenu.getMenuHeight();

                        break;

                    case "GameButton":
                        control_width = this.GameControls.get(i).getActivity_w();
                        control_height = this.GameControls.get(i).getActivity_h();

                        break;

                    case "GameLabel":
                        GameLabel tmpLabel = (GameLabel) this.GameControls.get(i);

                        control_width = tmpLabel.getActivity_w();
                        control_height = tmpLabel.getActivity_h();

                        break;

                    case "GameCheckbox":
                        GameCheckbox tmpCheckbox = (GameCheckbox) this.GameControls.get(i);

                        control_width = tmpCheckbox.getWidth();
                        control_height = tmpCheckbox.getHeight();

                        break;

                    case "GameGroupCheckbox":
                        GameGroupCheckbox tmpGroupCheckbox = (GameGroupCheckbox) this.GameControls.get(i);

                        control_width = tmpGroupCheckbox.getWidth();
                        control_height = tmpGroupCheckbox.getHeight();

                        break;
                }

                control_canv_x = this.GameControls.get(i).getX();
                control_canv_y = this.GameControls.get(i).getY();

                // Если меню горизонтальной направленности
                if ("left right".contains(this.MenuDirection.toLowerCase())) {
                    // Если центрирование горизонтальное
                    if ("horizontal all".contains(this.Centering.toLowerCase())) {
                        Log.w(TAG, "ME IS " + this.GameControls.get(i).getClass().getSimpleName());
                        Log.w(TAG, "I HAVE Y POINT: " + this.GameControls.get(i).getY());
                        Log.w(TAG, "I HAVE Y MOVE: " + (control_height / 2));
                        Log.w(TAG, "I HAVE H CENERING Yn POINT: " + (this.getY() - control_height / 2));

                        // Просто перестраиваем все элементы по центру их высоты
                        this.GameControls.get(i).setY(this.getY() - control_height / 2);

                        Log.w(TAG, "I HAVE new Yn POINT: " + this.GameControls.get(i).getY());
                        Log.w(TAG, "Centering MENU Y POINT: " + this.getY());

                    }

                    // Если центрирование вертикальное
                    if ("vertical all".contains(this.Centering.toLowerCase())) {
                        Log.w(TAG, "SHtow?");
                        // Перестраиваем все элементы на половину ширины всего меню,
                        // не сбивая выравнивание элементов относительно друг друга
                        this.GameControls.get(i).setX(control_canv_x - this.getMenuWidth() / 2);
                    }
                }


                // Если меню вертикальной направленности
                if ("up down".contains(this.MenuDirection.toLowerCase())) {
                    // Если центрирование вертикальное
                    if ("vertical all".contains(this.Centering.toLowerCase())) {
                        // Просто перестраиваем все элементы по центру их ширины
                        this.GameControls.get(i).setX(this.getX() - control_width / 2);
                    }

                    // Если центрирование горизонтальное
                    if ("horizontal all".contains(this.Centering.toLowerCase())) {
                        // Перестраиваем все элементы на половину высоты всего меню,
                        // не сбивая выравнивание элементов относительно друг друга
                        this.GameControls.get(i).setY(control_canv_y - this.getMenuHeight() / 2);
                    }
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
