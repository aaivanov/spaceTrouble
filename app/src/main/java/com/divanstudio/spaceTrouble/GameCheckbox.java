package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.example.divanstudio.firsttry.R;

import java.util.ArrayList;

import static java.lang.Math.max;

/**
 * Created by WJ_DDA on 21.03.2017.
 */

// TODO описание полей
public class GameCheckbox extends GameControl {
    private GameLabel ControlLabel = null;
    private GameButton CheckboxButton = null;

    private String labelPosition   = "Left";
    private String Name            = "";
    private String CheckReaction   = "";
    private String UncheckReaction = "";

    private int space       = 0;
    private int checkbox_id = 0;

    private Bitmap sourceChecked   = null;
    private Bitmap sourceUnchecked = null;

    private boolean check_status = false;

    private static final String TAG = GameCheckbox.class.getSimpleName();


    // Конструктор по-умолчанию
    public GameCheckbox(mainView gamePanel) {
        /*
         * По умолчанию будем создавать кнопку выхода из игры )
         */
        super();

        // Берём картинки из ресурсов
        this.sourceUnchecked = BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.tmp_menu_music_on
        );

        this.sourceChecked = BitmapFactory.decodeResource(
                gamePanel.getResources(),
                R.drawable.tmp_menu_music_off
        );

        // В ресурсах генерируется id ресурса, его используем как id контрола
        this.checkbox_id = sourceUnchecked.getGenerationId();

        // Id и тип класса дадут уникальное имя чекбоксу
        this.Name = checkbox_id + "_CheckBox";

        // Создаём пустой текст чекбокса
        this.ControlLabel = new GameLabel();
        this.ControlLabel.setText(this.Name + "_label");

        // Что будет, если мы чекнем чекбокс
        this.CheckReaction   = "Sound_Off";

        // И что будет, если мы анчекнем чекбокс
        this.UncheckReaction = "Sound_On";

        this.CheckboxButton = new GameButton(
                this.sourceUnchecked,
                0,
                0,
                this.sourceUnchecked.getWidth(),
                this.sourceUnchecked.getHeight(),
                this.Name + "_button",
                this.UncheckReaction
        );

        // Задаём активность чекбокса
        // this.setActivityZone(this.sourceUnchecked.getWidth(), this.sourceUnchecked.getHeight());
    }


    // Конструктор
    public GameCheckbox(
            Bitmap bitmapUncheckedSource,
            Bitmap bitmapCheckedSource,
            GameLabel Label,
            GameButton Button,
            String CheckReaction,
            String UncheckReaction,
            String CheckBoxName,
            String labelPos,
            int canv_x,
            int canv_y,
            int activity_w,
            int activity_h,
            int indent_l,
            int indent_t,
            int indent_r,
            int indent_b,
            int elements_space,
            boolean is_checked
    ) {
        /*
         * По умолчанию будем создавать кнопку выхода из игры )
         */
        super(canv_x, canv_y, activity_w, activity_h, indent_l, indent_t, indent_r, indent_b);

        this.sourceUnchecked = bitmapUncheckedSource;
        this.sourceChecked   = bitmapCheckedSource;
        this.checkbox_id     = this.sourceUnchecked.getGenerationId();

        if (CheckBoxName == null) {
            this.Name = this.checkbox_id + "_CheckBox";
        }
        else {
            this.Name = CheckBoxName;
        }

        this.labelPosition   = labelPos;
        this.ControlLabel    = Label;
        this.CheckboxButton  = Button;
        this.space           = elements_space;
        this.check_status    = is_checked;
        this.CheckReaction   = CheckReaction;
        this.UncheckReaction = UncheckReaction;

        // Задаём начальную реакцию кнопки
        // Если чекбокс не в статусе "Checked", на него вешается реакция для "Check"
        // Если чекбос в статусе "Checked", на него вешается реакция "Uncheck"
        if (this.check_status) {
            this.CheckboxButton.setClickReaction(this.UncheckReaction);
        }
        else{
            this.CheckboxButton.setClickReaction(this.CheckReaction);
        }
        // Если зона активности задана меньше нуля,
        // то будем её автоматически считать из лейбла и кнопки.
        int checkbox_activity_w = 0;
        int checkbox_activity_h = 0;

        if (activity_w < 0) {
            if ("left right".contains(this.labelPosition.toLowerCase())){
                checkbox_activity_w = this.ControlLabel.getActivity_w() + this.space +
                        this.CheckboxButton.getActivity_w();
            }

            if ("top bottom".contains(this.labelPosition.toLowerCase())) {
                checkbox_activity_w = max(
                        this.ControlLabel.getActivity_w(),
                        this.CheckboxButton.getActivity_w()
                );
            }
        }

        if (activity_h < 0) {
            if ("left right".contains(this.labelPosition.toLowerCase())) {
                checkbox_activity_h = max(
                        this.ControlLabel.getActivity_h(),
                        this.CheckboxButton.getActivity_h()
                );
            }

            if ("top bottom".contains(this.labelPosition.toLowerCase())) {
                checkbox_activity_h = this.ControlLabel.getActivity_h() + this.space +
                        this.CheckboxButton.getActivity_h();
            }
        }

        if ((activity_w < 0) && (activity_h < 0)) {
            this.setActivityZone(checkbox_activity_w, checkbox_activity_h);
        }

        if (this.ControlLabel != null) {
            if (this.labelPosition.toLowerCase().equals("left")) {
                this.ControlLabel.setX(this.getX());
                this.ControlLabel.setY(this.getY());
                this.CheckboxButton.setX(
                        this.getX() + this.space + this.ControlLabel.getActivity_w()
                );
                this.CheckboxButton.setY(this.getY());
            }

            if (this.labelPosition.toLowerCase().equals("top")) {
                this.ControlLabel.setX(this.getX());
                this.ControlLabel.setY(this.getY());
                this.CheckboxButton.setX(this.getX());
                this.CheckboxButton.setY(
                        this.getY() + this.space +  this.ControlLabel.getActivity_h()
                );
            }

            if (this.labelPosition.toLowerCase().equals("right")) {
                this.ControlLabel.setX(
                        this.getX() + this.space +  this.CheckboxButton.getActivity_w()
                );
                this.ControlLabel.setY(this.getY());
                this.CheckboxButton.setX(this.getX());
                this.CheckboxButton.setY(this.getY());
            }

            if (this.labelPosition.toLowerCase().equals("bottom")) {
                this.ControlLabel.setX(this.getX());
                this.ControlLabel.setY(
                        this.getY() + this.space +  this.CheckboxButton.getActivity_h()
                );
                this.CheckboxButton.setX(this.getX());
                this.CheckboxButton.setY(this.getY());
            }
        }

        this.setActivityZone(this.getWidth(), this.getHeight());
    }


    // Упрощённый конструктор без лейбла
    public GameCheckbox (
            Bitmap srcUncheckedSource,
            Bitmap srcCheckedSource
    ) {
        super(0, 0, srcUncheckedSource.getWidth(), srcUncheckedSource.getHeight(), 0, 0, 0, 0);

        this.sourceUnchecked = srcUncheckedSource;
        this.sourceChecked   = srcCheckedSource;
        this.CheckboxButton  = new GameButton(srcUncheckedSource);
        this.checkbox_id     = this.sourceUnchecked.getGenerationId();
        this.Name            = this.checkbox_id + "_" + this.CheckboxButton.getName() + "_checkbox";
        this.check_status    = false;
    }


    // TODO
    public int getButtonActivityWidth() {
        return 0;
    }


    // TODO
    public int getButtonActivityHeight() {
        return 0;
    }


    public int getWidth() {
        int checkbox_width  = 0;

        if (this.CheckboxButton != null) {
            checkbox_width += this.CheckboxButton.getActivity_w();
        }

        if (this.ControlLabel != null) {
            checkbox_width += this.ControlLabel.getActivity_w();
        }

        if (this.ControlLabel != null && this.CheckboxButton != null) {
            if ("left right".contains(this.labelPosition.toLowerCase())) {
                checkbox_width += this.space;
            }
        }

        return checkbox_width;
    }


    public int getHeight() {
        int checkbox_height = 0;

        if (this.CheckboxButton != null) {
            checkbox_height += this.CheckboxButton.getActivity_h();
        }

        if (this.ControlLabel != null) {
            checkbox_height += this.ControlLabel.getActivity_h();
        }

        if (this.ControlLabel != null && this.CheckboxButton != null) {
            if ("top bottom".contains(this.labelPosition.toLowerCase())) {
                checkbox_height += this.space;
            }
        }

        return checkbox_height;
    }


    public String getName() { return this.Name; }


    public void setCheckReaction (String newReaction) {
        this.CheckReaction = newReaction;
        this.CheckboxButton.setClickReaction(newReaction);
    }


    public void setUncheckReaction (String newReaction) {
        this.UncheckReaction = newReaction;
    }


    // TODO setLabel() {}


    public void Uncheck() {
        if (isChecked()){
            this.CheckboxButton.setBitmap(this.sourceUnchecked);
            this.CheckboxButton.setClickReaction(this.CheckReaction);
            this.check_status = false;
        }
    }


    public void Check() {
        if (!isChecked()){
            this.CheckboxButton.setBitmap(this.sourceChecked);
            this.CheckboxButton.setClickReaction(this.UncheckReaction);
            this.check_status = true;
        }
    }


    public boolean isChecked() {
        return this.check_status;
    }


    public boolean isPressed(MotionEvent event) {
        return this.CheckboxButton.isPressed(event);
    }


    public void onDraw(Canvas canvas) {
        try {
            // Рисование лейбла
            if (this.ControlLabel != null) {
                this.ControlLabel.onDraw(canvas);
            }

            // Рисование кнопки чекбокса
            this.CheckboxButton.onDraw(canvas);
        }
        catch(NullPointerException e) {
            Log.e(TAG, "Checkbox draw error");
            e.printStackTrace();
        }
    }


    public String getReaction() {
        return this.CheckboxButton.getReaction();
    }


    // Переназначает координаты чекбокса и его суб-элементов
    public void relocate(int x, int y) {
        super.relocate(x, y);

        int button_x = x;
        int button_y = y;
        int label_x = x;
        int label_y = y;

        if (this.ControlLabel != null) {
            if (this.labelPosition.toLowerCase().equals("left")) {
                button_x += this.ControlLabel.getActivity_w() + this.space;
            }

            if (this.labelPosition.toLowerCase().equals("top")) {
                button_y += this.ControlLabel.getActivity_h() + this.space;
            }

            if (this.labelPosition.toLowerCase().equals("right")) {
                label_x += this.CheckboxButton.getActivity_w() + this.space;
            }

            if (this.labelPosition.toLowerCase().equals("bottom")) {
                label_y += this.CheckboxButton.getActivity_h() + this.space;
            }

            this.ControlLabel.relocate(label_x, label_y);
        }

        this.CheckboxButton.relocate(button_x, button_y);
    }
}

