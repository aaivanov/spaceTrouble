package com.divanstudio.spaceTrouble;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.divanstudio.firsttry.R;

/**
 * Created by aaivanov on 11/29/15.
 */

public class mainView extends SurfaceView  {
    private SurfaceHolder holder;
    private mainManager gameLoopThread;

    private Controls controls;
    private Enemies meteors;
    private Player player;
    private Background background;

    private StateManager State;
    private MediaPlaylist MusicPlaylist = new MediaPlaylist();

    private int sBackground;

    public mainView(Context context) {
        super(context);
        gameLoopThread = new mainManager (this);
        player = Player.getInstance();
        holder = getHolder();

        this.State = StateManager.getInstance();

        this.holder.addCallback (new SurfaceHolder.Callback() {
            public void surfaceDestroyed (SurfaceHolder holder) {
                // Убиваем тред
                gameLoopThread.kill();

                // Удаляем менеджер
                MusicPlaylist.clear();
            }

            public void surfaceCreated(SurfaceHolder holder) {
                initMainViewRes();

                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    public void initMainViewRes() {
        // Инициализация музыки
        this.MusicPlaylist.addMedia(this.getContext(), "main_menu", R.raw.main_menu);
        this.MusicPlaylist.addMedia(this.getContext(), "asteroids", R.raw.asteroids);

        // Инициализация игрока
        player.setPlayerData(this, BitmapFactory.decodeResource(getResources(), R.drawable.player), "ship_engine", R.raw.ship_eng_idling_big_loop);

        // Инициализация вражеских объектов
        meteors = new Enemies(this, BitmapFactory.decodeResource(getResources(), R.drawable.cut_map_pixelize));

        // Инициализация контролов
        controls = new Controls(this, BitmapFactory.decodeResource(getResources(), R.drawable.arrows ), meteors);

        // Инициализация бэкграунда
        background = new Background(this, BitmapFactory.decodeResource(getResources(), R.drawable.bckgrnd_1280_720_pixelize));
    }

    protected void onDraw(Canvas canvas) {
        if (background != null) background.onDraw(canvas);
        if (meteors != null) meteors.onDraw(canvas);
        if (player != null) player.onDraw(canvas);
        if (controls != null) controls.onDraw(canvas);
    }

    protected void playMusic() {
        if (this.State.getState() == StateManager.States.MENU) {
            this.MusicPlaylist.play("main_menu", false, true, true);
        }

        if (this.State.getState() == StateManager.States.PLAY) {
            this.MusicPlaylist.play("asteroids", false, true, true);
        }

        if (this.State.getState() == StateManager.States.GAMEOVER) {
            // TODO this.MusicPlaylist.play("GameOver", false, true, true);
            this.MusicPlaylist.stop();
        }

        if (this.State.getState() == StateManager.States.PAUSE) {
            this.MusicPlaylist.pause();
        }
    }

    public void setTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            controls.checkTapAction(event);
        }
    }
}
