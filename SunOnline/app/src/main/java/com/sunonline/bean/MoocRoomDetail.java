package com.sunonline.bean;

import java.io.Serializable;

/**
 * 公益课堂部分讲师和课程的详细信息对应json的bean
 * Created by duanjigui on 2016/7/18.
 */
public class MoocRoomDetail implements Serializable{
    private String c_id;//课程id
    private String c_introduce; //课程介绍
    private String c_name;//课程名称
    private String c_path_url;//课程路径url
    private String c_pic_url;//图片url
    private String c_teacher_intro;//讲师介绍
    private String c_teacher_name;//讲师姓名
    private int t_id;//teacher id

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_introduce() {
        return c_introduce;
    }

    public void setC_introduce(String c_introduce) {
        this.c_introduce = c_introduce;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_path_url() {
        return c_path_url;
    }

    public void setC_path_url(String c_path_url) {
        this.c_path_url = c_path_url;
    }

    public String getC_pic_url() {
        return c_pic_url;
    }

    public void setC_pic_url(String c_pic_url) {
        this.c_pic_url = c_pic_url;
    }

    public String getC_teacher_intro() {
        return c_teacher_intro;
    }

    public void setC_teacher_intro(String c_teacher_intro) {
        this.c_teacher_intro = c_teacher_intro;
    }

    public String getC_teacher_name() {
        return c_teacher_name;
    }

    public void setC_teacher_name(String c_teacher_name) {
        this.c_teacher_name = c_teacher_name;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }
}
