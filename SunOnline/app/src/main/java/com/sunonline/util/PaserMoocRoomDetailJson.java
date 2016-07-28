package com.sunonline.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sunonline.activity.PublicTeachActivity;
import com.sunonline.adpter.MoocRoomDetailAdpter;
import com.sunonline.bean.MoocRoomDetail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 针对PaserMoocRoomDetail的特定实现类
 * 由于是共用一个url，所以增加看类型字段，用于区分
 * Created by duanjigui on 2016/7/18.
 */
public class PaserMoocRoomDetailJson implements JsonParserUtil.PaserJson {
    public static final int MEDIAS_AFTER=0;
    public static final  int RADIO_INTERVIEW=1;
    public  static  final  int SOFTWARE_DEVELOPING=2;
    private Activity activity;
    private int type;
    public PaserMoocRoomDetailJson(Activity activity, int type) {
        this.activity = activity;
        this.type = type;
    }

    @Override
    public void executeParse(JsonArray jsonArray, RecyclerView.Adapter adpter, Gson gson) {

    }

    @Override
    public void executeObjctParse(JsonObject jsonObject, RecyclerView.Adapter adpter, Gson gson) {
        JsonArray jsonArray=null;
        if (type==SOFTWARE_DEVELOPING){
            jsonArray=  jsonObject.getAsJsonArray("softwareDeveloping");
        }else if (type==RADIO_INTERVIEW){
            jsonArray=  jsonObject.getAsJsonArray("radioInterview");
        }else if (type==MEDIAS_AFTER){
            jsonArray=  jsonObject.getAsJsonArray("mediasAfter");
        }else {
            Toast.makeText(activity,"类型输入错误！",Toast.LENGTH_SHORT).show();
        }
        List<MoocRoomDetail> list = new ArrayList<MoocRoomDetail>();
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement element = (JsonElement) iterator.next();
            MoocRoomDetail moocRecom = gson.fromJson(element, MoocRoomDetail.class);
            list.add(moocRecom);
        }
        MoocRoomDetailAdpter moocRoomDetailAdpter = (MoocRoomDetailAdpter) adpter;
        moocRoomDetailAdpter.setList(list);
        moocRoomDetailAdpter.setOnclickListener(new MoocRoomDetailAdpter.OnclickListener() {
            @Override
            public void onclick(List<MoocRoomDetail> list, int Position) {
                Log.e("message:", " " + list.size() + " " + Position);
                MoocRoomDetail moocRoomDetail=list.get(Position);
                Intent intent=new Intent(activity, PublicTeachActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("moocRoomDetail_bean_info",moocRoomDetail);
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }
        });


    }
}
