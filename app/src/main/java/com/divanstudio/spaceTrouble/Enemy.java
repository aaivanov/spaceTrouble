package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.Random;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemy extends Sprite {
    private static final int BMP_ROWS = globalBitmapInfo.getInstance().ENEMY_BMP_ROWS;
    private static final int BMP_COLUMNS = globalBitmapInfo.getInstance().ENEMY_BMP_COLUMNS;
    private static final int IMG_SIZE_COEFFICIENT = globalBitmapInfo.getInstance().ENEMY_IMG_SIZE_COEFFICIENT;

    private Player player;
    private double pSpeed;
    private StateManager state;

    private int xSpeed = 0;
    private int ySpeed = 0;

    private int canvX;  //convas coords
    private int canvY;  //convas coords

    private Random rnd;

    private String collisionSFX = "";
    private MediaPlaylist MediaSFX = new MediaPlaylist();

    public Enemy (mainView gameView, Bitmap origBmp, int frameCount, String collisionSFX, int sound_res_id) {
        super(gameView, origBmp, frameCount, IMG_SIZE_COEFFICIENT, BMP_ROWS, BMP_COLUMNS);
        this.rnd = new Random();
        this.player = Player.getInstance();
        this.canvX = -renderWidth;
        this.pSpeed = player.getPlayerSpeed();
        this.state = StateManager.getInstance();
        this.collisionSFX = collisionSFX;
        this.MediaSFX.addMedia(gameView.getContext(), this.collisionSFX, sound_res_id);
    }

    private void update()
    {
        if (canvX <= -renderWidth ) {
            canvX = gameView.getWidth();
            //canvY = rnd.nextInt(gameView.getHeight() - renderHeight);
            canvY = 6*rnd.nextInt(gameView.getHeight()) - gameView.getHeight()*3;
            xSpeed = -(rnd.nextInt(3) + 1) * gameView.getHeight() / 100 ;
        }
        canvX = canvX + xSpeed;
        canvY = canvY + ySpeed;
        moveStop ();
    }

    public boolean isCollision () {
        return player.checkCollision(getHitBox(), getCenterPoint(), getMaxDetectLenght() );
    }

    public void onDraw(Canvas canvas) {
        if(state.getState() != StateManager.States.PAUSE) {
            update();
        }
        super.onDraw(canvas, canvX, canvY);
    }

    public void moveUp () { ySpeed = -(int)pSpeed; }

    public void moveDown () { ySpeed = (int)pSpeed; }

    public void moveStop () { ySpeed = 0; }

    public void playSound () { this.MediaSFX.play(this.collisionSFX); }
}
