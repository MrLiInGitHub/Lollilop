package org.fastgame.bbt;

import android.test.AndroidTestCase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author: MrLi
 * @Since: 2016/9/8
 */
public class AndroidTest extends AndroidTestCase {

    public void testAd() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(BBT.getAppContext().getAssets().open("ip.txt")));

            String tmpStr;

            while((tmpStr = reader.readLine()) != null) {
                String host = tmpStr.substring(0, tmpStr.indexOf(":"));
                String port = tmpStr.substring(tmpStr.indexOf(":") + 1);

                System.out.println("host:" + host + ",port:" + port);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openHtml(String ip, String port) {
        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", port);

        System.out.println(getHtml("http://www.ip138.com/ip2city.asp"));
    }

    private static String getHtml(String address){
        StringBuffer html = new StringBuffer();
        String result = null;
        try{
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

            try{
                String inputLine;
                byte[] buf = new byte[4096];
                int bytesRead = 0;
                while (bytesRead >= 0) {
                    inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
                    html.append(inputLine);
                    bytesRead = in.read(buf);
                    inputLine = null;
                }
                buf = null;
            }finally{
                in.close();
                conn = null;
                url = null;
            }
            result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            html = null;
        }
        return result;
    }

}
