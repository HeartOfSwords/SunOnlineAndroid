package com.sunonline.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * 负责获取网络数据工具类
 * 获取真实的数据需要stringInformationFromNet，resourceFromNet变量
 * 自带封装成异步加载数据
 * 异步数据获取的原理，通过设置监听器来保证数据一定可以加载成功
 * Created by duanjigui on 2016/7/8.
 */
public class NetUtil extends  NetHelper{
    private static final int STRING_INFO = 1;
    private static final int INPUT_RESOURSE = 2;
    private static final int ERROR_MESSAGE = 3;
    private static final int IMAGE_RESOURSE = 21;
    private Context context;
    private String stringInformationFromNet;  //字符串信息
    private InputStream resourceFromNet;  //图片、视频二进制等信息

    private Bitmap bitmap;//图片资源

    private OnValueRecived onValueRecived;  //设置监听事件

    public void setOnValueRecived(OnValueRecived onValueRecived) {
        this.onValueRecived = onValueRecived;
    }
    //当子线程将值赋予时，调用此接口
  public   interface OnValueRecived{
        //当接收到消息时，调用此方法
        public  void  recieve(String stringInformationFromNet,InputStream inputStream);

        public void recieveBitmap(Bitmap bitmap);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case STRING_INFO:
                    stringInformationFromNet= (String) msg.obj;
                    onValueRecived.recieve(stringInformationFromNet,null);
                    break;

                case INPUT_RESOURSE:
                    if (msg.arg1==IMAGE_RESOURSE){
                        bitmap= (Bitmap) msg.obj;
                        onValueRecived.recieveBitmap(bitmap);
                    }else {
                        //待定
                    }
                    break;
                case ERROR_MESSAGE:
                    String error_message= (String) msg.obj;
                    Toast.makeText(context,error_message,Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    public NetUtil(Context context){
        this.context=context;
    }

    /**
     *从指定URL中使用指定的请求获取服务端返回的数据
     * @param URL  要连接的url
     * @param HttpMethod  请求的方法
     */
    public void getStringInformationFromNet( final String URL , final String HttpMethod, final Boolean input, final Boolean output)  {
        new Thread(){
            @Override
            public void run() {
                try {
                    String result=null;
                    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                    InputStream inputStream= fetchInputStreamFromNet(URL, HttpMethod,input,output);
                    byte [] temp=new byte[1024];
                    int length=0;
                    if (null!=inputStream){
                        while ((length= inputStream.read(temp))>=0){
                            outputStream.write(temp,0,length);
                        }
                        Message message= handler.obtainMessage();
                        message.what=STRING_INFO;
                        result=outputStream.toString();
                        Log.d("error",result);
                        message.obj=result;
                        message.sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 根据url和方法获取网络资源【图片，视频】
     * @param URL
     * @param HttpMethod
     * @return
     */
    public void getImageResourceFromNet(final String URL, final String HttpMethod, final Boolean input, final Boolean output){
        new Thread(){
            @Override
            public void run() {
                InputStream inputStream=null;
                inputStream=fetchInputStreamFromNet(URL, HttpMethod,input,output);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                Message message= handler.obtainMessage();
                message.what=INPUT_RESOURSE;
                message.arg1=IMAGE_RESOURSE;
                message.obj=bitmap;
                message.sendToTarget();
            }
        }.start();
    }


    @Override
    protected InputStream httpResponeSucess(HttpURLConnection connection) {
        InputStream inputStream=null;
        try {
            inputStream=  connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    @Override
    protected void httpResponeFail() {
        Message message= handler.obtainMessage();
        message.what=ERROR_MESSAGE;
        message.obj="sorry,网络故障!";
        message.sendToTarget();
    }
}
