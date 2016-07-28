package com.sunonline.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by duanjigui on 2016/7/11.
 */
public class WindowsUtill {
    /**
     *  获取屏幕的宽度和高度
     * @param context
     * @return  int[]  第一个值为宽度，第二个值为高度
     */
    public static int[] getScreenWidthAndHeight(Context context){
        int attr[]=new int[2];
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        attr[0]=displayMetrics.widthPixels;
        attr[1]=displayMetrics.heightPixels;
        return attr;
    }
    //设置全屏
    public static void fullScreen(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    //取消全屏
    public  static  void cancleScreen(Activity activity){
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
