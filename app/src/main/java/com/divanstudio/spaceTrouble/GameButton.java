package com.divanstudio.spaceTrouble;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import android.view.MotionEvent;

import com.example.divanstudio.firsttry.R;

import static android.app.PendingIntent.getActivity;

/**
 * Created by WJ_DDA on 05.10.2016.
 */

public class GameButton extends GameControl {
    private Bitmap sourceBitmap = null;      // Картинка контрола

    private String buttonText = "";          // Текст на кнопке
    private String buttonName = "";          // Имя кнопки
    private int button_id;                   // Уникальный ключ контрола

    // TODO возможно - новый класс или константы реакций кнопок
    private String reaction = "";            // Реакция кнопки. Её задача при нажатии.

    // Объекты на которые назначаются реакции кнопок
    private FullscreenActivity mainActivity;
    private State mainState;
    private Enemies mainEnemies;

    private static final String TAG = GameButton.class.getSimpleName();

    // Конструктор по-умолчанию
    // TODO Как избавится от gamePanel?
    public GameButton(mainView gamePanel) {
        /*
         * По умолчанию будем создавать кнопку выхода из игры )
         */
        super();

        // Берём картинку из ресурсов
        this.sourceBitmap = BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.tmp_menu_exit
        );

        // В ресурсах генерируется id ресурса, его используем как id контрола
        this.button_id = sourceBitmap.getGenerationId();

        // Id и тип класса дадут уникальное имя кнопки
        this.buttonName = button_id + "_Button";

        this.reaction   = "Exit";
    }


    // Конструктор с ресурсом кнопки
    public GameButton(
            Bitmap bitmapSource,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            String Name,
            String Reaction,
            Context gameActivity,
            State gameState,
            Enemies gameObjects
    ) {
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = Name;

        this.reaction     = Reaction;

        this.mainActivity = (FullscreenActivity) gameActivity;
        this.mainState    = gameState;
        this.mainEnemies  = gameObjects;
    }


    // Конструктор с ресурсом кнопки без отступов
    public GameButton(
            Bitmap bitmapSource,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            String Name,
            String Reaction,
            Context gameActivity,
            State gameState,
            Enemies gameObjects
    ) {
        super(canv_x, canv_y, activity_w, activity_h);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = Name;

        this.reaction     = Reaction;

        this.mainActivity = (FullscreenActivity) gameActivity;
        this.mainState    = gameState;
        this.mainEnemies  = gameObjects;
    }


    // Упрощённый Конструктор с ресурсом кнопки
    public GameButton(
            Bitmap bitmapSource,
            int canv_x,
            int canv_y,
            String Reaction,
            FullscreenActivity gameActivity,
            Enemies gameObjects
            ) {
        super(canv_x, canv_y, bitmapSource.getWidth(), bitmapSource.getHeight(), 0, 0, 0 ,0);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = button_id + "_Button";

        this.reaction     = Reaction;

        this.mainActivity = gameActivity;
        this.mainState    = State.getInstance();
        this.mainEnemies  = gameObjects;
    }


    // Конструктор с рисованием кнопки
    // TODO эту функцию надо расширять и дорабатывать
    public GameButton(
            int canv_x,
            int canv_y,
            int width,
            int height,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            String Text,
            String Reaction,
            FullscreenActivity gameActivity,
            State gameState,
            Enemies gameObjects
    )
    {
        super(canv_x, canv_y, width, height, indent_l, indent_t, indent_r, indent_b);

        // Кисть раскраски текста кнопки
        Paint textBrushStyle = new Paint();

        // Настраиваем параметры кисти: цвет, ширина линии, стиль
        textBrushStyle.setColor(Color.BLACK);
        textBrushStyle.setStrokeWidth(0);
        textBrushStyle.setStyle(Paint.Style.STROKE);

        // Задаём раскраску контрола
        Paint buttonBrushStyle = new Paint();

        // Настраиваем параметры кисти
        buttonBrushStyle.setColor(Color.WHITE);
        buttonBrushStyle.setStrokeWidth(0);
        buttonBrushStyle.setStyle(Paint.Style.STROKE);

        // Кнопку рисуем в виде прямоугольника
        // TODO RectF?
        Rect controlRect = new Rect(canv_x, canv_y, canv_x + width, canv_y + height);

        // Генерируем картинку контрола
        Bitmap controlBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        // Сгенерированную картинку делаем холстом
        Canvas drawBitmap = new Canvas(controlBitmap);

        // Рисуем кнопку на холсте с учётом всех настроек
        // Если я правильно понял, наш битмап будет изменён
        drawBitmap.drawRect(canv_x, canv_y, canv_x + width, canv_y + height, buttonBrushStyle);

        // Если поле buttonText не пусто, то пишем текст в кнопку
        this.buttonText = Text;

        if ((buttonText != null) && !buttonText.equals("")) {
            drawBitmap.drawText(
                    buttonText,
                    canv_x + width / 2,
                    canv_y + height / 2,
                    textBrushStyle
            );
        }

        // Конструируем контрол
        this.sourceBitmap = controlBitmap;

        // TODO не зенаю какой ID. Возможно он генерируется (на то похоже из Гуглирования)
        this.button_id = controlBitmap.getGenerationId();

        this.buttonName = button_id + "_Button";

        this.reaction   = Reaction;

        this.mainActivity = gameActivity;
        this.mainState    = gameState;
        this.mainEnemies  = gameObjects;
    }


    // Обновить кнопку
    public void updateGameButton(
            mainView newGamePanel,
            int id_resource,
            int canv_new_x,
            int canv_new_y,
            int new_activity_w,
            int new_activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            String newName,
            String Reaction,
            FullscreenActivity newActivity,
            State newState,
            Enemies newObjects

    ) {
        /*
         *
         */
        super.updateGameControl(
                newGamePanel,
                canv_new_x,
                canv_new_y,
                new_activity_w,
                new_activity_h,
                indent_l,
                indent_t,
                indent_r,
                indent_b
        );

        // Задаём новый ресурс битмапа
        this.sourceBitmap = BitmapFactory.decodeResource(
                newGamePanel.getResources(), id_resource
        );

        this.button_id = sourceBitmap.getGenerationId();
        this.buttonName = newName;

        this.reaction   = Reaction;

        this.mainActivity = newActivity;
        this.mainState    = newState;
        this.mainEnemies  = newObjects;
    }

    /*
    * Извлечение данных кнопки
    */
    public String getName() { return this.buttonName; }


    public int getId() { return this.button_id; }


    public String getReaction() { return this.reaction; }


    public Bitmap getBitmap() { return this.sourceBitmap; }


    /*
     * Установка данных кнопки
     */
    public void setName(String newName) { this.buttonName = newName; }


    public void setBitmap(Bitmap source) {
        this.sourceBitmap = source;

        // Апдейт id
        this.button_id = sourceBitmap.getGenerationId();
    }


    public void setInactive() { this.unsetActivityZone(); }


    // Реакция на нажатие кнопки
    public void setClickReaction(String Reaction) {
        this.reaction = Reaction;
    }


    /*
     * Рисование кнопок на экране игры
     */
    public void onDraw(Canvas canvas) {
        // Какую часть битмапа берём из ресурса
        // Задаётся 2-мя точками прямоугольника
        Rect src = new Rect(
                0,
                0,
                sourceBitmap.getWidth(),
                sourceBitmap.getHeight()
        );

        // Где рисуем битмап
        // Нужно задать координаты 2-х точек для прямоугольника
        // Точки берём из super при помощи методов класса GameControl
        Rect dst = new Rect(
                this.getX(),
                this.getY(),
                this.getX() + this.getActivity_w(),
                this.getY() + this.getActivity_h()
        );

//        Log.d(TAG, "Button '" + this.getName() + "' " + sourceBitmap.getGenerationId() + ": " + this.getX() + " " + this.getY() + " " + sourceBitmap.getWidth() + " " + sourceBitmap.getHeight());

        if (sourceBitmap !=  null) {
            canvas.drawBitmap(sourceBitmap, src, dst, null);
        }
    }


    /*
     * Обработчик событий мышки (тачпада?)
     */
    // Обработчик кнопки. Меняет состояние в зависимости от попадания в зону активности контрола
    public boolean isPressed (MotionEvent event) {
        Log.d(TAG, String.format("Button %s isCollision - %s", this.getName(), this.isCollision(event.getX(), event.getY())));
        return this.isCollision(event.getX(), event.getY());
    }


    // Обработчик нажатия кнопки при разных состояниях события тачпада.
    // TODO Кнопки управления имеют реакцию только при ACTION DOWN и ACTION_MOVE надо сделать
    public void onTouch(MotionEvent event, String touchTypes) {
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:         // нажатие на тачпад - нажатие на кнопку
                if (touchTypes.contains("DOWN")) {
                    if (this.isPressed(event)) {
                        // TODO Анимация нажатой кнопки без реакции
                        Log.i(TAG, "Button " + this.getName() + " touch DOWN");

                        onClick(this.mainActivity, this.mainState, this.mainEnemies);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:         // движение по тачпаду - тоже считается нажатием
                if (touchTypes.contains("MOVE")) {
                    if (this.isPressed(event)) {
                        // TODO Анимация нажатой кнопки остаётся, реакция не следует
                        Log.i(TAG, "Button " + this.getName() + " touch MOVE");

                        onClick(this.mainActivity, this.mainState, this.mainEnemies);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:           // отжатие от тачпада - кнопка не нажата
                if (touchTypes.contains("UP")) {
                    if (this.isPressed(event)) {
                        // TODO Анимация отжатой кнопки
                        // TODO Реакция кнопки
                        Log.i(TAG, "Button " + this.getName() + " touch UP");
                        Log.i(TAG, "Button " + this.getName() + " pressed and do " + this.getReaction());

                        onClick(this.mainActivity, this.mainState, this.mainEnemies);
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:       // TODO Пока хз как обработать
                if (touchTypes.contains("CANCEL")) {
                    if (this.isPressed(event)) {
                        //TODO Что-то
                        Log.i(TAG, "Button " + this.getName() + " touch CANCEL");

                        onClick(this.mainActivity, this.mainState, this.mainEnemies);
                    }
                }
                break;
        }
    }


    // TODO Куда тебя поместить то? Как тебя сделать круче и по java канону?
    // Нужно определять этот метод только для созданной кнопки. И в нём конкретные действя

    // Список реакций кнопок
    public void onClick(FullscreenActivity gameActivity, State gameState, Enemies gameObjects) {
        switch (this.reaction) {
            // Activity manager
            case "Exit":
                gameState.setState("Exit");

                // TODO Ошибка приложения.
                // Вероятно во время завершения активности надо ещё что-то делать.
                // Например, останавливать Главный Тред.
                // Ещё иногда тормоза в игре не сразу включают эту реакцию кнопки
                // Пример ошибки:
                // E/AndroidRuntime: FATAL EXCEPTION: Thread-230
                // java.lang.NullPointerException
                // at com.divanstudio.spaceTrouble.Background.onDraw(Background.java:21)
                // at com.divanstudio.spaceTrouble.mainView.onDraw(mainView.java:83)
                // at com.divanstudio.spaceTrouble.mainManager.run(mainManager.java:65)
                gameActivity.finish();
                break;

            // Game Manager
            case "Menu":
                gameState.setState("Menu");
                break;
            case "Play":
            case "Start":
                gameState.setState("Play");
                break;
            case "Pause":
                //<gameActivity.onStop();>
                //gameState.setState("Pause");
                break;
            case "Resume":
                //<gameActivity.onResume();>
                //gameState.setState("Play");
                break;

            // Game Objects Manager
            // TODO CHECKPOINT Затык. Метеориты двигаются и не останавливаются. Как исправить?
            // TODO Как то нужно сделать грамотные названия кнопок из кусков битмапа
            case "MoveShipUp":
            case "Player_Move_0":
                gameObjects.moveDown();
                break;
            case "MoveShipDown":
            case "Player_Move_1":
                gameObjects.moveUp();
                break;

            // Game Options Manager
            case "SoundOff":
                //gameOptions["Sound"].set("Off");
                break;
            case "SoundOn":
                //gameOptions["Sound"].set("On");
                break;
            case "MusicOff":
                //gameOptions["Music"].set("Off");
                break;
            case "MusicOn":
                //gameOptions["Music"].set("On");
                break;

        }
    }
}
