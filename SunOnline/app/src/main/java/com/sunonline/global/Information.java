package com.sunonline.global;

import com.sunonline.bean.UserInfo;

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

}
