package com.divanstudio.spaceTrouble;

import com.example.divanstudio.firsttry.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;

import android.media.MediaPlayer;
import android.media.AudioManager;
import android.media.SoundPool;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import org.json.JSONObject;

import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.JSONException;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by WJ_DDA on 08.02.2017.
 */
public class MainMenuView extends SurfaceView {
    private SurfaceHolder holder;

    // TODO: Есть мнение, что здесь нужен Runnable, а выполнение треда запихать отдельно
    // TODO: Таким образом мы для каждого окна игры создаём задачу на запуск и вызываем под эту задачу тред.
    // TODO: Вероятнее всего, когда будет введена анимация, это будет удобнее.
    private MainMenuManager mainMenuThread;

    private String settingFileName = "SpaceTroublesSettings.json";

    private List<GameControl> settingsMenuControls = new ArrayList<>();
    private GameMenu mainMenu;

    // TODO Лучше создать ассоциативный массив
    private List<Integer> soundIDs = new ArrayList<>();

    private Background MainMenuBackground;
    private Background SettingsMenuBackground;

    private MediaPlayer musicPlayer;
    private SoundPool soundPoolPlayer;

    private AssetManager AssetsManager = null;

    private float sound_volume = 1.0f;
    private float music_volume = 1.0f;

    private boolean is_main_menu = true;
    private boolean is_music_off = false;
    private boolean is_sound_off = false;

    private static final String TAG = MainMenuView.class.getSimpleName();

    public MainMenuView(Context context) {
        super(context);

        // Создаём тред
        this.mainMenuThread = new MainMenuManager(this);

        // Создаём проигрыватель.
        // Он переходит в состояние Idle, потом в Initialized, после в Prepared
        this.musicPlayer = MediaPlayer.create(context, R.raw.main_menu);

        // Задаём параметры воспроизведения.
        this.musicPlayer.setLooping(true);
        this.musicPlayer.setVolume(this.music_volume, this.music_volume);

        // Создаём плеер звуков
        // STREAM_MUSIC позволяет настраивать громкость воспроизведения, привязанную к
        // основной настройке девайса
        this.soundPoolPlayer = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        try {
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_select_start, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_open_submenu, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_close_submenu, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_change_parameter, 1));
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_select_option, 1));

            //TODO Наверное для тестов и убрать потом
            this.soundIDs.add(this.soundPoolPlayer.load(context, R.raw.menu_change_parameter, 1));
        } catch ( Exception e ) {
            Log.e(TAG, "Have some error:");
            e.printStackTrace();
        }

        // Создаём обработчик отображения холста (SurfaceView)
        this.holder = getHolder();
        this.holder.addCallback (new SurfaceHolder.Callback() {
            // При уничтожении объекта MainMenuView выполняется эта функция
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG,"Surface destroying...");

                // Освобождаем ресурсы проигрывателя звуков
                soundPoolPlayer.release();

                // Останавливаем проигрыватель музыки и удаляем
                if (musicPlayer.isPlaying()){
                    musicPlayer.stop();
                }

                musicPlayer.release();

                // Убиваем тред
                mainMenuThread.threadStop();
            }

            // При создании MainMenuView выполняется эта функция
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG,"Surface creating...");

                mainMenuThread.setRunning(true);
                mainMenuThread.start();
                AssetsManager = getContext().getAssets();

                initViewRes();

                // Старт проигрывателя. Перейдёт в состояние Started
                musicPlayer.start();
            }

            // Что делает этот метод?
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG,"Surface changed");
            }
        });
    }


    // TODO Фунция громоздкая и неудобная для анализа, разбить
    // TODO Попробовать использовать 1 ресурс на несколько объектов
    // TODO Выравнивание лейблов и контролов субменюшками сделать
    private void initViewRes() {
        // Загрузка настроек игры
        Log.i(TAG,"Loading game settings");
        this.loadSettings(this.getContext());

        /* ===================================== */
        /* Инициализация главного меню           */
        /* ===================================== */
        Log.i(TAG,"Initializing main menu");

        // Задаём бэкграунд главного меню
        this.MainMenuBackground = new Background(this, BitmapFactory.decodeResource(
                getResources(),
                R.drawable.tmp_main_menu_bckgrnd_1280_720
        ));

        // Список кнопок главного меню
        List<GameControl> mainMenuButtons = new ArrayList<>();

        // Кнопка - "Start"
        Bitmap srcStartButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_start
        );

        GameButton startMenuButton = new GameButton(
                srcStartButton,
                "Start_Main_Menu_Button",
                "Start"
        );

        mainMenuButtons.add(startMenuButton);

        // Кнопка - "Options"
        Bitmap srcOptionsButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_options
        );

        GameButton optionsMenuButton = new GameButton(
                srcOptionsButton,
                "Options_Main_Menu_Button",
                "Options"
        );

        mainMenuButtons.add(optionsMenuButton);

        // Кнопка - "Exit"
        Bitmap srcExitButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_exit
        );

        GameButton exitMenuButton = new GameButton(
                srcExitButton,
                "Exit_Main_Menu_Button",
                "Exit"
        );

        mainMenuButtons.add(exitMenuButton);

        /* Инициализация объекта главного меню */
        String menuDirection     = "down";
        String controlAlign      = "center";
        String controlsCentering = "All";

        //TODO Внести это меню в список контролов MainMenuView
        this.mainMenu = new GameMenu(
                mainMenuButtons,
                menuDirection,
                controlAlign,
                controlsCentering,
                this.getWidth() / 2,
                this.getHeight() / 2,
                50
        );

        /* ===================================== */
        /* Инициализация меню настроек           */
        /* ===================================== */
        Log.i(TAG,"Initializing settings menu");

        // Задаём бэкграунд меню настроек
        this.SettingsMenuBackground = new Background(this, BitmapFactory.decodeResource(
                getResources(),
                R.drawable.tmp_settings_menu_bckgrnd_1280_720
        ));

        // Задаём шрифт для лейблов
        Typeface Font = Typeface.createFromAsset(this.AssetsManager, "fonts/android_robot.ttf");

        // Задаём лейбл "OPTIONS" вверху активности одиночный контрол
        Log.i(TAG,"Initializing settings menu signboard");
        GameLabel optionsSignboard = new GameLabel(
                this.getWidth() / 2,
                50,
                "options_label",
                "OPTIONS",
                0xFFFFFFFF,
                0x66999999,
                Font,
                52
        );

        optionsSignboard.VCentering();

        this.settingsMenuControls.add(optionsSignboard);

        // Кнопка - "CANCEL" - одиночный контрол
        // Располагается в правом верхнем углу окна
        Log.i(TAG,"Initializing 'Cancel' button");
        Bitmap srcCancelButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_cancel
        );

        GameButton cancelButton = new GameButton(
                srcCancelButton,
                this.getWidth() - srcCancelButton.getWidth(),
                0,
                "CANCEL",
                "Cancel_Settings"
        );

        this.settingsMenuControls.add(cancelButton);

        /* Инициализация меню управления активностью */
        Log.i(TAG,"Initializing menu activity control");

        // Список кнопок меню управления активностью
        List<GameControl> activityManageButtons = new ArrayList<>();

        // Кнопка - "ACCEPT"
        Bitmap srcAcceptButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_accept
        );

        GameButton acceptButton = new GameButton(
                srcAcceptButton,
                "ACCEPT",
                "Accept_Settings"
        );

        activityManageButtons.add(acceptButton);

        // Кнопка - "DEFAULT"
        Bitmap srcDefaultButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_default
        );

        GameButton defaultButton = new GameButton(
                srcDefaultButton,
                "DEFAULT",
                "Default_Settings"
        );

        activityManageButtons.add(defaultButton);

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
                "vertical"
        );

        activityManageMenu.setBottomIndent(bottom_indent);
        activityManageMenu.setDistance(30);
        activityManageMenu.relocate(this.getWidth() / 2, this.getHeight());

        this.settingsMenuControls.add(activityManageMenu);

        /* Инициализация меню управления звуковым проигрывателем */
        // Список контролов меню управления звуковым проигрывателем
        List<GameControl> soundPlayerControls = new ArrayList<>();

        // Лейбл "Sound test"
        GameLabel soundPlayerLabel = new GameLabel(
                "sound_player",
                "Sound test",
                0xFFFFFFFF,
                0x66999999,
                Font,
                40
        );

        // Лейбл "Sound number"
        GameLabel soundNumberLabel = new GameLabel(
                "sound_num",
                "1",
                0xFFFFFFFF,
                0x66999999,
                Font,
                48
        );

        // Лейбл "Sound file"
        GameLabel soundFileLabel = new GameLabel(
                "sound_file",
                "menu_select_start.mp3",
                0xFFFFFFFF,
                0x66999999,
                Font,
                24
        );

        // Кнопка - "Left_Select" для звукового проигрывателя
        Bitmap srcLSoundSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_left
        );

        GameButton leftSoundSelectButton = new GameButton(
                srcLSoundSelectButton,
                "Left_Select",
                "Decrease"
        );

        // Кнопка - "Right_Select" для звукового проигрывателя
        Bitmap srcRSoundSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_right
        );

        GameButton rightSoundSelectButton = new GameButton(
                srcRSoundSelectButton,
                "Right_Select",
                "Increase"
        );

        // Кнопка - "Play" для звукового проигрывателя
        Bitmap srcSoundPlayButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_play
        );

        GameButton soundPlayButton = new GameButton(
                srcSoundPlayButton,
                "play_sound",
                "Play_Sound"
        );

        // Собираем список контролов
        soundPlayerControls.add(soundPlayerLabel);
        soundPlayerControls.add(leftSoundSelectButton);
        soundPlayerControls.add(soundNumberLabel);
        soundPlayerControls.add(rightSoundSelectButton);
        soundPlayerControls.add(soundPlayButton);
        soundPlayerControls.add(soundFileLabel);

        // Задаём меню с отступом от главного лейбла
        GameMenu soundPlayerMenu = new GameMenu(
                soundPlayerControls,
                "right",
                "center",
                "horizontal",
                100,
                optionsSignboard.getY() + optionsSignboard.getActivity_h() + 100,
                50
        );

        // Добавляем в список контролов настроек
        this.settingsMenuControls.add(soundPlayerMenu);

        /* Инициализация меню управления музыкальным проигрывателем */
        // Лейбл "Music test"
        GameLabel musicPlayerLabel = new GameLabel(
                "music_player",
                "Music test",
                0xFFFFFFFF,
                0x66999999,
                Font,
                40
        );

        // Лейбл "Track number"
        GameLabel trackNumberLabel = new GameLabel(
                "music_num",
                "1",
                0xFFFFFFFF,
                0x66999999,
                Font,
                48
        );

        // Лейбл "Track file"
        GameLabel trackFileLabel = new GameLabel(
                "track_file",
                "main_menu.mp3",
                0xFFFFFFFF,
                0x66999999,
                Font,
                24
        );

        // Кнопка - "Left_Select" для музыкального проигрывателя
        Bitmap srcLMusicSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_left
        );

        GameButton leftMusicSelectButton = new GameButton(
                srcLMusicSelectButton,
                "Left_Select",
                "Decrease"
        );

        // Кнопка - "Right_Select" для музыкального проигрывателя
        Bitmap srcRMusicSelectButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_select_right
        );

        GameButton rightMusicSelectButton = new GameButton(
                srcRMusicSelectButton,
                "Right_Select",
                "Increase"
        );

        // Кнопка - "Play" для музыкального проигрывателя
        Bitmap srcMusicPlayButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_play
        );

        GameButton musicPlayButton = new GameButton(
                srcMusicPlayButton,
                "play_music",
                "Play_Music"
        );

        // Список контролов меню управления музыкальным проигрывателем
        List<GameControl> musicPlayerControls = new ArrayList<>();

        // Собираем список контролов
        musicPlayerControls.add(musicPlayerLabel);
        musicPlayerControls.add(leftMusicSelectButton);
        musicPlayerControls.add(trackNumberLabel);
        musicPlayerControls.add(rightMusicSelectButton);
        musicPlayerControls.add(musicPlayButton);
        musicPlayerControls.add(trackFileLabel);

        // Задаём меню с отступом от меню звукового проигрывателя
        GameMenu musicPlayerMenu = new GameMenu(
                musicPlayerControls,
                "right",
                "center",
                "",
                100,
                soundPlayerMenu.getY() + soundPlayerMenu.getMenuHeight() + 50,
                50
        );

        // Добавляем в список контролов настроек
        this.settingsMenuControls.add(musicPlayerMenu);

        /* Инициализация меню управления громкости воспроизведения звуков */
        // Лейбл "Sound volume"
        GameLabel soundVolumeLabel = new GameLabel(
                "sound_volume",
                "Sound volume",
                0xFFFFFFFF,
                0x66999999,
                Font,
                40
        );

        // Чекбокс - "Sound Off/On"
        // TODO Дать имя
        Bitmap srcSoundOnButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_sound_on
        );

        Bitmap srcSoundOffButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_sound_off
        );

        GameCheckbox soundVolumeClicker = new GameCheckbox(srcSoundOnButton, srcSoundOffButton);

        // Групповой чекбокс - "Sound volume line"
        // TODO Дать имя
        Bitmap srcSoundVolumeOffLine = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_volume_uncheck
        );

        Bitmap srcSoundVolumeOnLine = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_volume_check
        );

        GameGroupCheckbox soundVolumeLine = new GameGroupCheckbox();

        soundVolumeLine.setCheckboxesByBitmap(srcSoundVolumeOffLine, srcSoundVolumeOnLine, 1, 5);
        soundVolumeLine.setCheckboxesDirection("right");
        soundVolumeLine.setAllChecked();
        soundVolumeLine.setGroupReaction("Setting_volume");
        soundVolumeLine.relocate(soundVolumeLine.getX(), soundVolumeLine.getY());

        // Список контролов меню управления громкостью звука
        List<GameControl> soundVolumeControls = new ArrayList<>();

        // Собираем список контролов
        soundVolumeControls.add(soundVolumeLabel);
//        soundVolumeControls.add(soundVolumeLine);
        soundVolumeControls.add(soundVolumeClicker);

        // Задаём меню с отступом от меню музыкального проигрывателя
        // TODO CHECKPOINT
        GameMenu soundVolumeMenu = new GameMenu(
                soundVolumeControls,
                "right",
                "",
                "",
                100,
                musicPlayerMenu.getY() + musicPlayerMenu.getMenuHeight() + 100,
                50
        );

        // Добавляем в список контролов настроек
        this.settingsMenuControls.add(soundVolumeMenu);

        /* Инициализация меню управления громкости воспроизведения музыки */
        // Лейбл "Music volume"
        GameLabel musicVolumeLabel = new GameLabel(
                "music_volume",
                "Music volume",
                0xFFFFFFFF,
                0x66999999,
                Font,
                40
        );

        // Чекбокс - "Music Off/On"
        // TODO Дать имя
        Bitmap srcMusicOnButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_music_on
        );

        Bitmap srcMusicOffButton = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_music_off
        );

        GameCheckbox musicVolumeClicker = new GameCheckbox(srcMusicOnButton, srcMusicOffButton);

        // Групповой чекбокс - "Music volume line"
        // TODO Дать имя
        Bitmap srcMusicVolumeOffLine = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_volume_uncheck
        );

        Bitmap srcMusicVolumeOnLine = BitmapFactory.decodeResource(
                this.getResources(),
                R.drawable.tmp_menu_volume_check
        );

        GameGroupCheckbox musicVolumeLine = new GameGroupCheckbox();

        musicVolumeLine.setCheckboxesByBitmap(srcMusicVolumeOffLine, srcMusicVolumeOnLine, 1, 5);
        musicVolumeLine.setCheckboxesDirection("right");
        musicVolumeLine.setAllChecked();
        musicVolumeLine.setGroupReaction("Setting_volume");
        musicVolumeLine.relocate(musicVolumeLine.getX(), musicVolumeLine.getY());

        // Список контролов меню управления громкостью музыки
        List<GameControl> musicVolumeControls = new ArrayList<>();

        // Собираем список контролов
        musicVolumeControls.add(musicVolumeLabel);
//        musicVolumeControls.add(musicVolumeLine);
        musicVolumeControls.add(musicVolumeClicker);

        // Задаём меню с отступом от меню настройки громкости звука
//        GameMenu musicVolumeMenu = new GameMenu(
//                musicVolumeControls,
//                "right",
//                "center",
//                "horizontal",
//                100,
//                soundVolumeMenu.getY() + soundVolumeMenu.getMenuHeight() + 50,
//                50
//        );

        // Добавляем в список контролов настроек
//        this.settingsMenuControls.add(musicVolumeMenu);
    }


    protected void drawView(Canvas canvas) {
        // Если у нас активировано главное меню
        if (this.is_main_menu) {
            if (this.MainMenuBackground != null) this.MainMenuBackground.onDraw(canvas);
            if (this.mainMenu != null) this.mainMenu.onDraw(canvas);
        }

        // Если у нас активировано меню настроек
        if (!this.is_main_menu) {
            if (this.SettingsMenuBackground != null) this.SettingsMenuBackground.onDraw(canvas);
            if (this.settingsMenuControls != null) {
                for (GameControl viewControl : this.settingsMenuControls) {
                    switch (viewControl.getClass().getSimpleName()){
                        case "GameMenu":
                            GameMenu Menu = (GameMenu) viewControl;
                            Menu.onDraw(canvas);
                            break;

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
                    }
                }
            }
        }
    }


    public void setMusicVolume(float volume){
        this.music_volume = volume;
    }


    public void setSoundsVolume(float volume){
        this.sound_volume = volume;
    }


    // Установка события при прикосновении
    public void setTouchEvent(MainMenuActivity menuActivity, MotionEvent event) {
        // Тут пишем реакцию кнопок
        synchronized (getHolder()) {
            if (this.is_main_menu) {
                GameButton touchedButton = this.mainMenu.getTouchedButton(event);
                if (touchedButton != null) {
                    switch (touchedButton.getReaction()) {
                        case "Start":
                            this.soundPoolPlayer.play(
                                    this.soundIDs.get(0),
                                    this.sound_volume,
                                    this.sound_volume,
                                    0,
                                    0,
                                    1.0f
                            );

                            // Паузим тред, чтобы проигрался звук
                            mainMenuThread.threadPause(2000);

                            // TODO Анимация ускорения мигания кнопки;
                            // TODO Затемнение экрана;
//                        this.background.backgroundDarkerAnimation();

                            // Запуск новой активности FullscreenActivity
                            Intent callGame = new Intent(menuActivity, FullscreenActivity.class);

                            menuActivity.setKilling(false);
                            menuActivity.onClose();

                            menuActivity.startActivity(callGame);

                            break;

                        case "Options":
                            this.soundPoolPlayer.play(
                                    this.soundIDs.get(1),
                                    this.sound_volume,
                                    this.sound_volume,
                                    0,
                                    0,
                                    1.0f
                            );

                            // Активируем настройки
                            this.setSettingsMenu();

                            break;

                        case "Exit":
                            this.soundPoolPlayer.play(
                                    this.soundIDs.get(4),
                                    this.sound_volume,
                                    this.sound_volume,
                                    0,
                                    0,
                                    1.0f
                            );

                            // Сохраняем настройки игры
                            this.saveSettings(this.getContext());

                            mainMenuThread.threadPause(2000);

                            menuActivity.setKilling(true);
                            menuActivity.onClose();

                            break;
                    }
                }
            }

            if (!this.is_main_menu) {
                // Проанализируем все контролы и выполним доступную для них реакцию
                for (GameControl viewControl : this.settingsMenuControls) {
                    switch (viewControl.getClass().getSimpleName()) {
                        case "GameButton":
                            GameButton Button = (GameButton) viewControl;

                            if (Button.isPressed(event)) {
                                switch (Button.getReaction()) {
                                    case "Cancel_Settings":
                                        this.soundPoolPlayer.play(
                                                this.soundIDs.get(2),
                                                this.sound_volume,
                                                this.sound_volume,
                                                0,
                                                0,
                                                1.0f
                                        );

                                        // Закрываем настройки, сбросив их
                                        this.loadSettings(this.getContext());
                                        this.setMainMenu();

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
                                switch (touchedButton.getReaction()) {
                                    case "Accept_Settings":
                                        this.soundPoolPlayer.play(
                                                this.soundIDs.get(2),
                                                this.sound_volume,
                                                this.sound_volume,
                                                0,
                                                0,
                                                1.0f
                                        );

                                        // TODO
                                        this.saveSettings(this.getContext());

                                        this.setMainMenu();

                                        break;

                                    case "Default_Settings":
                                        this.soundPoolPlayer.play(
                                                this.soundIDs.get(4),
                                                this.sound_volume,
                                                this.sound_volume,
                                                0,
                                                0,
                                                1.0f
                                        );

                                        // TODO
                                        this.setDefaultSettings();

                                        break;
                                }
                            }

                            break;
                    }
                }
            }
        }
    }


    // Сброс настроек игры до дэфолтных
    private void setDefaultSettings() {
        Log.i(TAG, "Setting default options...");
        this.sound_volume = 1.0f;
        this.music_volume = 1.0f;
        this.is_sound_off = false;
        this.is_music_off = false;
    }


    // Сохранение настроек игры в JSON
    private void saveSettings(Context context) {
        // Сохраним конфиг в виде JSON
        JSONObject jsonConfig = new JSONObject();

        try {
            jsonConfig.put("sound_vol", String.valueOf(this.sound_volume));
            jsonConfig.put("music_vol", String.valueOf(this.music_volume));
            jsonConfig.put("is_sound_off", this.is_sound_off);
            jsonConfig.put("is_music_off", this.is_music_off);

        } catch (JSONException e) {
            Log.e(TAG, "JSON creating error");
            e.printStackTrace();
        }

        try {
            Log.i(TAG, "Writing config...");
            BufferedWriter confWriter = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(this.settingFileName, MODE_PRIVATE)
            ));

            // пишем данные
            confWriter.write(String.valueOf(jsonConfig));

            // закрываем поток
            confWriter.close();

        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found");
            e.printStackTrace();

        } catch (IOException e) {
            Log.e(TAG, "File writing error");
            e.printStackTrace();
        }
    }


    // Загрузка настроек игры
    // Если настройки ниразу не были сохранены, создаётся новый файл с текущими настройками View
    private void loadSettings(Context context) {
        StringBuilder readBuffer = new StringBuilder();

        try {
            // Открываем поток для чтения
            Log.i(TAG, "Reading config...");
            Log.d(TAG, "File path: " + context.getFilesDir());
            BufferedReader confReader = new BufferedReader(new InputStreamReader(
                    context.openFileInput(this.settingFileName)
            ));

            // Читаем построчно
            String tempLine;
            while ((tempLine = confReader.readLine()) != null)
                readBuffer.append(tempLine);

        } catch (FileNotFoundException e) {
            Log.w(TAG, "Settings has never saved. Creating settings file...");
            this.saveSettings(context);

        } catch (IOException e) {
            Log.e(TAG, "File reading error");
            e.printStackTrace();
        }

        // Данные парсим как JSON
        JSONObject jsonConfig;

        Log.d(TAG, "Settings: ");
        Log.d(TAG, readBuffer.toString());

        try {
            jsonConfig = new JSONObject(readBuffer.toString());

            // Считываем данные в нужном формате
            this.sound_volume = (float) jsonConfig.getDouble("sound_vol");
            this.music_volume = (float) jsonConfig.getDouble("music_vol");
            this.is_sound_off = jsonConfig.getBoolean("is_sound_off");
            this.is_music_off = jsonConfig.getBoolean("is_music_off");
        } catch (JSONException e) {
            Log.e(TAG, "JSON reading error");
            e.printStackTrace();
        }
    }


    private void setMainMenu() {
        this.is_main_menu = true;
    }


    private void setSettingsMenu() {
        this.is_main_menu = false;
    }
}
