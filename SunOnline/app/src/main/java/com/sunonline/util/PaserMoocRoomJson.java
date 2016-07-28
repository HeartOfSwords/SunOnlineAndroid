package com.sunonline.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sunonline.activity.VideoPlayActivity;
import com.sunonline.adpter.MoocRecomAdapter;
import com.sunonline.bean.MoocRecom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by duanjigui on 2016/7/18.
 */
public class PaserMoocRoomJson implements JsonParserUtil.PaserJson {
    private Activity activity;

    public PaserMoocRoomJson(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void executeParse(JsonArray jsonArray, RecyclerView.Adapter adpter, Gson gson) {
        List<MoocRecom> public_teach_list = new ArrayList<MoocRecom>();
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement element = (JsonElement) iterator.next();
            MoocRecom moocRecom = gson.fromJson(element, MoocRecom.class);
            public_teach_list.add(moocRecom);
        }
        MoocRecomAdapter moocRecomAdapter = (MoocRecomAdapter) adpter;
        moocRecomAdapter.setList(public_teach_list);
        Log.d("message", public_teach_list.size() + " " + public_teach_list.get(0).getCl_name());
        moocRecomAdapter.setOnClickListener(new MoocRecomAdapter.OnclickListener() {
            @Override
            public void onclick(List<MoocRecom> list, int Position) {
                Log.d("list", " " + list.size() + " " + Position);
                MoocRecom moocRecom=     list.get(Position);
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("moocRecom_video_info",moocRecom);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void executeObjctParse(JsonObject jsonObject, RecyclerView.Adapter adpter, Gson gson) {

    }
}
