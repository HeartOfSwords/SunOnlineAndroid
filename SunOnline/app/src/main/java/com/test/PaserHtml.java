package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.sunonline.application.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 测试jsoup抓取网页数据
 * Created by duanjigui on 2016/7/19.
 */
public class PaserHtml extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        new Thread(){
            @Override
            public void run() {
                try {
                    Document document= Jsoup.connect("http://139.129.221.162/studentGuide.html").get();
                    Elements element= document.select("div.video-player video");
                    String vedio_url=   element.attr("src");
                    Log.d("url", vedio_url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();



    }
}
