package com.divanstudio.spaceTrouble;

import android.app.Activity;
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
    private static final String TAG = MainMenuActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Создаём запрос операции с активностью
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Берём окно-форму нашей активности. Ставим ему "На весь экран"
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Непонятно что это и зачем
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Создаём отображение холста в активности
        this.mainMenuView = new MainMenuView(this);
        setContentView(mainMenuView);

        // Назначаем на холст обработчик касаний с Активности MainMenuActivity.
        this.mainMenuView.setOnTouchListener(this);

        Log.i(TAG,"Main menu activity created.");
    }


    @Override
    protected void onDestroy(){
        Log.i(TAG,"Main menu activity destroying...");
        super.onDestroy();
    }


    @Override
    protected void onStop(){
        Log.i(TAG,"Main menu activity stopping...");
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
}

