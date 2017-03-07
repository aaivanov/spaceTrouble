package com.divanstudio.spaceTrouble;

import android.util.Log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by WJ_DDA on 29.09.2016.
 */

public class GameControl {
    private int draw_point_x;          // Координата Х точки отображения контрола
    private int draw_point_y;          // Координата Y точки отображения контрола

    private int activity_width;        // Ширина зоны активности
    private int activity_height;       // Высота зоны активности

    // TODO Для хэша как-то сделать неизменяемые ключи
    // TODO Не уверен, что поле нужно
    private Hashtable<String, Integer> Indents = new Hashtable<>(); // Отступы от границ экрана

    private static final String TAG = GameControl.class.getSimpleName();

    // Конструктор по-умолчанию
    public GameControl() {
        /*
         * По умолчанию создаём контрол в левом верхнем углу )
         * Зона активности контрола - это прямоугольник
         * Прямоугольник рисуется от точки (draw_point_x, draw_point_y)
         * до точки (draw_point_x + activity_width, draw_point_y + activity_height)
         */

        // Берём нулевую зону активности
        this.activity_width = 0;
        this.activity_height = 0;

        // По умолчанию контрол располагается в верхнем левом углу
        this.draw_point_x = 0;
        this.draw_point_y = 0;

        this.Indents.put("left", 0);
        this.Indents.put("top", 0);
        this.Indents.put("right", 0);
        this.Indents.put("bottom", 0);
    }


    // Конструктор
    public GameControl(
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_left,
            int indent_top,
            int indent_right,
            int indent_bottom
    ) {
        if ((indent_left < 0) && (indent_top < 0) && (indent_right < 0) & (indent_bottom < 0)) {
            Log.w(TAG, "Incorrect indent values for control. Set them to 0");
            indent_left   = 0;
            indent_top    = 0;
            indent_right  = 0;
            indent_bottom = 0;
        }

        this.Indents.put("left", indent_left);
        this.Indents.put("top", indent_top);
        this.Indents.put("right", indent_right);
        this.Indents.put("bottom", indent_bottom);

        this.activity_width  = activity_w;
        this.activity_height = activity_h;

        this.draw_point_x = canv_x + indent_left - indent_right;
        this.draw_point_y = canv_y + indent_top - indent_bottom;
    }


    // Конструктор контрола без отступов
    public GameControl(
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h
    ) {
        this.Indents.put("left", 0);
        this.Indents.put("top", 0);
        this.Indents.put("right", 0);
        this.Indents.put("bottom", 0);

        this.activity_width  = activity_w;
        this.activity_height = activity_h;

        this.draw_point_x = canv_x + this.Indents.get("left") - this.Indents.get("right");
        this.draw_point_y = canv_y + this.Indents.get("top") - this.Indents.get("bottom");
    }


    /*
    * Извлечение данных контрола
    */
    public int getX() { return this.draw_point_x; }

    public int getY() { return this.draw_point_y; }

    public int getActivity_w() { return this.activity_width; }

    public int getActivity_h() { return this.activity_height; }

    public int getLeftIndent() { return this.Indents.get("left"); }

    public int getTopIndent() { return this.Indents.get("top"); }

    public int getRightIndent() { return this.Indents.get("right"); }

    public int getBottomIndent() { return this.Indents.get("bottom"); }


    public Hashtable<String, Integer> getIndents() { return this.Indents; }


    public ArrayList<Integer> getActivityZone() {
        // Зону активности вернём списком значений
        ArrayList<Integer> dataList = new ArrayList<>();

        dataList.add(this.activity_width);
        dataList.add(this.activity_height);

        return dataList;
    }


    /*
     * Установка данных контрола
     */
    public void setX(int canv_new_x) { this.draw_point_x = canv_new_x; }

    public void setX(int canv_new_x, int indent_l, int indent_r) {
        if (indent_l > 0 && indent_r > 0) {
            this.draw_point_x = canv_new_x + indent_l - indent_r;
        }
        else {
            Log.e(TAG, "Incorrect indent values for control");
        }
    }

    public void setY(int canv_new_y) { this.draw_point_y = canv_new_y; }

    public void setY(int canv_new_y, int indent_t, int indent_b) {
        if (indent_t > 0 && indent_b > 0) {
            this.draw_point_y = canv_new_y + indent_t - indent_b;
        }
        else {
            Log.e(TAG, "Incorrect indent values for control");
        }
    }


    // Передача зоны активности массивом
    public void setActivityZone(ArrayList<Integer> newActivityZone) {
        this.activity_width = newActivityZone.get(0);
        this.activity_height = newActivityZone.get(1);
    }


    // Передача зоны активности по значениям width и height
    public void setActivityZone(int new_width, int new_height) {
        this.activity_width = new_width;
        this.activity_height = new_height;
    }


    // Деактивация зоны активности
    public void unsetActivityZone() {
        this.activity_width = 0;
        this.activity_height = 0;
    }


    public void setIndents(Hashtable<String, Integer> newIndents) {
        // По всем ключам найдём значения больше 0
        for (Enumeration<String> e = newIndents.keys(); e.hasMoreElements();) {
            if (newIndents.get(e.nextElement()) > 0) {
                // Присвоим эти значения контролу
                this.Indents.put(e.nextElement(), newIndents.get(e.nextElement()));

                Log.i(TAG, "Updating Indent " + e.nextElement());
            }
        }
    }


    public void setIndents(int indent_left, int indent_top, int indent_right, int indent_bottom) {
        // Если отступ отрицательный, то ничего не передаём
        if (indent_left > 0) this.Indents.put("left", indent_left);
        if (indent_top > 0) this.Indents.put("top", indent_top);
        if (indent_right > 0) this.Indents.put("right", indent_right);
        if (indent_bottom > 0) this.Indents.put("bottom", indent_bottom);
    }


    /*
     * Апдейт контролов
     */
    // Обновить контрол. Например, когда кнопка меняет рисунок или расположение
    public void updateGameControl(
            mainView newGamePanel,
            int canv_new_x,
            int canv_new_y,
            int new_activity_w,
            int new_activity_h,
            int new_indent_l,
            int new_indent_t,
            int new_indent_r,
            int new_indent_b
    ) {
        // Если -1, по-умолчанию берём правый край экрана с отступом 50 пикселей
        if (canv_new_x == -1) {
            canv_new_x = newGamePanel.getWidth() - 50;
        }

        if (canv_new_y == -1) {
            canv_new_y = 50;
        }

        // Если -1, по-умолчанию берём зону в 50 пикселей
        if (new_activity_w == -1) {
            new_activity_w = 50;
        }


        if (new_activity_h == -1) {
            new_activity_h = 50;
        }

        // Если отступ задан меньше 0, то игнорируем значение
        if (new_indent_l != -1){
            this.Indents.put("left", new_indent_l);

            canv_new_x += new_indent_l;
        }

        if (new_indent_t != -1){
            this.Indents.put("top", new_indent_t);

            canv_new_y += new_indent_t;
        }

        if (new_indent_r != -1){
            this.Indents.put("right", new_indent_r);

            canv_new_x -= new_indent_r;
        }

        if (new_indent_b != -1){
            this.Indents.put("bottom", new_indent_b);

            canv_new_y -= new_indent_b;
        }

        this.draw_point_x    = canv_new_x;
        this.draw_point_y    = canv_new_y;

        this.activity_width  = new_activity_w;
        this.activity_height = new_activity_h;
    }


    /*
     * Обработчики взаимодействия с контролами
     */
    // При нажатии на тачпад проверяется коллизия точки нажатия с зоной активности контрола
    public boolean isCollision(float touchEventX, float touchEventY) {
        return touchEventX > draw_point_x
                && touchEventX < draw_point_x + activity_width
                && touchEventY > draw_point_y
                && touchEventY < draw_point_y + activity_height;
    }
}
