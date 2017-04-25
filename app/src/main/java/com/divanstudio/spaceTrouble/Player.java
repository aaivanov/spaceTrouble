package com.divanstudio.spaceTrouble;

/**
 * Created by aaivanov on 12/5/15.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Player extends Sprite{
    private static volatile Player instance;
    private StateManager state;

    private static final int SPEED_COEFFICIENT = globalBitmapInfo.getInstance().PLAYER_SPEED_COEFFICIENT;
    private static final int IMG_SIZE_COEFFICIENT = globalBitmapInfo.getInstance().PLAYER_IMG_SIZE_COEFFICIENT;
    private static final int BMP_ROWS = globalBitmapInfo.getInstance().PLAYER_BMP_ROWS;
    private static final int BMP_COLUMNS = globalBitmapInfo.getInstance().PLAYER_BMP_COLUMNS;

    private int x = 0;
    private int y = 0;

    private double ySpeed = 0;
    private double pSpeed;

    // TODO Может нужно добавить состояние игрока, при котором он будет играть звуки
    private String ambSFX = "";
    private MediaPlaylist AmbPlaylist = new MediaPlaylist();

    private Player () {
        super();
        this.state = StateManager.getInstance();
    }

    public static Player getInstance() {
        Player localInstance = instance;
        if (localInstance == null) {
            synchronized (Player.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Player();
                }
            }
        }
        return  localInstance;
    }

    public void setPlayerData (mainView gameView, Bitmap origBmp, String ambienceSFX, int sound_res_id) {
        super.setSpriteData(gameView, origBmp, 0, IMG_SIZE_COEFFICIENT, BMP_ROWS, BMP_COLUMNS);
        this.x = gameView.getWidth() / 3;
        this.y = ( gameView.getHeight() - renderHeight) / 2;
        this.pSpeed = gameView.getHeight() / 100 * SPEED_COEFFICIENT;

        this.ambSFX = ambienceSFX;
        this.AmbPlaylist.addMedia(gameView.getContext(), this.ambSFX, sound_res_id);
    }


    private void update() {
        if (y >= gameView.getHeight() - renderHeight - ySpeed || y + ySpeed < 0) {
            ySpeed = 0;
        }

        y = y + (int) ySpeed;
    }

    public void onDraw(Canvas canvas) {
        if(state.getState() == StateManager.States.PLAY) {
            update();
            super.onDraw(canvas, x, y);

            // Если рисуется спрайт, то он звучит
            this.playAmbienceSound();
        } else {
            ySpeed = 0;

            // Если не рисуется, то молчит
            this.stopAmbienceSound();
        }
    }

    public double getPlayerSpeed () {
        return this.pSpeed;
    }

    public void moveUp () { ySpeed = pSpeed; }

    public void moveDown () { ySpeed = -pSpeed; }

    public void moveStop () { ySpeed = 0; }

    public boolean isMove () {
        if (ySpeed == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void playAmbienceSound () { this.AmbPlaylist.play(this.ambSFX, false, true); }

    public void stopAmbienceSound () { this.AmbPlaylist.stop(this.ambSFX); }
}
