package com.divanstudio.spaceTrouble;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.divanstudio.firsttry.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ_DDA on 08.02.2017.
 */
public class MainMenuView extends SurfaceView {
    private SurfaceHolder holder;
    private MainMenuManager mainMenuThread;

    private GameMenu mainMenu;

    private Background background;

    private MediaPlayer musicPlayer;
    private float music_volume = 1.0f;

    // TODO Лучше создать ассоциативный массив
    private List<Integer> soundIDs = new ArrayList<>();
    private float sounds_volume = 1.0f;
    private SoundPool soundPoolPlayer;

    private static final String TAG = MainMenuView.class.getSimpleName();

    public MainMenuView(Context context) {
        super(context);

        this.mainMenuThread = new MainMenuManager(this);
        this.holder = getHolder();

        this.musicPlayer = MediaPlayer.create(context, R.raw.main_menu);
        this.musicPlayer.setLooping(true); // Set looping
        this.musicPlayer.setVolume(this.music_volume, this.music_volume);

        // Создаём плеер звуков
        // STREAM_MUSIC позволяет настраивать громкость воспроизведения
        this.soundPoolPlayer = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
//        this.soundPoolPlayer.setOnLoadCompleteListener(context);

        try {
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_select_start, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_open_submenu, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_select_option, 1));
        } catch ( Exception e ) {
            Log.e(TAG, "Have some error:");
            Log.e(TAG, String.valueOf(e));
        }

        this.holder.addCallback (new SurfaceHolder.Callback() {
            public void surfaceDestroyed (SurfaceHolder holder) {
                boolean retry = true;
                mainMenuThread.setRunning (false);
                while (retry) {
                    try {
                        mainMenuThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
                musicPlayer.stop();
                musicPlayer.release();
            }

            public void surfaceCreated(SurfaceHolder holder) {
                mainMenuThread.setRunning(true);
                mainMenuThread.start();
                initViewRes();
                musicPlayer.start();
            }

            // Что делает этот метод?
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }


    public void initViewRes() {
        this.background = new Background(this, BitmapFactory.decodeResource(
                getResources(),
                R.drawable.tmp_main_menu_bckgrnd_1280_720
        ));

        // Создание Главного Меню Игры (ГМИ)
        int button_canv_x = 0;
        int button_canv_y = 0;

        Bitmap sourceStartButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_start
        );

        Bitmap sourceExitButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_exit
        );

        Bitmap sourceOptionsButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_options
        );

        GameButton startMenuButton = new GameButton(
                sourceStartButton,
                button_canv_x,
                button_canv_y,
                sourceStartButton.getWidth(),
                sourceStartButton.getHeight(),
                "Start_Main_Menu_Button",
                "Start"
        );

        GameButton exitMenuButton = new GameButton(
                sourceExitButton,
                button_canv_x,
                button_canv_y,
                sourceExitButton.getWidth(),
                sourceExitButton.getHeight(),
                "Exit_Main_Menu_Button",
                "Exit"
        );

        GameButton optionsMenuButton = new GameButton(
                sourceOptionsButton,
                button_canv_x,
                button_canv_y,
                sourceOptionsButton.getWidth(),
                sourceOptionsButton.getHeight(),
                "Options_Main_Menu_Button",
                "Options"
        );

        // Добавление кнопок в список. Порядок добавления важен.
        List<GameButton> mainMenuButtons = new ArrayList<>();
        mainMenuButtons.add(startMenuButton);
        mainMenuButtons.add(optionsMenuButton);
        mainMenuButtons.add(exitMenuButton);

        String menuDirection = "down";
        String buttonAlign   = "center";

        int button_distance      = 20;
        int menu_canv_x          = this.getWidth() / 2;
        int menu_canv_y          = this.getHeight() / 2;
        int menu_activity_width  = 0;
        int menu_activity_height = 0;
        int menu_indent_left     = 0;
        int menu_indent_top      = 0;
        int menu_indent_right    = 0;
        int menu_indent_bottom   = 0;

        this.mainMenu = new GameMenu(
                mainMenuButtons,
                menuDirection,
                buttonAlign,
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
    }


    protected void onDraw(Canvas canvas) {
        if (this.background != null) this.background.onDraw(canvas);
        if (this.mainMenu != null) this.mainMenu.onDraw(canvas);
    }


    public void setMusicVolume(float volume){
        this.music_volume = volume;
    }


    public void setSoundsVolume(float volume){
        this.sounds_volume = volume;
    }


    // Установка события при прикосновении
    // TODO ЧЕКПОИНТ! Громкость звуков громкая и не меняется! Громкость музыки меняется от настроек громкости телефона.
    public void setTouchEvent(MainMenuActivity menuActivity, MotionEvent event) {
        synchronized (getHolder()) {
            // Пишем реакцию кнопок
            GameButton touchedButton = this.mainMenu.getTouchedButton(event);

            if (touchedButton != null) {
                switch (touchedButton.getReaction()){
                    case "Start":
                        this.musicPlayer.stop();
                        this.soundPoolPlayer.play(
                                this.soundIDs.get(0),
                                this.sounds_volume,
                                this.sounds_volume,
                                0,
                                0,
                                1.0f
                        );

                        // TODO Анимация ускорения мигания кнопки;
                        // TODO Затемнение экрана;
//                        this.background.backgroundDarkerAnimation();

                        Intent intent = new Intent(menuActivity, FullscreenActivity.class);
                        menuActivity.startActivity(intent);

                        this.mainMenuThread.setRunning(false);
                        menuActivity.finish();

                        break;

                    case "Options":
                        this.soundPoolPlayer.play(
                                this.soundIDs.get(1),
                                this.sounds_volume,
                                this.sounds_volume,
                                0,
                                0,
                                1.0f
                        );

                        break;

                    case "Exit":
                        this.musicPlayer.stop();
                        this.soundPoolPlayer.play(
                                this.soundIDs.get(2),
                                this.sounds_volume,
                                this.sounds_volume,
                                0,
                                0,
                                1.0f
                        );

                        this.mainMenuThread.setRunning(false);
                        menuActivity.finish();

                        break;
                }
            }
        }
    }
}
