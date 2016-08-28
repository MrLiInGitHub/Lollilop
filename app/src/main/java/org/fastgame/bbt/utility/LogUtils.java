package org.fastgame.bbt.utility;

import android.text.TextUtils;
import android.util.Log;

import org.fastgame.bbt.constant.SysConstant;

/**
 * @Author MrLi
 * @Since 08/23/2016
 */
public final class LogUtils {

    private static final String TAG = LogUtils.class.getSimpleName();
    private static final int MAX_LOG_SIZE = 1000;

    private LogUtils() {}

    public static void info(String tag, String msg) {
        if (!SysConstant.DEBUG) {
            return;
        }
        if (!TextUtils.isEmpty(tag)) {
            Log.i(tag, msg);
        } else {
            Log.i(TAG, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (!SysConstant.DEBUG) {
            return;
        }
        if (!TextUtils.isEmpty(tag)) {
            Log.d(tag, msg);
        } else {
            Log.d(TAG, msg);
        }
    }

    public static void error(String tag, String msg) {
        if (!SysConstant.DEBUG) {
            return;
        }
        if (!TextUtils.isEmpty(tag)) {
            Log.e(tag, msg);
        } else {
            Log.e(TAG, msg);
        }
    }

    public static void error(String tag, String msg, Throwable tr) {
        if (!TextUtils.isEmpty(tag)) {
            Log.e(tag, msg, tr);
        } else {
            Log.e(TAG, msg, tr);
        }
    }

    @SuppressWarnings("unused")
    private static void i(String tag, String msg) {
        for (int i = 0; i < msg.length() / MAX_LOG_SIZE + 1; i++) {
            int start = i * MAX_LOG_SIZE;
            int end = (i + 1) * MAX_LOG_SIZE;
            end = end > msg.length() ? msg.length() : end;
            Log.i(tag, msg.substring(start, end));
        }
    }
}
