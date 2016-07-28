package com.sunonline.util;

import com.sunonline.bean.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 专门负责将字符串json数据封装成对象的形式
 * Created by duanjigui on 2016/7/8.
 */
public class JsonUtill {
    /**
     * 将video类型的json字符串映射成为video字符串
     * @param value
     * @return
     */
    public List<Video>  fetchVideoAllInfo(String value){
        List<Video> list=new ArrayList<>();
        try {
            if (null!= value){
                JSONArray jsonArray=new JSONArray(value);
                for (int i=0;i<jsonArray.length();i++){
                    Video video=new Video();
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    video.setType(jsonObject.getString("type"));
                    video.setVideoDate(jsonObject.getString("videoDate"));
                    video.setVideoId(jsonObject.getInt("videoId"));
                    video.setVideoIntro(jsonObject.getString("videoIntro"));
                    video.setVideoName(jsonObject.getString("videoName"));
                    video.setVideoPicUrl(jsonObject.getString("videoPicUrl"));
                    video.setVideoPlayedNumber(jsonObject.getInt("videoPlayedNumber"));
                    video.setVideoUploader(jsonObject.getString("videoUploader"));
                    video.setVideoUrl(jsonObject.getString("videoUrl"));
                    list.add(video);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
