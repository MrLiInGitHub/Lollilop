package org.fastgame.bbt.utility;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utilities about network.
 *
 * @Author MrLi
 * @Since 08/20/2016
 */
public class NetworkUtils {

    /** the return value when there is no network */
    private static final int TYPE_NONE = -1;

    /**
     * Reports the type of network to which the
     * info in this {@code NetworkInfo} pertains.
     *
     * @return one of {@link ConnectivityManager#TYPE_MOBILE}, {@link
     * ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_WIMAX}, {@link
     * ConnectivityManager#TYPE_ETHERNET},  {@link ConnectivityManager#TYPE_BLUETOOTH}, or other
     * types defined by {@link ConnectivityManager}
     */
    public static int getNetworkType() {

        ConnectivityManager connectivityManager = SystemManagerUtils.getConnectivityManager();

        if (connectivityManager == null) {
            return TYPE_NONE;
        }
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

        if (info == null || info.length == 0) {
            return TYPE_NONE;
        } else {
            for (int i =0,len = info.length; i < len; i++) {
                NetworkInfo.State state = info[i].getState();
                if (state == NetworkInfo.State.CONNECTED) {
                    return info[i].getType();
                }
            }
        }

        return TYPE_NONE;
    }

    /**
     * if the device is connected to network.
     *
     * @return the result
     */
    public static boolean isNetworkAvailable() {
        return getNetworkType() != TYPE_NONE;
    }

    public static boolean isWiFiConnected() {
        return getNetworkType() == ConnectivityManager.TYPE_WIFI;
    }

}
