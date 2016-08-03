package com.sunonline.global;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.sunonline.bean.UserInfo;
import com.sunonline.bean.Video;

import java.util.List;

/**
 * 存放系统全局的一些信息
 * Created by duanjigui on 2016/7/19.
 */
public class Information {

    public static Boolean IS_MENU_OPEN=false;  //菜单是否打开，默认为关闭的

    public static Boolean IS_LOGIN=false; //是否已经登录，默认false,即没有登录

    public static final String BASE_URL="http://139.129.221.162/webapi";  //访问web api    的根路径

    public static UserInfo userInfo=null;  //存放的用户信息

    public static boolean NET_INFO=false;  //网络连接信息
    /**
     * 给指定的imageview设置图片
     * @param imageView
     * @param textView
     * @param list
     */
    public static void SetImageForDataSet(ImageView imageView, TextView textView, List<Video> list,Activity activity) {
        for(int k=0;k<list.size();k++){
            if (list.get(k).getVideoName().equals( textView.getText().toString().trim())){
                Glide.with(activity)
                        .load(list.get(k).getVideoPicUrl())
                        .priority(Priority.LOW)
                        .into(imageView);

            }
        }
    }

}
