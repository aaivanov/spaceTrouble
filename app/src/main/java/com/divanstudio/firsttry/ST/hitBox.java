package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by RUINIVAN on 17.12.2015.
 */
public class hitBox {
    private ArrayList<Point> apexListOnScrn = new ArrayList<Point>();
    private ArrayList<Point> apexList = new ArrayList<Point>();
    private int apexShift = 10;
    Paint paint = new Paint();

    public hitBox ( int renderWidth, int renderHeight ) {
        this.apexList.add(new Point(apexShift, apexShift));
        this.apexList.add(new Point(renderWidth - apexShift, apexShift));
        this.apexList.add(new Point(renderWidth - apexShift, renderHeight - apexShift));
        this.apexList.add(new Point(apexShift, renderHeight - apexShift));

        for ( int i = 0; i < apexList.size(); i++ ) {
            apexListOnScrn.add(new Point());
        }

        paint.setColor(Color.RED);
    }

    public hitBox ( Bitmap img , int renderHeight, int frameCount, int srcHeight )  {
        float crop = (float) srcHeight / renderHeight ;
        int step = Math.round( crop ) * 3;
        int ph = srcHeight * frameCount + step;

        while( srcHeight * frameCount < ph && ph < srcHeight * ( frameCount + 1 ) ) {
                for ( int pw = 0; pw < img.getWidth(); pw += step ) {
                    if ( Color.alpha(img.getPixel(pw, ph)) != 0 ) {
                        this.apexList.add(new Point(Math.round(pw/crop), Math.round((ph - srcHeight * frameCount)/crop)));
                        ph += step;
                        break;
                    }
                }
                ph += step;
        }
        ph -= step;
        if (ph >= srcHeight * ( frameCount + 1 )) {
            ph = srcHeight * ( frameCount + 1 ) - 1;
        }
        while( srcHeight * frameCount < ph && ph < srcHeight * ( frameCount + 1 ) ) {
                for ( int pw = img.getWidth() - 1 ; pw > 0; pw -= step ) {
                    if ( Color.alpha(img.getPixel(pw, ph)) != 0 ) {
                        this.apexList.add(new Point(Math.round(pw/crop), Math.round((ph - srcHeight * frameCount)/crop)));
                        ph -= step;
                        break;
                    }
                }
                ph -= step;
        }

        for ( int i = 0; i < apexList.size(); i++ ) {
            apexListOnScrn.add(new Point());
        }

        paint.setColor(Color.GREEN);
    }

    public ArrayList<Point> getHitBox () {
        return apexListOnScrn;
    }

    public void updateHitBox (int x, int y){
        for (int i = 0; i < apexList.size(); i++) {
            Point point = apexList.get(i);
            apexListOnScrn.set(i, new Point(point.x + x, point.y + y));
        }
    }

    public void onDraw(Canvas canvas) {
        for ( int i = 0; i < apexListOnScrn.size(); i++ ) {
            Point point = apexListOnScrn.get(i);
            if (i == apexListOnScrn.size() - 1 ){
                canvas.drawLine(apexListOnScrn.get(i).x, apexListOnScrn.get(i).y, apexListOnScrn.get(0).x, apexListOnScrn.get(0).y, paint);
            } else {
                canvas.drawLine(apexListOnScrn.get(i).x, apexListOnScrn.get(i).y, apexListOnScrn.get(i+1).x, apexListOnScrn.get(i+1).y, paint);
            }
        }
    }

    public boolean checkCollision (ArrayList<Point> hitBox) {
        Point a;
        Point b;
        boolean isCollision = false;

        //TODO Некорректный возврат значения функции. Заменить на true|false.
        //TODO Либо сделать адекватную обработку isCollision
        //OLD
//        for (int j = 0; j < hitBox.size(); j++) {
//            if ( j == hitBox.size() - 1 ) {
//                a = hitBox.get(j);
//                b = hitBox.get(0);
//            } else {
//                a = hitBox.get(j);
//                b = hitBox.get(j + 1);
//            }
//            for (int i = 0; i < apexListOnScrn.size(); i++) {
//                if ( i == apexListOnScrn.size() - 1 ) {
//                    isCollision = check(apexListOnScrn.get(i), apexListOnScrn.get(0), a, b);
//                } else {
//                    isCollision = check(apexListOnScrn.get(i), apexListOnScrn.get(i + 1), a, b);
//                }
//                if (isCollision) {
//                    return isCollision;
//                }
//            }
//        }
//        return isCollision;
        //NEW
        for (int j = 0; j < hitBox.size(); j++) {
            if ( j == hitBox.size() - 1 ) {
                a = hitBox.get(j);
                b = hitBox.get(0);
            } else {
                a = hitBox.get(j);
                b = hitBox.get(j + 1);
            }
            for (int i = 0; i < apexListOnScrn.size(); i++) {
                if ( i == apexListOnScrn.size() - 1 ) {
                    isCollision = check(apexListOnScrn.get(i), apexListOnScrn.get(0), a, b);
                } else {
                    isCollision = check(apexListOnScrn.get(i), apexListOnScrn.get(i + 1), a, b);
                }

                // Выход из цикла по i
                if (isCollision) {
                    break;
                }
            }

            // Выход из цикла по j
            if (isCollision) {
                break;
            }
        }

        // Возвращаем результат
        return isCollision;
    }

    /*public int check(Point a, Point b, Point middle) {
        long ax = a.x - middle.x;
        long ay = a.y - middle.y;
        long bx = b.x - middle.x;
        long by = b.y - middle.y;
        int s = Long.signum(ax * by - ay * bx);
        if (s == 0 && (ay == 0 || by == 0) && ax * bx <= 0)
            return 0;
        if (ay < 0 ^ by < 0)
        {
            if (by < 0)
                return s;
            return -s;
        }
        return 1;
    }*/

    public boolean check(Point a1n, Point a2n, Point b1n, Point b2n) {
        Point a1, a2, b1;
        a1 = new Point(a1n.x - b2n.x, a1n.y - b2n.y);
        a2 = new Point(a2n.x - b2n.x, a2n.y - b2n.y);
        b1 = new Point(b1n.x - b2n.x, b1n.y - b2n.y);

        /*long v1 = (b2.x-b1.x)*(a1.y-b1.y)-(b2.y-b1.y)*(a1.x-b1.x);
          long v2 = (b2.x-b1.x)*(a2.y-b1.y)-(b2.y-b1.y)*(a2.x-b1.x);
          long v3 = (a2.x-a1.x)*(b1.y-a1.y)-(a2.y-a1.y)*(b1.x-a1.x);
          long v4 = (a2.x-a1.x)*(b2.y-a1.y)-(a2.y-a1.y)*(b2.x-a1.x);*/

        long v1 = b1.y * a1.x - b1.x * a1.y;
        long v2 = b1.y * a2.x - b1.x * a2.y;
        long v3 = (a2.x - a1.x) * (b1.y - a1.y) - (a2.y - a1.y) * (b1.x - a1.x);
        long v4 = a2.y * a1.x - a2.x * a1.y;

        //TODO можно просто вернуть это выражение
//        if ( v1*v2 < 0 && v3*v4 < 0) {
//            return true;
//        }
//
//        return false;
        return v1 * v2 < 0 && v3 * v4 < 0;
    }
}
