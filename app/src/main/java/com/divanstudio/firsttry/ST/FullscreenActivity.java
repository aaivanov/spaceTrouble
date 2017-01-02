package com.divanstudio.firsttry.ST;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
// С этого класса начинается создание приложения
public class FullscreenActivity extends Activity implements OnTouchListener {
//    mainView mv;

    private static final String TAG = FullscreenActivity.class.getSimpleName();

    // TODO WJ
    // хз зачем это тут
    MainGamePanel mainPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mv = new mainView(this);
//        setContentView(mv);
//        mv.setOnTouchListener(this);

        //TODO WJ
        mainPanel = new MainGamePanel(this);
        setContentView(mainPanel);
        mainPanel.setOnTouchListener(this);

        Log.i(TAG,"Main activity created.");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        mv.setTouchEvent(event);

        //TODO WJ Вернуть касание!
//        mainPanel.setTouchEvent(event);
        return true;
    }

    @Override
    protected void onDestroy(){
        Log.i(TAG,"Main activity destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        Log.i(TAG,"Main activity stopping...");
        super.onStop();
    }
}
