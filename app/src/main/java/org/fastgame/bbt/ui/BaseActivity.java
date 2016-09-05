package org.fastgame.bbt.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.fastgame.bbt.R;

/**
 * Base {@link Activity} for All the {@link Activity}
 *
 * @Author MrLi
 * @Since 08/20/2016
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Make {@link android.app.Activity} fill the screen.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void requestFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
