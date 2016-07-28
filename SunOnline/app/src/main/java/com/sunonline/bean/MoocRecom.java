package com.sunonline.bean;

import java.io.Serializable;

/**
 * 公益课堂json解析相关
 * Created by duanjigui on 2016/7/16.
 */
public class MoocRecom implements Serializable{
    private int c_id;
    private String c_path_url;
    private  int cl_id;
    private String cl_name;
    private String cl_pic_url;
    private int cl_play_time;
    private String cl_upload_time;
    private String cl_video_intro;
    private String cl_video_url;

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getC_path_url() {
        return c_path_url;
    }

    public void setC_path_url(String c_path_url) {
        this.c_path_url = c_path_url;
    }

    public int getCl_id() {
        return cl_id;
    }

    public void setCl_id(int cl_id) {
        this.cl_id = cl_id;
    }

    public String getCl_name() {
        return cl_name;
    }

    public void setCl_name(String cl_name) {
        this.cl_name = cl_name;
    }

    public String getCl_pic_url() {
        return cl_pic_url;
    }

    public void setCl_pic_url(String cl_pic_url) {
        this.cl_pic_url = cl_pic_url;
    }

    public int getCl_play_time() {
        return cl_play_time;
    }

    public void setCl_play_time(int cl_play_time) {
        this.cl_play_time = cl_play_time;
    }

    public String getCl_upload_time() {
        return cl_upload_time;
    }

    public void setCl_upload_time(String cl_upload_time) {
        this.cl_upload_time = cl_upload_time;
    }

    public String getCl_video_intro() {
        return cl_video_intro;
    }

    public void setCl_video_intro(String cl_video_intro) {
        this.cl_video_intro = cl_video_intro;
    }

    public String getCl_video_url() {
        return cl_video_url;
    }

    public void setCl_video_url(String cl_video_url) {
        this.cl_video_url = cl_video_url;
    }
}
