package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.divanstudio.firsttry.R;

/**
 * Created by WJ_DDA on 05.10.2016.
 */

public class GameButton extends GameControl {
    private Bitmap sourceBitmap;      // Картинка контрола
    private String buttonText = "";    // Текст на кнопке
    private String buttonName = "";    // Имя кнопки
    private int button_id;             // Уникальный ключ контрола

    private static final String TAG = GameButton.class.getSimpleName();

    // Конструктор по-умолчанию
    // TODO Как избавится от gamePanel?
    public GameButton(MainGamePanel gamePanel) {
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
    }


    // Конструктор с ресурсом кнопки
    public GameButton(
            Bitmap bitmapSource,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            String Name
    ) {
        super(canv_x, canv_y, activity_w, activity_h);

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = Name;
    }


    // Упрощённый Конструктор с ресурсом кнопки
    public GameButton(Bitmap bitmapSource, int canv_x, int canv_y) {
        super(canv_x, canv_y, bitmapSource.getWidth(), bitmapSource.getHeight());

        this.sourceBitmap = bitmapSource;
        this.button_id    = bitmapSource.getGenerationId();
        this.buttonName   = button_id + "_Button";
    }


    // Конструктор с рисованием кнопки
    // TODO эту функцию надо расширять и дорабатывать
    public GameButton(
            int canv_x,
            int canv_y,
            int width,
            int height,
            String Text
    )
    {
        super(canv_x, canv_y, width, height);

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
    }


    // Обновить кнопку
    public void updateGameButton(
            MainGamePanel newGamePanel,
            int id_resource,
            int canv_new_x,
            int canv_new_y,
            int new_activity_w,
            int new_activity_h,
            String newName
    ) {
        /*
         *
         */
        super.updateGameControl(
                newGamePanel,
                canv_new_x,
                canv_new_y,
                new_activity_w,
                new_activity_h
        );

        // Задаём новый ресурс битмапа
        this.sourceBitmap = BitmapFactory.decodeResource(
                newGamePanel.getResources(), id_resource
        );

        this.button_id = sourceBitmap.getGenerationId();
        this.buttonName = newName;
    }

    /*
    * Извлечение данных кнопки
    */
    public String getName() { return this.buttonName; }


    public int getId() { return this.button_id; }


    /*
     * Установка данных кнопки
     */
    public void setName(String newName) { this.buttonName = newName; }


    public void setBitmap(Bitmap source) {
        this.sourceBitmap = source;

        // Апдейт id
        this.button_id = sourceBitmap.getGenerationId();
    }

    /*
     * Обработчики взаимодействия с кнопками
     */
    // TODO Коснулся ли палец контрола. Дописать
    public boolean isCollision() {
        return true;
    }


    /*
     * Рисование кнопок на экране игры
     */
    //TODO Тут кроется загадка нерисования контролов
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

        Log.d(TAG, sourceBitmap.getGenerationId() + ": " + this.getX() + " " + this.getY() + " " + sourceBitmap.getWidth() + " " + sourceBitmap.getHeight());

        if (sourceBitmap !=  null) {
            canvas.drawBitmap(sourceBitmap, src, dst, null);
        }
    }

}
