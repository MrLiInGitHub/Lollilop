package org.fastgame.bbt.connectivity;

import android.text.TextUtils;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.utility.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * @Author: MrLi
 * @Since: 2016/9/12
 */
public class HttpRequestHelper {

    public static void simulateClick(String host) {

        if (TextUtils.isEmpty(host)) {
            return;
        }

        try {
            HttpURLConnection connection = getHttpUrlConnection(host);
            getHttpUrlConnectionResponseContent(connection);

            String url = connection.getURL().toString();
            String targetUrl = "";
            int totalTimes = 0;

            if (isMonsterLink(url)) {
                targetUrl = getMonsterShareUrl(url);
                totalTimes = 5;
            } else if (isBallsLink(url)) {
                targetUrl = getBallsShareUrl(url);
                totalTimes = 5;
            }

            getHttpUrlConnectionResponseContent(getHttpUrlConnection(targetUrl));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void proxyClick(String url, int totalTime) {

        int time = 0;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(BBT.getAppContext().getAssets().open("ip.txt")));
            String tmp;

            while ((tmp = reader.readLine()) != null) {
                String host = tmp.substring(0, tmp.indexOf(":"));
                String port = tmp.substring(tmp.indexOf(":") + 1);

//                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Integer.parseInt(port)));
                System.setProperty("http.proxyHost", host);
                System.setProperty("http.proxyPort", port);

                HttpURLConnection post = getHttpUrlConnection(url, "POST");

                if (post.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    time++;
                }

                LogUtils.debug("TAG", getHttpUrlConnectionResponseContent(post));

                if (time > totalTime) {
                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMonsterLink(String url) {
        return !isEmpty(url) && url.contains("4399sy.com");
    }

    public static boolean isBallsLink(String url) {
        return !isEmpty(url) && url.contains("www.battleofballs.com");
    }

    public static HttpURLConnection getHttpUrlConnection(String host) throws IOException {
        return getHttpUrlConnection(host, "");
    }

    public static HttpURLConnection getHttpUrlConnection(String host, String requestMethod) throws IOException {
        return getHttpUrlConnection(host, requestMethod, null);
    }

    public static HttpURLConnection getHttpUrlConnection(String host, String requestMethod, Proxy proxy) throws IOException {

        if (isEmpty(host)) {
            return null;
        }

        if (proxy != null) {
            LogUtils.debug("TAG", "httpConnection host:" + host + ",proxy:" + proxy.address());
        }

        URL url = new URL(host);
        HttpURLConnection connection = (HttpURLConnection) (proxy == null ? url.openConnection() : url.openConnection(proxy));

        if (!isEmpty(requestMethod)) {
            connection.setRequestMethod(requestMethod);
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.933.400 QQBrowser/9.4.8699.400");

        return connection;
    }

    public static String getHttpUrlConnectionResponseContent(HttpURLConnection httpURLConnection) throws IOException {

        if (httpURLConnection == null) {
            return "";
        }

        int code = httpURLConnection.getResponseCode();

        LogUtils.debug("TAG", "getContent: code:" + code);

        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String tmp;
            StringBuffer res = new StringBuffer(200);

            while ((tmp = reader.readLine()) != null) {
                res.append(tmp);
            }

            return res.toString();
        } else {
            return "";
        }
    }

    public static String getMonsterShareUrl(String url) {
        return isEmpty(url) ? "" : "http://udpdcs.4399sy.com/get_share_data.php?id=" + url.substring(url.indexOf("?id=") + 4, url.indexOf("&s="));
    }

    public static String getBallsShareUrl(String url) {
        return isEmpty(url) ? "" : "http://cn.battleofballs.com/share?type=1&id=" + url.substring(url.indexOf("?id=") + 4, url.indexOf("&Account="));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static void printStr(String str) {
        System.out.println(str);
    }

}
