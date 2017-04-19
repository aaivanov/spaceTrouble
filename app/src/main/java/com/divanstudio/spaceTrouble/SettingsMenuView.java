package com.divanstudio.spaceTrouble;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.divanstudio.firsttry.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/**
 * Created by WJ_DDA on 03.04.2017.
 */

public class SettingsMenuView extends SurfaceView {
    private SurfaceHolder holder;
    private SettingsMenuManager settingsMenuThread;

    // Список всех контролов активности для отображения на холсте
    private List<GameControl> viewControls = new ArrayList<>();

    private Background background;

//    private MediaPlayer musicPlayer;
//    private float music_volume = 1.0f;

    // TODO Лучше создать ассоциативный массив
    private List<Integer> soundIDs = new ArrayList<>();
    private float sounds_volume = 1.0f;
    private SoundPool soundPoolPlayer;

//    // TODO Тестовые поля
//    private AssetManager AssetsManager = null;
//    private GameLabel testLabel;
//    private GameCheckbox SoundVolumeClicker;
//    private GameCheckbox MusicVolumeClicker;
//    private GameGroupCheckbox SoundVolume;

    private static final String TAG = SettingsMenuView.class.getSimpleName();

    public SettingsMenuView(Context context) {
        super(context);

//        // TODO для теста это!
//        this.AssetsManager = context.getAssets();

        this.settingsMenuThread = new SettingsMenuManager(this);
        this.holder = getHolder();

//        // Создаём проигрыватель.
//        // Он переходит в состояние Idle, потом в Initialized, после в Prepared
//        this.musicPlayer = MediaPlayer.create(context, R.raw.main_menu);
//
//        // Задаём параметры воспроизведения.
//        this.musicPlayer.setLooping(true);
//        this.musicPlayer.setVolume(this.music_volume, this.music_volume);

        // Создаём плеер звуков
        // STREAM_MUSIC позволяет настраивать громкость воспроизведения, привязанную к
        // основной настройке девайса
        this.soundPoolPlayer = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        try {
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_close_submenu, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_select_option, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_change_parameter, 1));

        } catch ( Exception e ) {
            Log.e(TAG, "Have some error:");
            e.printStackTrace();
        }

        this.holder.addCallback (new SurfaceHolder.Callback() {
            // TODO Сюда надо запихать реакцию от кнопки EXIT
            public void surfaceDestroyed (SurfaceHolder holder) {
                Log.i(TAG,"Surface destroying...");

                // Освобождаем ресурсы проигрывателя звуков
                soundPoolPlayer.release();

//                // Останавливаем проигрыватель музыки и удаляем
//                if (musicPlayer.isPlaying()){
//                    musicPlayer.stop();
//                }
//
//                musicPlayer.release();

                // Убиваем тред
                settingsMenuThread.threadStop();
            }

            public void surfaceCreated(SurfaceHolder holder) {
                settingsMenuThread.setRunning(true);
                settingsMenuThread.start();
                initViewRes();

                // Старт проигрывателя. Перейдёт в состояние Started
//                musicPlayer.start();
            }

            // Что делает этот метод?
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG,"Surface changed");
            }
        });
    }


    public void initViewRes() {
        this.background = new Background(this, BitmapFactory.decodeResource(
                getResources(),
                R.drawable.tmp_settings_menu_bckgrnd_1280_720
        ));

        // Создание Суб-меню Игры "Настройки" (СМИ)
        /* Ресурсы кнопок */
        Bitmap sourceAcceptButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_accept
        );

        Bitmap sourceDefaultButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_default
        );

        Bitmap sourceCancelButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_cancel
        );

        Bitmap sourceLSoundSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_left
        );

        Bitmap sourceRSoundSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_right
        );

        Bitmap sourceLMusicSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_left
        );

        Bitmap sourceRMusicSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_right
        );

        /* Инициализация кнопок */
        GameButton acceptButton = new GameButton(
                sourceAcceptButton,
                "ACCEPT",
                "Accept_Settings"
        );

        GameButton defaultButton = new GameButton(
                sourceDefaultButton,
                "DEFAULT",
                "Default_Settings"
        );

        // Правый верхний угол окна c отступом = 2 размера кнопки
        GameButton cancelButton = new GameButton(
                sourceCancelButton,
                this.getWidth() - sourceCancelButton.getWidth(),
                0,
                0,
                0,
                0,
                0,
                "CANCEL",
                "Cancel_Settings"
        );

        GameButton leftSoundSelectButton = new GameButton(
                sourceLSoundSelectButton,
                "Left_Select",
                "Decrease"
        );

        GameButton rightSoundSelectButton = new GameButton(
                sourceRSoundSelectButton,
                "Right_Select",
                "Increase"
        );

        GameButton leftMusicSelectButton = new GameButton(
                sourceLMusicSelectButton,
                "Left_Select",
                "Decrease"
        );

        GameButton rightMusicSelectButton = new GameButton(
                sourceRMusicSelectButton,
                "Right_Select",
                "Increase"
        );

        /* Инициализация Лейблов */
        // TODO Сделать
        /* Инициализация Чекбоксов */
        // TODO Сделать
        /* Инициализация Групповых Чекбоксов */
        // TODO Сделать

        /* Разметка меню */
        List<GameControl> activityManageButtons = new ArrayList<>();
        activityManageButtons.add(acceptButton);
        activityManageButtons.add(defaultButton);

        // Дистанцию между кнопками задаём переменной, чтобы разделить кнопки ровно от центра
        int distance = 30;

        // Отступ снизу берём от максимальной зоны активности кнопок меню
        int bottom_indent = 0;
        for (GameControl Control : activityManageButtons) {
            if (bottom_indent < Control.getActivity_h()){
                bottom_indent = Control.getActivity_h();
            }
        }

        GameMenu activityManageMenu = new GameMenu(
                activityManageButtons,
                "right",
                "center",
                "vertical",
                this.getWidth() / 2,
                this.getHeight(),
                0,
                0,
                0,
                0,
                0,
                bottom_indent,
                distance
        );

        /* Инициализация списка контролов активности */
        this.viewControls.add(cancelButton);
        this.viewControls.add(activityManageMenu);

//        // TODO Тест Лейблов
//        int label_canv_x          = 200;
//        int label_canv_y          = 50;
//        int label_activity_width  = 200;
//        int label_activity_height = label_activity_width / 2;
//        int label_indent_left     = 0;
//        int label_indent_top      = 0;
//        int label_indent_right    = 0;
//        int label_indent_bottom   = 0;
//
//        Typeface Font = Typeface.createFromAsset(this.AssetsManager, "fonts/android_robot.ttf");
//
//        this.testLabel = new GameLabel(
//                label_canv_x,
//                label_canv_y,
//                label_activity_width,
//                label_activity_height,
//                label_indent_left,
//                label_indent_top,
//                label_indent_right,
//                label_indent_bottom,
//                "Test_Label",
//                "<< Space Troubles >>",
//                0xFF876cff,
//                -1,
//                Font,
//                72
//        );
//
//        // TODO Тест Чекбоксов
//        Bitmap sourceSoundOnButton = BitmapFactory.decodeResource(
//                this.getResources(),
//                R.drawable.tmp_menu_music_on
//        );
//
//        Bitmap sourceMusicOnButton = BitmapFactory.decodeResource(
//                this.getResources(),
//                R.drawable.tmp_menu_music_on
//        );
//
//        Bitmap sourceSoundOffButton = BitmapFactory.decodeResource(
//                this.getResources(),
//                R.drawable.tmp_menu_music_off
//        );
//
//        Bitmap sourceMusicOffButton = BitmapFactory.decodeResource(
//                this.getResources(),
//                R.drawable.tmp_menu_music_off
//        );
//
//
//        GameButton SoundVolumeButton = new GameButton(
//                sourceSoundOnButton,
//                0,
//                0,
//                sourceSoundOnButton.getWidth(),
//                sourceSoundOnButton.getHeight(),
//                "Sound_Volume_Clicker"
//        );
//
//        GameButton MusicVolumeButton = new GameButton(
//                sourceMusicOnButton,
//                100,
//                this.getHeight() / 2 + sourceSoundOnButton.getHeight() + 30,
//                sourceMusicOnButton.getWidth(),
//                sourceMusicOnButton.getHeight(),
//                "Music_Volume_Clicker"
//        );
//
//        GameLabel SoundCheckBoxLabel = new GameLabel(
//                0,
//                0,
//                100,
//                sourceSoundOnButton.getHeight(),
//                0,
//                0,
//                0,
//                0,
//                "Sound_Label",
//                "Sounds: ",
//                0xFFFFFFFF,
//                -1,
//                Font,
//                18
//        );
//
//        GameLabel MusicCheckBoxLabel = new GameLabel(
//                0,
//                0,
//                100,
//                sourceMusicOnButton.getHeight(),
//                0,
//                0,
//                0,
//                0,
//                "Music_Label",
//                "Music: ",
//                0xFFFFFFFF,
//                -1,
//                Font,
//                18
//        );
//
//        this.SoundVolumeClicker = new GameCheckbox(
//                sourceSoundOnButton,
//                sourceSoundOffButton,
//                SoundCheckBoxLabel,
//                SoundVolumeButton,
//                "Sound_Off",
//                "Sound_On",
//                "SoundVolumeClicker",
//                "left",
//                0,
//                this.getHeight() / 2,
//                -1,
//                -1,
//                50,
//                0,
//                0,
//                0,
//                50,
//                false
//        );
//
//        this.MusicVolumeClicker = new GameCheckbox(
//                sourceMusicOnButton,
//                sourceMusicOffButton,
//                MusicCheckBoxLabel,
//                MusicVolumeButton,
//                "Music_Off",
//                "Music_On",
//                "MusicVolumeClicker",
//                "left",
//                0,
//                SoundVolumeClicker.getY() + 100,
//                -1,
//                -1,
//                50,
//                0,
//                0,
//                0,
//                50,
//                false
//        );
//
//        // TODO Тест Группового чекбокса
//        this.SoundVolume = new GameGroupCheckbox();
//        this.SoundVolume.setX(0);
//        this.SoundVolume.setY(200);
//
//        GameLabel SoundVolumeLabel = new GameLabel(
//                "Sound volume",
//                0xFFFFFFFF,
//                -1,
//                Font,
//                18
//        );
//
//        Bitmap sourceSoundLineOff = BitmapFactory.decodeResource(
//                this.getResources(),
//                R.drawable.tmp_menu_volume_uncheck
//        );
//
//        Bitmap sourceSoundLineOn = BitmapFactory.decodeResource(
//                this.getResources(),
//                R.drawable.tmp_menu_volume_check
//        );
//
//        SoundVolumeLabel.setActivityZone(200, sourceSoundLineOff.getHeight());
//
//        this.SoundVolume.setLabel(SoundVolumeLabel);
//        this.SoundVolume.setLabelPosition("left");
//        this.SoundVolume.setCheckboxesByBitmap(sourceSoundLineOff, sourceSoundLineOn, 1, 5);
//        this.SoundVolume.setCheckboxesDirection("right");
//        this.SoundVolume.setAllChecked();
//        this.SoundVolume.relocate(this.SoundVolume.getX(), this.SoundVolume.getY());
//
//        // Реакция чекбоксов одна, визуализация разная
//        this.SoundVolume.setGroupReaction("Setting_volume");
    }


    protected void drawView(Canvas canvas) {
        if (this.background != null) this.background.onDraw(canvas);

        if (this.viewControls != null) {
            for (GameControl viewControl : this.viewControls) {
                switch (viewControl.getClass().getSimpleName()){
                    case "GameButton":
                        GameButton Button = (GameButton) viewControl;
                        Button.onDraw(canvas);
                        break;

                    case "GameLabel":
                        GameLabel Label = (GameLabel) viewControl;
                        Label.onDraw(canvas);
                        break;

                    case "GameCheckbox":
                        GameCheckbox Checkbox = (GameCheckbox) viewControl;
                        Checkbox.onDraw(canvas);
                        break;

                    case "GameGroupCheckbox":
                        GameGroupCheckbox GroupCheckbox = (GameGroupCheckbox) viewControl;
                        GroupCheckbox.onDraw(canvas);
                        break;

                    case "GameMenu":
                        GameMenu Menu = (GameMenu) viewControl;
                        Menu.onDraw(canvas);
                        break;
                }
            }
        }

        //if (this.testLabel != null) this.testLabel.onDraw(canvas);
        //if (this.SoundVolumeClicker != null) this.SoundVolumeClicker.onDraw(canvas);
        //if (this.MusicVolumeClicker != null) this.MusicVolumeClicker.onDraw(canvas);
        //if (this.SoundVolume != null) this.SoundVolume.onDraw(canvas);
    }


//    public void setMusicVolume(float volume){
//        this.music_volume = volume;
//    }


    public void setSoundsVolume(float volume){
        this.sounds_volume = volume;
    }


    // Установка события при прикосновении к контролам
    public void setTouchEvent(SettingsMenuActivity settingActivity, MotionEvent event) {
        synchronized (getHolder()) {
            // Проанализируем все контролы и выполним доступную для них реакцию
            for (GameControl viewControl : this.viewControls) {
                switch (viewControl.getClass().getSimpleName()){
                    case "GameButton":
                        GameButton Button = (GameButton) viewControl;

                        if (Button.isPressed(event)) {
                            switch (Button.getReaction()){
                                case "Cancel_Settings":
                                    this.soundPoolPlayer.play(
                                            this.soundIDs.get(0),
                                            this.sounds_volume,
                                            this.sounds_volume,
                                            0,
                                            0,
                                            1.0f
                                    );

                                    // Закрываем активность

                                    settingActivity.onClose();

                                    break;
                            }
                        }

                        break;

//                    case "GameLabel":
//                        GameLabel Label = (GameLabel) viewControl;
//                        Label.onDraw(canvas);
//                        break;
//
//                    case "GameCheckbox":
//                        GameCheckbox Checkbox = (GameCheckbox) viewControl;
//                        Checkbox.onDraw(canvas);
//                        break;
//
//                    case "GameGroupCheckbox":
//                        GameGroupCheckbox GroupCheckbox = (GameGroupCheckbox) viewControl;
//                        GroupCheckbox.onDraw(canvas);
//
//                        break;

                    case "GameMenu":
                        GameMenu Menu = (GameMenu) viewControl;
                        GameButton touchedButton = Menu.getTouchedButton(event);

                        if (touchedButton != null) {
                            switch (touchedButton.getReaction()){
                                case "Accept_Settings":
                                    this.soundPoolPlayer.play(
                                            this.soundIDs.get(0),
                                            this.sounds_volume,
                                            this.sounds_volume,
                                            0,
                                            0,
                                            1.0f
                                    );

                                    // TODO ЧЕКПОИНТ: Добавить сохранение настроек


                                    /* Сохранение настроек */
                                    settingActivity.settingsSave();

                                    // Закрываем активность
                                    settingActivity.onClose();

                                    break;

                                case "Default_Settings":
                                    this.soundPoolPlayer.play(
                                            this.soundIDs.get(1),
                                            this.sounds_volume,
                                            this.sounds_volume,
                                            0,
                                            0,
                                            1.0f
                                    );


                                    /* Сброс настроек */
                                    /* Сохранение настроек */
                                    settingActivity.settingsSave();

                                    break;
                            }
                        }

                        break;
                }
            }

//            // TODO ToLowerCase
//            if (touchedButton != null) {
//                switch (touchedButton.getReaction()){
//                    case "Start":
//                        if (this.musicPlayer.isPlaying()){
//                            this.musicPlayer.stop();
//                        }
//
//                        this.musicPlayer.release();  //TODO Если плеер передаём новой активности, то удалить
//
//                        this.soundPoolPlayer.play(
//                                this.soundIDs.get(0),
//                                this.sounds_volume,
//                                this.sounds_volume,
//                                0,
//                                0,
//                                1.0f
//                        );
//
//                        // TODO Анимация ускорения мигания кнопки;
//                        // TODO Затемнение экрана;
////                        this.background.backgroundDarkerAnimation();
//
//                        // Запуск новой активности FullscreenActivity
//                        Intent intent = new Intent(settingActivity, FullscreenActivity.class);
//                        settingActivity.startActivity(intent);
//
//                        // Остановка треда и закрывание текущей активности MainMenuActivity
//                        this.settingsMenuThread.setRunning(false);
//                        settingActivity.finish();
//
//                        break;
//
//                    case "Options":
//                        this.soundPoolPlayer.play(
//                                this.soundIDs.get(1),
//                                this.sounds_volume,
//                                this.sounds_volume,
//                                0,
//                                0,
//                                1.0f
//                        );
//
//                        break;
//
//                    case "Exit":
//                        // TODO Всё перенести в surfaceDestroyed()
//                        // TODO И вызвать метод surfaceDestroyed()
//                        if (this.musicPlayer.isPlaying()){
//                            this.musicPlayer.stop();
//                        }
//
//                        this.musicPlayer.release();
//
//                        // TODO Обработка окончания проигрывния звуков
//                        this.soundPoolPlayer.play(
//                                this.soundIDs.get(2),
//                                this.sounds_volume,
//                                this.sounds_volume,
//                                0,
//                                0,
//                                1.0f
//                        );
//
//                        this.settingsMenuThread.setRunning(false);
//                        settingActivity.finish();
//
//                        break;
//                }
//            }
//
//            // TODO для тестирования
//            // Если нажали на чекбокс
//            if (this.SoundVolumeClicker.isPressed(event)) {
//                switch (this.SoundVolumeClicker.getReaction()){
//                    case "Sound_On":
//                        // TODO сохранить начальную громкость из this.sounds_volume
//                        this.SoundVolume.setAllChecked();
//                        this.setSoundsVolume(1.0f);
//                        break;
//                    case "Sound_Off":
//                        this.SoundVolume.setAllUnchecked();
//                        this.setSoundsVolume(0.0f);
//                        break;
//                }
//
//                if (this.SoundVolumeClicker.is_checked()){
//                    this.SoundVolumeClicker.Uncheck();
//                }
//                else {
//                    this.SoundVolumeClicker.Check();
//                }
//            }
//
//            if (this.MusicVolumeClicker.isPressed(event)) {
//                switch (this.MusicVolumeClicker.getReaction()){
//                    case "Music_On":
//                        // Подготавливаем проигрыватель
//                        try {
//                            this.musicPlayer.prepare();
//                        }
//                        catch (IOException e) {
//                            Log.e(TAG, "musicPlayer preparation error: IO");
//                            e.printStackTrace();
//                        }
//                        catch (IllegalStateException e) {
//                            Log.e(TAG, "musicPlayer preparation error: Illegal State");
//                            e.printStackTrace();
//                        }
//
//                        // Трек ставим сначала
//                        this.musicPlayer.seekTo(0);
//
//                        // Играем
//                        this.musicPlayer.start();
//                        break;
//
//                    case "Music_Off":
//                        if (this.musicPlayer.isPlaying()) {
//                            // Останавливаем проигрыватель
//                            this.musicPlayer.stop();
//                        }
//                        break;
//                }
//
//                if (this.MusicVolumeClicker.is_checked()){
//                    this.MusicVolumeClicker.Uncheck();
//                }
//                else {
//                    this.MusicVolumeClicker.Check();
//                }
//            }
//
//
//            if (this.SoundVolume.isPressed(event)) {
//                switch (this.SoundVolume.getReaction(event)) {
//                    case "Setting_volume":
//                        // Анчекаем нужные чекбоксы
//                        this.SoundVolume.groupChecking(event);
//
//                        // Считаем число чекнутых чекбоксов
//                        int checked_count = this.SoundVolume.getCheckedCount();
//
//                        // Теперь пересчитываем громкость
//                        this.sounds_volume = (float) checked_count / this.SoundVolume.getCheckboxesSize();
//
//                        // Активируем эту громкость
//                        this.setSoundsVolume(this.sounds_volume);
//
//                        Log.d(TAG, "Sounds volume set: " + this.sounds_volume);
//
//                        // Озвучка и, заодно, тестирование звука
//                        this.soundPoolPlayer.play(
//                                this.soundIDs.get(3),
//                                this.sounds_volume,
//                                this.sounds_volume,
//                                0,
//                                0,
//                                1.0f
//                        );
//
//                        // Заодно добавим реакцию на чекбокс отключения звука
//                        if (this.sounds_volume == 0.0) {
//                            if (!this.SoundVolumeClicker.is_checked()) {
//                                this.SoundVolumeClicker.Check();
//                            }
//                        }
//                        else {
//                            if (this.SoundVolumeClicker.is_checked()) {
//                                this.SoundVolumeClicker.Uncheck();
//                            }
//                        }
//
//                        break;
//                }
//            }
        }
    }
}
