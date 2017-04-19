package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by WJ_DDA on 16.03.2017.
 */

public class GameLabel extends GameControl {
    private String LabelText     = "";            // Текст
    private String Name          = "";            // Имя контрола

    private int text_color       = 0x00000000;    // Цвет текста (ARGB)
    private int background_color = 0x00000000;    // Цвет фона за текстом (ARGB)
    private int font_size        = 24;            // размер шрифта текста

    private Paint TextBrush = null;               // Кисть текста
    private Paint BackgroundBrush = null;         // Кисть фона

    private Typeface LabelFont   = null;          // Шрифт и сопутствующие настройки шрифта
                                                  // Можно задать кастомный шрифт в ресурсах

    private static final String TAG = GameLabel.class.getSimpleName();


    // Конструктор по-умолчанию
    public GameLabel() {
        super(0, 0, 0, 0, 0, 0, 0, 0);

        this.Name             = "Empty_Label";
        this.text_color       = 0;
        this.background_color = 0;
        this.font_size        = 0;

        initDrawLabel();
    }


    // Конструктор
    public GameLabel(
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            String Name,
            String Text,
            int text_color,
            int background_color,
            Typeface customFont,
            int font_size

    ) {
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.LabelText        = Text;
        this.text_color       = text_color;

        this.background_color = background_color;

        this.LabelFont = customFont;
        this.font_size = font_size;
        this.Name      = Name;

        initDrawLabel();

        this.setActivityZone(this.getTextWidth(), this.getTextHeight());
    }


    // Конструктор без зоны активности и отступов
    public GameLabel(
            int canv_x,
            int canv_y,
            String Name,
            String Text,
            int text_color,
            int background_color,
            Typeface customFont,
            int font_size

    ) {
        super(canv_x, canv_y, 0, 0, 0, 0, 0, 0);

        this.LabelText        = Text;
        this.text_color       = text_color;

        this.background_color = background_color;

        this.LabelFont = customFont;
        this.font_size = font_size;
        this.Name      = Name;

        initDrawLabel();

        this.setActivityZone(this.getTextWidth(), this.getTextHeight());
    }


    // Упрощённый конструктор
    public GameLabel(
            String Name,
            String Text,
            int text_color,
            int background_color,
            Typeface cFont,
            int font_size
    ) {
        super(0, 0, 0, 0, 0, 0, 0, 0);

        this.LabelText        = Text;
        this.Name             = Name;
        this.text_color       = text_color;
        this.background_color = background_color;
        this.LabelFont        = cFont;
        this.font_size        = font_size;

        initDrawLabel();

        this.setActivityZone(this.getTextWidth(), this.getTextHeight());
    }


    public String getName() { return this.Name; }


    public String getText() { return this.LabelText; }


    public int getTextColor() { return this.text_color; }


    public int getBackgroundColor() { return this.background_color; }


    public Typeface getFont() { return this.LabelFont; }


    public int getTextSize() { return (int) this.TextBrush.getTextSize(); }


    public int getTextWidth() {
        Rect bounds = new Rect();

        this.TextBrush.getTextBounds(this.LabelText, 0, this.LabelText.length(), bounds);

        return bounds.width();
    }


    public int getTextHeight() {
        Rect bounds = new Rect();
        this.TextBrush.getTextBounds(this.LabelText, 0, this.LabelText.length(), bounds);

        return bounds.height();
    }


    public Rect getTextBounds() {
        Rect bounds = new Rect();

        this.TextBrush.getTextBounds(this.LabelText, 0, this.LabelText.length(), new Rect());

        return bounds;
    }


    public void setName(String newName) { this.Name = newName; }


    public void setText(String newText) { this.LabelText = newText; }


    public void setTextColor(int new_color) { this.text_color = new_color; }


    public void setBackgroundColor(int new_color) { this.background_color = new_color; }


    public void setLabelFont(Typeface newFont) { this.LabelFont = newFont; }


    // TODO вместо шрифта задать отдельно атрибуты
    // public void setLabelFontAttr(...) { this.LabelFont = newFont; ...}


    private void initDrawLabel () {
        // Раскраска лейбла
        this.TextBrush = new Paint();    // Кисть раскраски текста

        // Назначаем на кисть шрифт
        this.TextBrush.setTypeface(this.LabelFont);
        this.TextBrush.setTextSize(this.font_size);

        // Настраиваем параметры кисти: цвет, ширина линии, стиль
        this.TextBrush.setColor(this.text_color);
        this.TextBrush.setStrokeWidth(0);
        this.TextBrush.setStyle(Paint.Style.FILL);

        if (this.background_color != 0) {
            this.BackgroundBrush = new Paint();    // Кисть раскраски фона

            this.BackgroundBrush.setColor(this.background_color);
            this.BackgroundBrush.setStrokeWidth(0);
            this.BackgroundBrush.setStyle(Paint.Style.FILL);
        }
    }


    public void onDraw(Canvas canvas) {
        if (this.background_color != 0) {
            try {
                // Рисуем фон на холсте с учётом всех настроек
                canvas.drawRect(
                        this.getX(),
                        this.getY(),
                        this.getX() + this.getActivity_w(),
                        this.getY() + this.getActivity_h(),
                        this.BackgroundBrush
                );
            }
            catch (NullPointerException e) {
                Log.w(TAG, "Background brush unset");
            }
        }


        try {
            // Рисуем текст на холсте, с учётом всех настроек, поверх фона
            // За линию рисования текста берём высоту зоны активности
            if ((this.LabelText != null) && !this.LabelText.equals("") && this.text_color != 0) {
                canvas.drawText(
                        this.LabelText,
                        this.getX(),
                        this.getY() + this.getActivity_h(),
                        this.TextBrush
                );
            }
        }
        catch (NullPointerException e) {
            Log.w(TAG, "Text brush unset");
        }
    }


    public void VCentering() {
        this.setX(this.getX() - this.getActivity_w() / 2);
    }


    public void HCentering() {
        this.setY(this.getY() - this.getActivity_h() / 2);
    }
}
