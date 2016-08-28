package org.fastgame.bbt.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import org.fastgame.bbt.BBT;

/**
 * Get system-related manager.
 *
 * @Author MrLi
 * @Since 08/20/2016
 */
public class SystemManagerUtils {

    public static ConnectivityManager getConnectivityManager() {
        Context context = BBT.getAppContext();

        if (context == null) {
            return null;
        } else {
            return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }

}
