package com.sunonline.bean;

import java.io.Serializable;

/**
 * Created by duanjigui on 2016/7/8.
 * 解析json数据的实体类型
 */
public class Video implements Serializable{
    private String type;  //类型
    private String videoDate; //上传日期
    private int videoId;  //video标示
    private String videoName;  //视频名称
    private String videoPicUrl; //视频截图
    private int videoPlayedNumber; //播放次数
    private String videoUploader;//上传者
    private  String videoUrl;//视频播放的url
    private String videoIntro;  //视屏介绍

    public String getVideoIntro() {
        return videoIntro;
    }

    public void setVideoIntro(String videoIntro) {
        this.videoIntro = videoIntro;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPicUrl() {
        return videoPicUrl;
    }

    public void setVideoPicUrl(String videoPicUrl) {
        this.videoPicUrl = videoPicUrl;
    }

    public int getVideoPlayedNumber() {
        return videoPlayedNumber;
    }

    public void setVideoPlayedNumber(int videoPlayedNumber) {
        this.videoPlayedNumber = videoPlayedNumber;
    }

    public String getVideoUploader() {
        return videoUploader;
    }

    public void setVideoUploader(String videoUploader) {
        this.videoUploader = videoUploader;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
