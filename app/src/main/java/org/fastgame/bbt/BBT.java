package org.fastgame.bbt;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * @Author MrLi
 * @Since 08/20/2016
 */
public class BBT extends Application {

    private static Context mContext;
    private static Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        if (mContext == null) {
            return getAppContext();
        }

        return mContext;
    }

    public static void runOnMainThread(Runnable runnable) {
        runOnMainThread(runnable, 0L);
    }

    public static void runOnMainThread(Runnable runnable, long delay) {
        if (runnable == null) {
            return;
        }
        if (mHandler == null) {
            mHandler = new Handler();
        }

        if (delay > 0) {
            mHandler.postDelayed(runnable, delay);
        } else {
            mHandler.post(runnable);
        }
    }
}
