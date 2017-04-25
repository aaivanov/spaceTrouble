package com.divanstudio.spaceTrouble;

/**
 * Created by aaivanov on 11/29/15.
 */
import android.graphics.Canvas;
import android.util.Log;

public class mainManager extends Thread {
    /** Наша скорость в мс = 10*/
    static final long FPS = 10;

    /** Объект класса GameView*/
    private mainView view;

    /** Задаем состояние потока*/
    private boolean running = false;

    /** Конструктор класса*/
    public mainManager( mainView view ) {
        this.view = view;
    }

    /** Задание состояния потока*/
    public void setRunning( boolean run ) {
        running = run;
    }

    private static final String TAG = mainManager.class.getSimpleName();

    /** Действия, выполняемые в потоке */
    @Override
    public void run() {
        long ticksPS = 1000 / FPS;

        final int MAX_FPS = 30;                // желательный fps
        final int MAX_FRAME_SKIPS = 5;         // максимальное число пропускаемых кадров
        final int FRAME_PERIOD = 1000 / MAX_FPS; // период кадра


        long startTime;            // время начала цикла
        long timeDiff;             // время выполнения шага цикла
        int sleepTime;             // сколько мс можно спать (<0 если выполнение опаздывает)
        int framesSkipped;         // число кадров с невыполненной операцией прорисовки

        // Цикл кадра игры
        sleepTime = 0;

        while ( running ) {
            Canvas canvas = null;
            startTime = System.currentTimeMillis();
            try {
                canvas = view.getHolder().lockCanvas();
                synchronized ( view.getHolder() ) {
                    // Считаем старт цикла системным методом currentTimeMillis()
                    startTime = System.currentTimeMillis();
                    // Обнуляем счетчик пропущенных кадров
                    framesSkipped = 0;

                    // Обновляем состояние игры...
//                    view.gamePanel.update();

                    // ... и формироваться кадр для вывода на экран
                    // Вызываем метод для рисования
                    this.view.onDraw(canvas);

                    // Вызываем метод для проигрывания музыки
                    this.view.playMusic();

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
                                throw e;
                            } catch (InterruptedException e1) {
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
                    view.getHolder().unlockCanvasAndPost( canvas );
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

    public void kill() {
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
