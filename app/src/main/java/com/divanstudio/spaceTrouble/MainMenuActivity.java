package com.divanstudio.spaceTrouble;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by WJ_DDA on 08.02.2017.
 */

public class MainMenuActivity extends Activity implements OnTouchListener {
    private MainMenuView mainMenuView;

    private boolean killing = false;

    private static final String TAG = MainMenuActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Activity creating...");
        super.onCreate(savedInstanceState);

        // Создаём запрос операции с активностью
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Берём окно-форму нашей активности. Ставим ему "На весь экран"
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Непонятно что это и зачем
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Отображаем контент активности на экран
        this.mainMenuView = new MainMenuView(this);
        this.setContentView(this.mainMenuView);

        // Назначаем на холст обработчик касаний с Активности MainMenuActivity.
        this.mainMenuView.setOnTouchListener(this);
    }


    @Override
    protected void onDestroy(){
        Log.i(TAG,"Activity destroying...");
        super.onDestroy();

        if (this.killing) {
            Log.i(TAG,"Activity send to the deep space...");
            System.exit(0);
        }
    }


    @Override
    protected void onStop(){
        Log.i(TAG,"Activity stopping...");
        super.onStop();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:         // нажатие на тачпад - нажатие на кнопку
                this.mainMenuView.setTouchEvent(this, event);
                break;
        }

        return true;
    }


    public void onClose() {
        Log.i(TAG,"Activity closing...");

        Intent intent = new Intent(this, Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finish();
    }


    public void setKilling(boolean kill) {
        this.killing = kill;
    }
}

