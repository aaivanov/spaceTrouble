package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.divanstudio.firsttry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/1/15.
 */
public class Controls {

    private List<Control> Controls = new ArrayList<Control>();
    private Control menu;
    private GameMenu mainMenu;
    private GameMenu gameMenu;
    private GameMenu playerControls;
    private Enemies meteors;

    private State state;

    private Player player;

    private static final String TAG = Controls.class.getSimpleName();

    public Controls (mainView GameView, Bitmap bmp, Enemies meteors) {
        this.Controls.add(new Control(GameView, bmp, 0, 30, 50));  // Кнопка движения вверх
        this.Controls.add(new Control(GameView, bmp, 1, 30, 150)); // Кнопка движения вниз
        this.player = Player.getInstance();
        this.state = State.getInstance();
        this.menu = new Control(300, 50, 100, 100, "START");
        this.meteors = meteors;

        // Использование класса GameMenu
        // Создание Главного Меню Игры (ГМИ)
        Bitmap sourceExitButton = BitmapFactory.decodeResource(
                GameView.getResources(),
                R.drawable.tmp_menu_exit
        );
        Bitmap sourceStartButton = BitmapFactory.decodeResource(
                GameView.getResources(),
                R.drawable.tmp_menu_start
        );

        // Задаём главное меню игры
        // TODO А если не хочу задавать расположение кнопок от точки меню, то чё?
        int button_canv_x = 0;
        int button_canv_y = 0;
        GameButton startMainButton = new GameButton(
                sourceStartButton,
                button_canv_x,
                button_canv_y,
                sourceStartButton.getWidth(),
                sourceStartButton.getHeight(),
                "Start_Main_Menu_Button",
                "Start",
                GameView.getContext(),
                State.getInstance(),
                null
        );

        GameButton exitMainButton = new GameButton(
                sourceExitButton,
                button_canv_x,
                button_canv_y,
                sourceExitButton.getWidth(),
                sourceExitButton.getHeight(),
                "Exit_Main_Menu_Button",
                "Exit",
                GameView.getContext(),
                State.getInstance(),
                null
        );

        List<GameButton> mainMenuButtons = new ArrayList<>();
        mainMenuButtons.add(startMainButton);
        mainMenuButtons.add(exitMainButton);

        // Данные меню (ПОКА ДЛЯ ТЕСТОВ)
        // Рисуем главное меню в центре экрана вниз
        String menuDirection = "down";
        String menuTouchTypes = "UP";
        int button_distance = 20;
        int button_width = 0;
        int button_height = 0;

        if (startMainButton.getBitmap() != null) {
            button_width = startMainButton.getBitmap().getWidth();
            button_height = startMainButton.getBitmap().getHeight();
        } else {
            button_width = startMainButton.getActivity_w();
            button_height = startMainButton.getActivity_h();
        }

        int menu_canv_x = GameView.getWidth() / 2;
        int menu_canv_y = GameView.getHeight() / 2;
        int menu_activity_width = 0;
        int menu_activity_height = 0;
        int menu_indent_left = 0;
        int menu_indent_top = 0;
        int menu_indent_right = button_width / 2;
        int menu_indent_bottom = button_height / 2;

        this.mainMenu = new GameMenu(
                GameView,
                GameView.getContext(),
                mainMenuButtons,
                menuDirection,
                menuTouchTypes,
                button_distance,
                menu_canv_x,
                menu_canv_y,
                menu_activity_width,
                menu_activity_height,
                menu_indent_left,
                menu_indent_top,
                menu_indent_right,
                menu_indent_bottom
        );

        // Создание Игрового Меню (ИМ)
        Bitmap sourceMenuButton = BitmapFactory.decodeResource(
                GameView.getResources(),
                R.drawable.tmp_menu_menu
        );

        GameButton menuGameButton = new GameButton(
                sourceMenuButton,
                button_canv_x,
                button_canv_y,
                sourceMenuButton.getWidth(),
                sourceMenuButton.getHeight(),
                "Menu_Game_Button",
                "Menu",
                GameView.getContext(),
                State.getInstance(),
                null
        );

        List<GameButton> gameMenuButtons = new ArrayList<>();
        gameMenuButtons.add(menuGameButton);

        // Рисуем ИМ в правом верхнем углу экрана
        menuDirection = "left";
        menuTouchTypes = "UP";
        button_distance = 20;

        menu_canv_x = GameView.getWidth();
        menu_canv_y = 0;
        menu_activity_width = 0;
        menu_activity_height = 0;
        menu_indent_left = 0;
        menu_indent_top = 0;
        menu_indent_right = 10;
        menu_indent_bottom = 0;

        this.gameMenu = new GameMenu(
                GameView,
                GameView.getContext(),
                gameMenuButtons,
                menuDirection,
                menuTouchTypes,
                button_distance,
                menu_canv_x,
                menu_canv_y,
                menu_activity_width,
                menu_activity_height,
                menu_indent_left,
                menu_indent_top,
                menu_indent_right,
                menu_indent_bottom
        );

        // Создание Кнопок Управления (КУ)
        List<GameButton> playerControlButtons = new ArrayList<>();
        List<Bitmap> playerControlFrames = new ArrayList<>();
        Bitmap sourceArrows = BitmapFactory.decodeResource(
                GameView.getResources(),
                R.drawable.arrows
        );

        // Разбиваем битмап на кнопки управления
        // TODO не уверен, что так правильно
        int bmp_cols = 4;          // Вертикальное деление битмапа
        int parts_count = 2;       // Количество частей Битмапа
        int part_x = 0;            // Координата х точки начала разбиения Битмапа

        for (int i = 0; i < parts_count; i++){
            playerControlFrames.add(Bitmap.createBitmap(
                    sourceArrows,
                    part_x,
                    0,
                    sourceArrows.getWidth() / bmp_cols,
                    sourceArrows.getHeight()
            ));

            part_x += sourceArrows.getWidth() / bmp_cols;
        }

        // Создаём кнопки
        button_canv_x = 0;
        button_canv_y = 0;

        // Битмап такой, что кнопки лучше создать в обратном порядке
        for (int i = playerControlFrames.size() - 1; i >= 0; i--){
            GameButton controlButton = new GameButton(
                    playerControlFrames.get(i),
                    button_canv_x,
                    button_canv_y,
                    playerControlFrames.get(i).getWidth(),
                    playerControlFrames.get(i).getHeight(),
                    "Control_Arrow_" + playerControlFrames.indexOf(playerControlFrames.get(i)),
                    "Player_Move_" + playerControlFrames.indexOf(playerControlFrames.get(i)),
                    GameView.getContext(),
                    State.getInstance(),
                    this.meteors
            );

            playerControlButtons.add(controlButton);
        }

        // Создаём меню из кнопок управления
        // Кнопки располагаются внизу экрана
        menuDirection = "up";
        menuTouchTypes = "DOWN MOVE";
        button_distance = 10;

        menu_canv_x = 0;
        menu_canv_y = GameView.getHeight();
        menu_activity_width = 0;
        menu_activity_height = 0;
        menu_indent_left = 10;
        menu_indent_top = 0;
        menu_indent_right = 0;
        menu_indent_bottom = 30;
        this.playerControls = new GameMenu(
                GameView,
                GameView.getContext(),
                playerControlButtons,
                menuDirection,
                menuTouchTypes,
                button_distance,
                menu_canv_x,
                menu_canv_y,
                menu_activity_width,
                menu_activity_height,
                menu_indent_left,
                menu_indent_top,
                menu_indent_right,
                menu_indent_bottom
        );

        Log.d(TAG, "Player state for Controls: " + String.valueOf(this.player));
        Log.d(TAG, "Game state for Controls: " + String.valueOf(this.state));

    }

    public void onDraw(Canvas canvas) {
        if(state.getState() == "Play") {
        //if(!state.getState().equals("Menu")) {
            // TODO Добавить мои контролы, убрать этот цикл
//            for (Control control : Controls) {
//                control.onDraw(canvas);
//            }
            this.playerControls.onDraw(canvas);
            this.gameMenu.onDraw(canvas);
        }
        if (state.getState() == "Menu") {
        //if (state.getState().equals("Menu")) {
//            menu.onDraw(canvas);
            this.mainMenu.onDraw(canvas);
        }
    }

    // Обработка нажатий на контролы управления (на самом деле метод OnTouch для OnTouchListener)
    public void isCollision( MotionEvent event ) {
        // Использование класса GameMenu
        if (this.state.getState() == "Play") {
            this.gameMenu.menuHandler(event);
            this.playerControls.menuHandler(event);
        }

        if (this.state.getState() == "Menu") {
            this.mainMenu.menuHandler(event);
        }

//        switch ( event.getAction() ) {
//            case MotionEvent.ACTION_DOWN:             // нажатие
//                mouseEventHandler(event);
//                break;
//
//            case MotionEvent.ACTION_MOVE:             //движение
//                mouseEventHandler(event);
//                break;
//
//            case MotionEvent.ACTION_UP:               //отпускание
//                if(state.getState() == "Menu") {
////                    if(state.getState().equals("Menu")) {
//                    state.setState("Play");
//                } else {
//                    meteors.moveStop();
//                }
//                meteors.moveStop();
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                if(state.getState() == "Menu") {
////                // if(state.getState().equals("Menu")) {
//                    state.setState("Play");
//                } else {
//                    meteors.moveStop();
//                }
//                break;
//        }
    }


    // Обработчик нажатий на тачпад
    public void mouseEventHandler ( MotionEvent event) {
        // Пробегаем по массиву контролов
        // TODO Лучше использовать аналог "for control in controls" Python
        for (int i = Controls.size() - 1; i >= 0; i--) {
            // Извлекаем контрол по индексу массива
            Control control = Controls.get(i);

            // Проверяем коллизию с зоной активности контрола
            if (control.isCollision(event.getX(), event.getY())) {
                // Чтобы "двигать" весь экран игры, а ГГ оставлять на месте, нужно двигать
                // все видимые объекты кроме игрока. Есть мнение, что это не оптимально.

                // Если попали в кнопку движения вверх двигаем игровые объекты вниз
                if (i == 0){
                    meteors.moveDown();
                }
                // Если попали в кнопку движения вниз двигаем игровые объекты вверх
                if (i == 1) {
                    meteors.moveUp();
                }
            }
        }
    }
}
