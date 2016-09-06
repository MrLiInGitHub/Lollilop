package org.fastgame.bbt.utility;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;
import org.fastgame.bbt.ui.AdColonyActivity;
import org.fastgame.bbt.ui.AdColonyV4VCActivity;
import org.fastgame.bbt.ui.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @Author MrLi
 * @Since 08/26/2016
 */
public final class ActivityUtils {

    private static final String TAG = ActivityUtils.class.getSimpleName();

    private static final int START_ACTIVITY_DURATION = 300;

    private static boolean isClick;

    /**
     * 安全启动应用程序，截获Exception。
     *
     * @param activity Activity
     * @param intent   Intent
     * @return 是否成功启动Activity。
     */
    public static void startActivitySafely(Activity activity, Intent intent) {
        startActivitySafely(activity, intent, true);
    }

    /**
     * 安全启动应用程序，截获Exception。
     *
     * @param activity activity
     * @param intent   Intent
     * @param newTask  是否添加Intent.FLAG_ACTIVITY_NEW_TASK
     * @return 是否成功启动Activity。
     */
    public static void startActivitySafely(Activity activity, Intent intent, boolean newTask) {
        if (!isClick) {
            isClick = true;
            if (activity == null || intent == null) {
                isClick = false;
                return;
            }
            try {
                if (newTask) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                activity.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(activity, R.string.Related_programs_were_not_found,
                        Toast.LENGTH_SHORT).show();
            } finally {
                delayEnable();
            }
        }
    }

    /**
     * 安全启动应用程序，截获Exception。 必须在主线程被调用
     *
     * @param context context
     * @param intent  Intent
     * @return 是否成功启动Activity。
     */
    public static void startActivitySafely(Context context, Intent intent) {
        if (!isClick) {
            isClick = true;
            if (context == null || intent == null) {
                isClick = false;
                return;
            }
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, R.string.Related_programs_were_not_found,
                        Toast.LENGTH_SHORT).show();
            } finally {
                delayEnable();
            }
        }
    }

    /**
     * Safe version to start an activity for result
     *
     * @param activity
     * @param intent
     * @param requestCode
     * @return 是否成功启动
     */
    public static void startActivityForResultSafely(Activity activity, Intent intent, int requestCode) {
        if (!isClick) {
            isClick = true;

            if (activity == null || intent == null) {
                isClick = false;
                return;
            }
            try {
                activity.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                Toast.makeText(activity, R.string.Related_programs_were_not_found,
                        Toast.LENGTH_SHORT).show();
            } finally {
                delayEnable();
            }
        }
        return;
    }

    /**
     * Safe version to start an activity for result
     *
     * @param fragment
     * @param intent
     * @param requestCode
     * @return 是否成功启动
     */
    public static void startActivityForResultSafely(Fragment fragment, Intent intent, int requestCode) {
        if (!isClick) {
            isClick = true;

            if (fragment == null || intent == null) {
                isClick = false;
                return;
            }
            try {
                fragment.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                if (fragment.getActivity() != null) {
                    Toast.makeText(fragment.getActivity(), R.string.Related_programs_were_not_found, Toast.LENGTH_SHORT).show();
                }
            } finally {
                delayEnable();
            }
        }
    }

    private static void delayEnable() {
        BBT.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                isClick = false;
            }
        }, START_ACTIVITY_DURATION);
    }

    public static void startAdColonyActivityForResult(Activity activity, int requestCode) {
        startActivityForResultSafely(activity, new Intent(activity, AdColonyActivity.class), requestCode);
    }

    public static void startAdColonyV4VCActivityForResult(Activity activity, int requestCode) {
        startActivityForResultSafely(activity, new Intent(activity, AdColonyV4VCActivity.class), requestCode);
    }

    public static void startMainActivity(Context context) {
        startActivitySafely(context, new Intent(context, MainActivity.class));
    }
}

