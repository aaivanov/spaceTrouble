package com.divanstudio.firsttry.ST;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.divanstudio.firsttry.R;

/**
 * Created by WJ_DDA on 13.09.2016.
 */
//TODO: main game View
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
//    private SurfaceHolder holder;                       // Удерживающий картинку игры интерфейс
    private MainThread thread;                            // Главный тред с главным циклом игры

    //private Controls controls;                            // Управление
    //private ControlMenu GameplayControls;                 // Управление игрой

    // TODO WJ DDA ADDs
    private GameMenu MainGameMenu;                        // Главное меню во время игры

    private Enemies meteors;                              // Враги
    private Player player;                                // Игрок
    private Background background;                        // Картинка задника
    // TODO перенести в другой тред
    private MediaPlayer musicPlayer;                      // Проигрыватель музыки
    private MediaPlayer soundPlayer;                      // Проигрыватель зв. эффектов
    private int sBackground;                              // ?

    private static final String TAG = MainGamePanel.class.getSimpleName();

    public MainGamePanel(Context context) {
        super(context);

        // TODO написано вместо колдунства ниже
        // Устанавливает текущий класс (MainGamePanel) как обработчик событий от поверхности.
        getHolder().addCallback(this);

        // создаем поток для игрового цикла до назначения фокуса
        // getHolder() стандартный метод SurfaceView
        thread = new MainThread(this);
        player = Player.getInstance();

//        holder = getHolder();

        musicPlayer = MediaPlayer.create(context, R.raw.asteroids);
        musicPlayer.setLooping(true);
        musicPlayer.setVolume(100, 100);

        Log.i(TAG,"Media player added");

        // TODO Что это за колдунство?
//        this.holder.addCallback (new SurfaceHolder.Callback() {
//            public void surfaceDestroyed (SurfaceHolder holder) {
//                boolean retry = true;
//                gameLoopThread.setRunning (false);
//                while (retry) {
//                    try {
//                        gameLoopThread.join();
//                        retry = false;
//                    } catch (InterruptedException e) {}
//                }
//                musicPlayer.stop();
//                musicPlayer.release();
//            }
//
//            public void surfaceCreated(SurfaceHolder holder) {
//                gameLoopThread.setRunning(true);
//                gameLoopThread.start();
//                initMainViewRes();
//                musicPlayer.start();
//            }
//
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//        });
        // делаем GamePanel focusable, чтобы она могла обрабатывать сообщения
        setFocusable(true);

        Log.i(TAG,"Game panel added.");
    }

    // Просто инициализация всех объектов в игре
    public void initMainViewRes() {
        player.setPlayerData(this, BitmapFactory.decodeResource(getResources(), R.drawable.player));

        Log.i(TAG,"Player data set");

        meteors = new Enemies(
                this,
                BitmapFactory.decodeResource(getResources(), R.drawable.cut_map_pixelize)
        );

        Log.i(TAG,"Meteors data set");

        // Рисует все кнопки управления игрой
//        controls = new Controls(
//                this,
//                BitmapFactory.decodeResource(getResources(), R.drawable.arrows ),
//                meteors
//        );

        background = new Background(
                this,
                BitmapFactory.decodeResource(getResources(), R.drawable.bckgrnd_1280_720_pixelize)
        );

        Log.i(TAG,"Background data set");

        // TODO WJ DDA ADDs
        // Добавим стандартное меню с кнопками
        // TODO после сделать вменяемое меню
        this.MainGameMenu = new GameMenu(this);

        Log.i(TAG,"Main game menu set");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Объявляем наш тред запущенным
        thread.setRunning(true);

        // Запускаем тред
        thread.start();
        Log.i(TAG,"Main thread started");

        // Инициализация рисунков и вида игры?
        initMainViewRes();
        Log.i(TAG,"Init the main view resolution");

        // Инициализация музыки
        musicPlayer.start();
        Log.i(TAG,"Music starts");

        // TODO Инициализация озвучки
//        if (musicPlayer.start() != True){}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //посылаем потоку команду на закрытие и дожидаемся, пока поток не будет закрыт.
        boolean retry = true;

        // TODO Сие перенести в функцию реализации кнопок интерфейса
        thread.setRunning (false);

        Log.i(TAG,"Main thread stopped");

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // пытаемся снова остановить поток thread
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (background != null) background.onDraw(canvas);
        if (meteors != null) meteors.onDraw(canvas);
        if (player != null) player.onDraw(canvas);
//        if (controls != null) controls.onDraw(canvas);

        // TODO WJ DDA ADDs:
        // Если мы задали главное меню игры, то рисуем его
        if (MainGameMenu != null) MainGameMenu.onDraw(canvas);
    }

    // TODO Доработать
//    public void setTouchEvent(MotionEvent event) {
//        synchronized (getHolder()) {
//            controls.isCollision(event);
//        }
//    }

    // Изменяем состояния объектов игры
    // TODO Что-то нужно сделать тут
    public void update(){
    }
}
