package org.fastgame.bbt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author: MrLi
 * @Since: 2016/9/8
 */
public class Whatever {

    public static void main(String[] args) {

        String filepath = "C:\\Users\\dawni\\Desktop\\ip.txt";

        try {

            BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));

            String tmpStr;

            while((tmpStr = reader.readLine()) != null) {
                String host = tmpStr.substring(0, tmpStr.indexOf(":"));
                String port = tmpStr.substring(tmpStr.indexOf(":") + 1);

                openHtml(host, port);
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

//        System.out.println(sendPost("http://t.cn/RcyE5bh")); //QiuQiu
        System.out.println(sendPost("http://snakeapi10.fanqiestatic.com/s/0vv2H-6653c")); //Snake
//        System.out.println(sendPost("http://t.cn/Rc5SDTW")); //Monster
    }

    public static String sendPost(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Dalvik/1.6.0 (Linux; U; Android 4.1.2; Nexus S Build/JZO54K)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


}
