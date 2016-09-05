package org.fastgame.bbt.utility;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import org.fastgame.bbt.BBT;

/**
 * @Author MrLi
 * @Since 08/29/2016
 */
public class SystemUtils {

    public static ApplicationInfo getApplicationInfo() {
        Context context = BBT.getAppContext();
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMetaDataString(String fieldName) {
        ApplicationInfo applicationInfo = getApplicationInfo();

        if (applicationInfo == null) {
            return "";
        }

        return applicationInfo.metaData.getString(fieldName);
    }

    public static String getVersionName() {
        Context context = BBT.getAppContext();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

}
