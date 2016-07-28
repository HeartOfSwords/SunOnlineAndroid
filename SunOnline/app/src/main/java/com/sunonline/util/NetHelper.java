package com.sunonline.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 对http请求进行了一次轻量的封装
 * Created by duanjigui on 2016/7/8.
 */
public abstract class NetHelper {

    protected InputStream fetchInputStreamFromNet(String URL ,String HttpMethod,Boolean input,Boolean output){
        InputStream inputStream=null;
        try {
            URL url= new URL(URL);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod);
            connection.setConnectTimeout(5000);
            if (input){
                connection.setDoInput(true);
            }
            if (output){
                connection.setDoOutput(true);
            }
            int responseCode=  connection.getResponseCode();
            Log.d("error", " code is:"+responseCode);
            if (responseCode==200){
                inputStream=httpResponeSucess(connection);
            }else {
                httpResponeFail();
            }
        } catch (MalformedURLException e) {
            httpResponeFail();
            e.printStackTrace();
        } catch (ProtocolException e) {
            httpResponeFail();
            e.printStackTrace();
        } catch (IOException e) {
            httpResponeFail();
            e.printStackTrace();
        }
        return inputStream;
    }
    //请求成功时需要执行的方法
    protected abstract InputStream httpResponeSucess(HttpURLConnection connection);
    //请求失败需要执行的方法
    protected abstract void httpResponeFail();
}
