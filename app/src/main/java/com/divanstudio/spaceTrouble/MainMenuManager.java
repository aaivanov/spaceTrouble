package com.divanstudio.spaceTrouble;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by WJ_DDA on 08.02.2017.
 */
public class MainMenuManager extends Thread {
    /** Наша скорость в мс = 10*/
    private static final long FPS = 10;

    /** Объект класса GameView*/
    private MainMenuView menuView;

    /** Задаем состояние потока*/
    private boolean running = false;
    private boolean waiting = false;

    private static final String TAG = MainMenuManager.class.getSimpleName();

    public MainMenuManager(MainMenuView view ) {
        this.menuView = view;
    }


    /** Задание состояния потока*/
    public void setRunning( boolean run ) {
        this.running = run;
    }


    public void setWaiting( boolean waiting ) {
        this.waiting = waiting;
    }


    /** Действия, выполняемые в потоке */
    @Override
    public void run() {
        // До начала выполнения кода проверим в ожидании ли тред
        synchronized (this.menuView.getHolder()) {
            while (this.waiting) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, String.valueOf(e));
                    e.printStackTrace();
                }
            }
        }

        long ticksPS = 1000 / FPS;

        final int MAX_FPS = 30;                // желательный fps
        final int MAX_FRAME_SKIPS = 5;         // максимальное число пропускаемых кадров
        final int FRAME_PERIOD = 1000 / MAX_FPS; // период кадра


        long startTime;            // время начала цикла
        long timeDiff;             // время выполнения шага цикла
        int sleepTime;             // сколько мс можно спать (<0 если выполнение опаздывает)
        int framesSkipped;         // число кадров с невыполненной операцией прорисовки

        // Цикл кадра игры
        Canvas canvas = null;
        sleepTime = 0;
        while (this.running) {
            try {
                startTime = System.currentTimeMillis();

                if (this.running && this.menuView.getHolder().getSurface().isValid()){
                    canvas = this.menuView.getHolder().lockCanvas();
                }

                synchronized ( this.menuView.getHolder() ) {
                    // Считаем старт цикла системным методом currentTimeMillis()
                    startTime = System.currentTimeMillis();

                    // Обнуляем счетчик пропущенных кадров
                    framesSkipped = 0;

                    // Обновляем состояние игры...
//                    view.gamePanel.update();

                    // ... и формируем кадр для вывода на экран
                    // Вызываем метод для рисования
                    this.menuView.drawView(canvas);

                    // Вычисляем время, которое прошло с момента запуска цикла
                    timeDiff = System.currentTimeMillis() - startTime;
//                    Log.d(TAG,"Got a diff time " + timeDiff + "ms");

                    // Вычисляем время, которое можно спать
                    sleepTime = (int)(FRAME_PERIOD - timeDiff); // (int)  не самая  лучшая идея.

                    // Если sleepTime > 0 все хорошо, мы идем с опережением
                    if(sleepTime > 0){
                        try{
                            // Отправляем поток в сон на период sleepTime
//                            Log.d(TAG,"We will sleep " + sleepTime + "ms");
                            Thread.sleep(sleepTime);
                        }
                        catch(InterruptedException e){
                            try {
                                Log.e(TAG, String.valueOf(e));
                                throw e;
                            } catch (InterruptedException e1) {
                                Log.e(TAG, String.valueOf(e1));
                                e1.printStackTrace();
                            }
                        }
                    }

                    // Если sleepTime < 0 нам нужно обновлять игровую
                    // ситуацию и не тратить время на вывод кадра.
                    while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS){
//                        view.gamePanel.update();

                        // Добавляем смещение FRAME_PERIOD, чтобы получить
                        // время границы следующего кадра
                        sleepTime += FRAME_PERIOD;  //TODO: надо придумть как сделать чтоб скорость прорисовки была одинаковой
                        framesSkipped++;
                    }
                }
            } finally {
                if (canvas != null) {
                    this.menuView.getHolder().unlockCanvasAndPost( canvas );
                }
            }
//            sleepTime = ticksPS - ( System.currentTimeMillis() - startTime );
//            try {
//                if ( sleepTime > 0 )
//                    sleep( sleepTime );
//                else
//                    sleep( 10 );
//            } catch ( Exception e ) {}
        }
    }

    public void threadPause(long time) {
        Log.d(TAG, "Sleeping thread on " + time + "ms...");
        this.setRunning(false);
        try {
            Thread.sleep(time);
        }
        catch(InterruptedException e) {
            Log.e(TAG,"Sleeping thread error");
            e.printStackTrace();
        }
        this.setRunning(true);
        Log.d(TAG, "Thread sleep ends");
    }


    public void threadStop() {
        Log.i(TAG, "Stopping Thread...");

        this.setRunning(false);

        boolean retry = true;
        while (retry) {
            try {
                this.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, "Stopping thread error");
                e.printStackTrace();
            }
        }

        Log.i(TAG, "Thread successfully stopped");
    }
}
