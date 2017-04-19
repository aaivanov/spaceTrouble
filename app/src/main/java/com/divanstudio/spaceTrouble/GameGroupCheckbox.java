package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

import com.example.divanstudio.firsttry.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ_DDA on 27.03.2017.
 */

// TODO описание полей
public class GameGroupCheckbox extends GameControl {
    private GameLabel ControlLabel = null;
    private List<GameCheckbox> Checkboxes = new ArrayList<>();

    private String Name           = "";
    private String labelPosition  = "Left";
    private String Direction      = "Right";

    private int label_space       = 0;
    private int checkbox_distance = 0;
    private int groupcheckbox_id  = 0;

    private static final String TAG = GameGroupCheckbox.class.getSimpleName();

    // Конструктор по-умолчанию
    public GameGroupCheckbox() {
        super();

        // Id и тип класса дадут уникальное имя чекбоксу
        this.Name = this.groupcheckbox_id + "_GroupCheckBox";
    }


    // TODO Label position обработать и дистанцию
    public GameGroupCheckbox(
            GameLabel Label,
            List<GameCheckbox> GameCheckboxes,
            String Name,
            String Position,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            int checkbox_distance,
            int label_distance
    ) {
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.ControlLabel = Label;
        this.Checkboxes   = GameCheckboxes;

        if (Name == null) {
            this.Name = this.groupcheckbox_id + "_GroupCheckBox";
        }
        else {
            this.Name = Name;
        }

        this.labelPosition     = Position;
        this.label_space       = label_distance;
        this.checkbox_distance = checkbox_distance;

        // Переназначаем координаты чекбоксов и лейблов
        this.relocate(this.getX(), this.getY());
    }

    // Число чекбоксов в списке
    public int getCheckboxesSize () { return this.Checkboxes.size(); }


    public int getCheckedCount() {
        int counter = 0;
        for (GameCheckbox Checkbox : this.Checkboxes) {
            if (Checkbox.isChecked()) { counter += 1; }
        }

        return counter;
    }


    public int getWidth() {
        int width = 0;

        if (this.Checkboxes.size() > 0) {
            for (GameCheckbox checkbox : this.Checkboxes) {
                width += checkbox.getWidth();
            }
        }

        if (this.ControlLabel != null) {
            width += this.ControlLabel.getActivity_h();
        }

        return width;
    }


    public int getHeight() {
        int height = 0;

        if (this.Checkboxes.size() > 0) {
            for(GameCheckbox checkbox : this.Checkboxes) {
                height += checkbox.getHeight();
            }
        }

        if (this.ControlLabel != null) {
            height += this.ControlLabel.getActivity_h();
        }

        return height;
    }


    public GameCheckbox getPressedCheckbox(MotionEvent event) {
        for (int i = 0; i < this.Checkboxes.size(); i++){
            if (this.Checkboxes.get(i).isPressed(event)) {
                return this.Checkboxes.get(i);
            }
        }

        return null;
    }


    public String getReaction(MotionEvent event) { return getPressedCheckbox(event).getReaction(); }


    // Задаёт лейбл чекбокса
    public void setLabel (GameLabel newLabel) { this.ControlLabel = newLabel; }


    // Задаёт позицию лейбла
    public void setLabelPosition (String Position) { this.labelPosition = Position; }


    // Задаёт направление ряда чекбоксов
    public void setCheckboxesDirection (String newDirection) { this.Direction = newDirection; }


    // Задаёт список чекбоксов на основе ресурса картинки
    public void setCheckboxesByBitmap(
            Bitmap uncheckSource,
            Bitmap checkSource,
            int sep_rows,
            int sep_cols
    ) {
        List<GameCheckbox> newCheckboxes = new ArrayList<>();

        this.groupcheckbox_id = uncheckSource.getGenerationId();

        int part_x      = 0;
        int part_y      = 0;
        int part_width  = 0;
        int part_height = 0;

        Bitmap sourceCheckedPart;
        Bitmap sourceUnCheckedPart;

        try {
            part_width  = checkSource.getWidth() / sep_cols;
            part_height = checkSource.getHeight() / sep_rows;
        }
        catch (ArithmeticException e) {
            Log.e(TAG, "ERROR: Dividing by 0. Input params is incorrect.");
            e.printStackTrace();
        }

        for (int i = 0; i < sep_rows; i++) {
            for (int j = 0; j < sep_cols; j++) {
                sourceCheckedPart = Bitmap.createBitmap(
                        checkSource,
                        part_x,
                        part_y,
                        part_width,
                        part_height
                );

                sourceUnCheckedPart = Bitmap.createBitmap(
                        uncheckSource,
                        part_x,
                        part_y,
                        part_width,
                        part_height
                );

                // На основе ресурсов создаём чекбокс
                // И добавляем чекбокс в список
                newCheckboxes.add(new GameCheckbox(sourceUnCheckedPart, sourceCheckedPart));

                part_x += part_width;
            }

            part_y += part_height;
        }

        this.Checkboxes = newCheckboxes;

        this.setActivityZone(this.getWidth(), this.getHeight());
    }


    public void setGroupReaction(String newGroupReaction) {
        for(int i = 0; i < this.Checkboxes.size(); i++) {
            this.Checkboxes.get(i).setUncheckReaction(newGroupReaction);
            this.Checkboxes.get(i).setCheckReaction(newGroupReaction);
        }
    }


    public void setAllUnchecked() {
        for(int i = 0; i < this.Checkboxes.size(); i++) {
            this.Checkboxes.get(i).Uncheck();
        }
    }


    public void setAllChecked() {
        Log.d(TAG, "Size = " + this.Checkboxes.size());

        for(int i = 0; i < this.Checkboxes.size(); i++) {
            this.Checkboxes.get(i).Check();
        }
    }


    // Обработка нажатий на чекбоксы
    // Если нажимаем один чекбокс, то в списке автоматически чекаются все чекбоксы до нажатого
    public void groupChecking(MotionEvent event) {
        GameCheckbox pressedCheckbox = this.getPressedCheckbox(event);

        int start = this.Checkboxes.indexOf(pressedCheckbox);

        if (!this.Checkboxes.get(start).isChecked()) {
            // Чекаем все чекбоксы с текущим
            for (int i = 0; i <= start; i++) {
                if (!this.Checkboxes.get(i).isChecked()) {
                    this.Checkboxes.get(i).Check();
                }
            }

            // Анчекаем все чекбоксы после текущего
            for (int i = start + 1; i < this.Checkboxes.size(); i++) {
                if (this.Checkboxes.get(i).isChecked()) {
                    this.Checkboxes.get(i).Uncheck();
                }
            }
        }
        else {
            // Чекаем все чекбоксы до текущего
            for (int i = 0; i < start; i++) {
                if (!this.Checkboxes.get(i).isChecked()) {
                    this.Checkboxes.get(i).Check();
                }
            }

            // Анчекаем все чекбоксы с текущим
            for (int i = start; i < this.Checkboxes.size(); i++) {
                if (this.Checkboxes.get(i).isChecked()) {
                    this.Checkboxes.get(i).Uncheck();
                }
            }
        }
    }


    // Пересчитывает координаты группового чекбокса и его суб-элементов
    public void relocate(int x, int y) {
        /* Чекбоксы будут отображаться по направлению, по этому нужно пересчитать координаты.
         * Так как чекбоксы в списке передаются по ссылке, то все поля будут изменены.
         * Кроме этого, нужно расположить лебл контрола.
         * Расположение лейбла зависит от первого чекбокса или от последнего.
         */
        super.relocate(x, y);

        // Сохраним список чекбоксов
        List<GameCheckbox> uneditedControls = this.Checkboxes;

        // Удалим очистим список чекбоксов
        this.Checkboxes = null;
        this.Checkboxes = new ArrayList<>();

        // Обрабатываем первый чекбокс и сохраняем его как "предыдущий"
        GameCheckbox prevCheckBox = uneditedControls.get(0);

        // Если лейбл слева или сверху, определяем координаты первого чекбокса сразу
        if (this.ControlLabel != null) {
            if (this.labelPosition.toLowerCase().equals("left")) {
                this.ControlLabel.relocate(this.getX(), this.getY());

                prevCheckBox.relocate(
                        this.getX() + this.label_space + this.ControlLabel.getActivity_w(),
                        this.getY()
                );
            }

            if (this.labelPosition.toLowerCase().equals("top")) {
                this.ControlLabel.relocate(this.getX(), this.getY());

                prevCheckBox.relocate(
                        this.getX(),
                        this.getY() + this.label_space + this.ControlLabel.getActivity_h()
                );
            }
        }

        this.Checkboxes.add(prevCheckBox);

        // Обрабатываем остальные чекбоксы и сразу добавляем в список
        int prev_control_width = prevCheckBox.getActivity_w();
        int prev_control_height = prevCheckBox.getActivity_h();
        for (GameCheckbox checkBox : uneditedControls) {
            // Если чекбокс совпал с обработанным, то берём следующий чекбокс
            if (checkBox.equals(this.Checkboxes.get(0))) { continue; }

            if (this.Direction.toLowerCase().equals("right")) {
                checkBox.relocate(
                        prevCheckBox.getX() + prev_control_width + this.checkbox_distance,
                        prevCheckBox.getY()
                );
            }

            if (this.Direction.toLowerCase().equals("down")) {
                checkBox.relocate(
                        prevCheckBox.getX(),
                        prevCheckBox.getY() + prev_control_height + this.checkbox_distance
                );
            }

            this.Checkboxes.add(checkBox);

            prevCheckBox = checkBox;
            prev_control_width  = checkBox.getActivity_w();
            prev_control_height = checkBox.getActivity_h();
        }

        // Если лейбл справа или снизу, определяем координаты лейбла возле последнего чекбокса
        // После обработки чекбоксов prevCheckBox сохранил последний чекбокс списка
        // Можно это использовать
        if (this.ControlLabel != null) {
            if (this.labelPosition.toLowerCase().equals("right")) {
                this.ControlLabel.relocate(
                        prevCheckBox.getX() + this.label_space + prevCheckBox.getActivity_w(),
                        this.getY()
                );
            }

            if (this.labelPosition.toLowerCase().equals("bottom")) {
                this.ControlLabel.relocate(
                        this.getX(),
                        prevCheckBox.getY() + this.label_space + prevCheckBox.getActivity_h()
                );
            }
        }
    }


    // Рисование группового чекбокса на холсте
    public void onDraw(Canvas canvas) {
        try {
            // Рисование лейбла
            if (this.ControlLabel != null) {
                this.ControlLabel.onDraw(canvas);
            }

            // Рисование чекбоксов
            for (GameCheckbox checkBox : this.Checkboxes) {
                checkBox.onDraw(canvas);
            }
        }
        catch(NullPointerException e) {
            Log.e(TAG, "Group checkbox draw error");
            e.printStackTrace();
        }
    }


    public boolean isPressed(MotionEvent event) {
        for (int i = 0; i < this.Checkboxes.size(); i++){
            if (this.Checkboxes.get(i).isPressed(event)) { return true; }
        }

        return false;
    }
}
