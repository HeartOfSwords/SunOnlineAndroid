package com.sunonline.util;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用来给recycleview设置适配器和数据源同时进行转换数据
 * Created by duanjigui on 2016/7/16.
 */
public class JsonParserUtil {
    private PaserJson paserJson;//接口实现
    private Activity activity;
    public static int VIDEOBEAN_TYPE=0;
    private int type;
    public JsonParserUtil(Activity activity){
        this.activity=activity;
    }
    public JsonParserUtil(Activity activity,int type){
        this(activity);
        this.type=type;
    }
    public void setPaserJson(PaserJson paserJson) {
        this.paserJson = paserJson;
    }

    public  interface PaserJson{
        public void executeParse(JsonArray jsonArray,RecyclerView.Adapter adpter,Gson gson); //根据具体的实例进行解析，同时给adpter设置数据源
        public void executeObjctParse(JsonObject jsonObject,RecyclerView.Adapter adpter,Gson gson);
    }
    public void paser(String url, final RecyclerView recyclerView,  final RecyclerView.Adapter adpter) {
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        //设置源
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "网络连接失败！请连接网络后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonArray jsonArray = null;
                JsonObject jsonObject=null;
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                String jsonValue = response.body().string();
                Log.d("message", jsonValue);
                synchronized (jsonValue){
                    JsonElement jsonElement = jsonParser.parse(jsonValue);
                    if (jsonElement.isJsonArray()) {
                        jsonArray = jsonElement.getAsJsonArray();
                        paserJson.executeParse(jsonArray, adpter, gson);//在具体的部分在自己重新赋值解决
                    }else if (jsonElement.isJsonObject()){
                        jsonObject= jsonElement.getAsJsonObject();
                        paserJson.executeObjctParse(jsonObject,adpter,gson);
                    }else {
                        Toast.makeText(activity,"json数据格式有错误！",Toast.LENGTH_SHORT).show();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adpter);
                        }
                    });
                }


            }
        });
    }


}
