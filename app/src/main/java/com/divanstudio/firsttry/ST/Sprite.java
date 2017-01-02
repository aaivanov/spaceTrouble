package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by RUINIVAN on 17.12.2015.
 */
public class Sprite {
//    private int BMP_ROWS;
//    private int BMP_COLUMNS;
    Rect srcImgRect;
//    mainView gameView;

    //TODO WJ
    MainGamePanel gamePanel;

    Bitmap origBmp;
    int renderWidth;
    int renderHeight;
    hitBox sHitBox;

    public Sprite (){};

//    public Sprite( mainView gameView, Bitmap origBmp, int frameCount, int imgScaleCoef, int BMP_ROWS, int BMP_COLUMNS) {
//        setSpriteData( gameView, origBmp, frameCount, imgScaleCoef, BMP_ROWS, BMP_COLUMNS );
//    }

    //TODO WJ
    public Sprite(MainGamePanel gamePanel, Bitmap origBmp, int frameCount, int imgScaleCoef, int BMP_ROWS, int BMP_COLUMNS) {
        setSpriteData(gamePanel, origBmp, frameCount, imgScaleCoef, BMP_ROWS, BMP_COLUMNS);
    }

//    public void setSpriteData ( mainView gameView, Bitmap origBmp, int frameCount, int imgScaleCoef, int BMP_ROWS, int BMP_COLUMNS ) {
//        //    this.BMP_ROWS = BMP_ROWS;
//        //    this.BMP_COLUMNS = BMP_COLUMNS;
//
//        this.gameView = gameView;
//        this.origBmp = origBmp;
//
//        int srcWidth = origBmp.getWidth() / BMP_COLUMNS;
//        int srcHeight  = origBmp.getHeight() / BMP_ROWS;
//        int screenHeight  = this.gameView.getHeight();
//
//        this.srcImgRect = new Rect(0, srcHeight * frameCount, srcWidth, srcHeight * ( frameCount + 1));
//
//        this.renderHeight = (int) ( screenHeight * imgScaleCoef / 100 );
//        this.renderWidth = (int) this.renderHeight * srcWidth / srcHeight;
//        //this.sHitBox = new hitBox(this.renderWidth, this.renderHeight );
//        sHitBox = new hitBox( this.origBmp, this.renderHeight, frameCount, srcHeight );
//    }

    //TODO WJ
    public void setSpriteData (
            MainGamePanel gamePanel,
            Bitmap origBmp,
            int frameCount,
            int imgScaleCoef,
            int BMP_ROWS,
            int BMP_COLUMNS
    ) {
        //    this.BMP_ROWS = BMP_ROWS;
        //    this.BMP_COLUMNS = BMP_COLUMNS;

        this.gamePanel = gamePanel;
        this.origBmp = origBmp;

        int srcWidth = origBmp.getWidth() / BMP_COLUMNS;
        int srcHeight  = origBmp.getHeight() / BMP_ROWS;
        int screenHeight  = this.gamePanel.getHeight();

        this.srcImgRect = new Rect(0, srcHeight * frameCount, srcWidth, srcHeight * ( frameCount + 1));

        //TODO Указание типа тут излишне
        this.renderHeight = (int) ( screenHeight * imgScaleCoef / 100 );
        this.renderWidth = (int) this.renderHeight * srcWidth / srcHeight;
        //this.sHitBox = new hitBox(this.renderWidth, this.renderHeight );
        sHitBox = new hitBox( this.origBmp, this.renderHeight, frameCount, srcHeight );
    }

    public boolean checkCollision (ArrayList<Point> hitBox) {
       return sHitBox.checkCollision(hitBox);
    }

    public ArrayList<Point> getHitBox () {
        return sHitBox.getHitBox();
    }

    public void onDraw(Canvas canvas, int x, int y) {
        sHitBox.updateHitBox(x, y);
        Rect dst = new Rect(x, y, x + this.renderWidth, y + this.renderHeight);
        canvas.drawBitmap(this.origBmp, this.srcImgRect, dst, null);
        sHitBox.onDraw( canvas );
    }
}
