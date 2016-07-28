package com.sunonline.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sunonline.activity.VideoPlayActivity;
import com.sunonline.adpter.VideoBeanAdapter;
import com.sunonline.bean.Video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  videoBean的默认实现类
 *  对网络中的数据类型为video的进行解析
 * Created by duanjigui on 2016/7/16.
 */
public class PaserVideodetailJson implements JsonParserUtil.PaserJson {
    private Activity activity;

    public PaserVideodetailJson(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void executeParse(JsonArray jsonArray, RecyclerView.Adapter adpter, Gson gson) {
        List<Video> old_driver_list = new ArrayList<Video>();
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement element = (JsonElement) iterator.next();
            Video video = gson.fromJson(element, Video.class);
            old_driver_list.add(video);
        }
        VideoBeanAdapter videoBeanAdapter= (VideoBeanAdapter) adpter;
        videoBeanAdapter.setList(old_driver_list);
        videoBeanAdapter.setOnClickListener(new VideoBeanAdapter.OnclickListener() {
            @Override
            public void onclick(List<Video> list, int Position) {
                Video video=list.get(Position);
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("videobean_video_info",video);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void executeObjctParse(JsonObject jsonObject, RecyclerView.Adapter adpter, Gson gson) {

    }
}
