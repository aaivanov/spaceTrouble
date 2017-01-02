package com.divanstudio.firsttry.ST;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by WJ_DDA on 29.09.2016.
 */

//TODO Более общее название класса контрола Control - занято. Но Надо его занять
public class GameControl {
    //TODO Если много панелей игры будет, надо добавить
    //private MainGamePanel gamePanel; // Игровая панель, к которой принадлежит контрол
    //private Bitmap ControlBitmap;    // Картинка контрола

    private int draw_point_x;        // Координата Х точки отображения контрола
    private int draw_point_y;        // Координата Y точки отображения контрола

    private int activity_width;      // Ширина зоны активности
    private int activity_height;     // Высота зоны активности

    // Конструктор по-умолчанию
    public GameControl() {
        /*
         * По умолчанию создаём контрол в левом верхнем углу )
         */

        // Берём нулевую зону активности
        this.activity_width = 0;
        this.activity_height = 0;

        // Контрол в левом верхнем углу
        // TODO Предположил я, что рисуется контрол от центра картинки. А может - неправда
        // Получим кнопку в верхнем правом углу экрана
        this.draw_point_x = 0;
        this.draw_point_y = 0;
    }


    // Конструктор
    public GameControl(
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h
    ) {
        this.draw_point_x    = canv_x;
        this.draw_point_y    = canv_y;

        this.activity_width  = activity_w;
        this.activity_height = activity_h;
    }


    /*
    * Извлечение данных контрола
    */
    public int getX() { return this.draw_point_x; }


    public int getY() { return this.draw_point_y; }

    public int getActivity_w() { return this.activity_width; }

    public int getActivity_h() { return this.activity_height; }


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


    public void setY(int canv_new_y) { this.draw_point_y = canv_new_y; }


    // Стандартный способ передачи зоны активности
    public void setActivityZone(ArrayList<Integer> newActivityZone) {
        this.activity_width = newActivityZone.get(0);
        this.activity_height = newActivityZone.get(1);
    }


    // Альтернативный способ передачи зоны активности
    public void setActivityZone(int new_width, int new_height) {
        this.activity_width = new_width;
        this.activity_height = new_height;
    }


    /*
     * Апдейт контролов
     */
    // Обновить контрол. Например, когда кнопка меняет рисунок или расположение
    public void updateGameControl(
            MainGamePanel newGamePanel,
            int canv_new_x,
            int canv_new_y,
            int new_activity_w,
            int new_activity_h
    ) {
        /*
         *
         */

        // Если -1, по-умолчанию берём правый край экрана с отступом 50 пикселей
        if (canv_new_x == -1) {
            canv_new_x = newGamePanel.getWidth() - 50;
        }
        this.draw_point_x = canv_new_x;

        if (canv_new_y == -1) {
            canv_new_y = 50;
        }
        this.draw_point_y = canv_new_y;

        // Если -1, по-умолчанию берём зону в 50 пикселей
        if (new_activity_w == -1) {
            new_activity_w = 50;
        }
        this.activity_width = new_activity_w;

        if (new_activity_h == -1) {
            new_activity_h = 50;
        }
        this.activity_height = new_activity_h;
    }


    /*
     * Обработчики взаимодействия с контролами
     */
    // TODO Коснулся ли палец контрола. Дописать
    public boolean isCollision() {
        return true;
    }


    /*
     * Рисование контрола на экране игры
     */
//    public void onDraw(Canvas canvas) {
//        canvas.drawBitmap(control, src, dst, null);
//    }


    /*
     * Деструкция?
     */
}
