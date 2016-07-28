package com.sunonline.boradReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.sunonline.global.Information;

/**
 * 监听网络状态的变化
 * Created by duanjigui on 2016/7/27.
 */
public class NotifyNetBoradCast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){

            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo= manager.getActiveNetworkInfo();

            if (null!=networkInfo&&networkInfo.isAvailable()){

                if (networkInfo.isConnected()){

                    Information.NET_INFO=true;

                    Toast.makeText(context,"网络连接了！",Toast.LENGTH_SHORT).show();

                }else {

                    Information.NET_INFO=false;

                    Toast.makeText(context,"网络断开了！",Toast.LENGTH_SHORT).show();

                }
            }

        }

    }
}
