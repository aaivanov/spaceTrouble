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

    private static final String TAG = GameButton.class.getSimpleName();

    // Конструктор по-умолчанию
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
            String Reaction
    ) {
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = Name;

        this.reaction     = Reaction;
    }


    // Конструктор с ресурсом кнопки без отступов
    public GameButton(
            Bitmap bitmapSource,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            String Name,
            String Reaction
    ) {
        super(canv_x, canv_y, activity_w, activity_h);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = Name;

        this.reaction     = Reaction;
    }


    // Упрощённый Конструктор с ресурсом кнопки
    public GameButton(
            Bitmap bitmapSource,
            int canv_x,
            int canv_y,
            String Reaction
            ) {
        super(canv_x, canv_y, bitmapSource.getWidth(), bitmapSource.getHeight(), 0, 0, 0 ,0);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = button_id + "_Button";

        this.reaction     = Reaction;
    }


    // Конструктор с рисованием кнопки
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
            String Reaction
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

        this.button_id = controlBitmap.getGenerationId();

        this.buttonName = button_id + "_Button";

        this.reaction   = Reaction;
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
            String Reaction
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
}
