package com.divanstudio.spaceTrouble;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by WJ_DDA on 03.04.2017.
 */

public class SettingsMenuActivity extends Activity implements OnTouchListener {
    private SettingsMenuView settingsMenuView;

    private List<String> Settings = null;

    private static final String TAG = SettingsMenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Activity creating...");
        super.onCreate(savedInstanceState);

        // Создаём запрос операции с активностью
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Берём окно-форму нашей активности. Ставим ему "На весь экран"
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Непонятно что это и зачем
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Создаём отображение холста в активности
        this.settingsMenuView = new SettingsMenuView(this);
        setContentView(settingsMenuView);

        // Назначаем на холст обработчик касаний с Активности SettingsMenuActivity.
        this.settingsMenuView.setOnTouchListener(this);
    }


    @Override
    protected void onDestroy(){
        Log.i(TAG,"Activity destroying...");
        super.onDestroy();
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
                this.settingsMenuView.setTouchEvent(this, event);
                break;
        }

        return true;
    }


    public void settingsSave() {

    }


    public void onClose() {
        Log.i(TAG,"Activity closing...");

        Intent intent = new Intent(this, Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        finish();
    }
}
