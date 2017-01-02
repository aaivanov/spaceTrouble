package com.divanstudio.firsttry.ST;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by WJ_DDA on 13.09.2016.
 */
public class MainThread extends Thread  {
    private final static int MAX_FPS = 30;                // желательный fps
    private final static int MAX_FRAME_SKIPS = 5;         // максимальное число пропускаемых кадров
    private final static int FRAME_PERIOD = 1000 / MAX_FPS; // период кадра

    private final SurfaceHolder surfaceHolder;      // Блокировщик экрана. Чтобы нормально рисовать

    // Этот тред имеет логирование.
    // Переменная TAG это место куда пишется сообщение лога.
    // Если определить эту переменную в каждом классе, то она будет содержать имя класса в логах
    // Это позволяет использовать стандартную утилиту Android Logging Framework
    private static final String TAG = MainThread.class.getSimpleName();

    private MainGamePanel gamePanel;               // Визуализатор игры
    private boolean running = false;               // Статус потока

    /** Конструктор класса*/
    // TODO Названия классов с заглавной!
    public MainThread(MainGamePanel gamePanel) {
        // Вызывает конструктор родителя (супер-класса)
        super();

        // Вот тут создаём Держатель и Панель
        this.surfaceHolder = gamePanel.getHolder();
        this.gamePanel = gamePanel;

        Log.i(TAG,"Main thread added");
    }

    /** Задание состояния потока*/
    public void setRunning(boolean run) {
        running = run;
    }

    /** Действия, выполняемые в потоке */
    // TODO а где onCreate()?
    @Override
    public void run() {
//        long ticksPS = 1000 / FPS;
//        long startTime;
//        long sleepTime;

        long startTime;            // время начала цикла
        long timeDiff;             // время выполнения шага цикла
        int sleepTime;             // сколько мс можно спать (<0 если выполнение опаздывает)
        int framesSkipped;         // число кадров с невыполненной операцией прорисовки

        // Цикл кадра игры
        sleepTime = 0;
        while (running) {
            Canvas canvas = null; // зачем?

            // Пытаемся заблокировать canvas
            // для изменение картинки на поверхности
            try {
                // Связываем холст (canvas) с выводом на экран (view)
                canvas = this.surfaceHolder.lockCanvas();

                // synchronized(Блокировщик) - блокирует объект так, чтобы с ним работал
                // только один тред. Пока блок этой функции не выполнится, все треды неактивны.
                // В частности, любые треды, кроме MainThread, не могут рисовать на холсте
                // 1) Нукжно следить за тем, чтобы метод update() запускался до метода ControlMenu.
                // 2) Методы update() и ControlMenu() должны рисовать картинку, как минимум, со скоростью
                // 25 FPS (40мс).
                // 3) FPS и UPS не должны зависеть от производительности системы,
                // то есть нужно задать фиксированную скорость игры.
                // 4) Если у нас крутой девайс, у нас после выполнения апдейта и рисования
                // остаётся запас времени, пока не кончится игровой цикл.
                // В этот момент лучше всего отключить игру (усыпить), чтобы не тратить ресурсы и
                // сэкономить заряд батареи.
                // 5) Чтобы игра шла лучше на слабых системах, нужно предрасчитывать время на
                // обновление и прорисовку, если времени не хватает, отказываться от операции
                // "прорисовка" (именно от неё)
                synchronized(surfaceHolder) {
                    // Считаем старт цикла системным методом currentTimeMillis()
                    startTime = System.currentTimeMillis();

                    // Обнуляем счетчик пропущенных кадров
                    framesSkipped = 0;

                    // Обновляем состояние игры...
//                    this.gamePanel.update();

                    // ... и формироваться кадр для вывода на экран
                    // Вызываем метод для рисования
                    this.gamePanel.onDraw(canvas);

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
//                        this.gamePanel.update();

                        // Добавляем смещение FRAME_PERIOD, чтобы получить
                        // время границы следующего кадра
                        sleepTime += FRAME_PERIOD;  //TODO: надо придумть как сделать чтоб скорость прорисовки была одинаковой
                        framesSkipped++;
                    }
                }
            } finally {
                // В случае ошибки разблокируем холст
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

//            Log.d(TAG,"Frames are skipped " + framesSkipped + " times");
        }
    }
}