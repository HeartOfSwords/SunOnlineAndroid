package com.sunonline.bean;

import java.io.Serializable;

/**
 * 存放用户登录后信息的bean
 * Created by duanjigui on 2016/7/23.
 */
public class UserInfo implements Serializable{
    private String userAvatar; //用户头像地址
    private String userEmail;//用户邮箱
    private String userMobile;//用户手机号
    private int user_id;//用户id
    private String usernickName;//用户昵称

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsernickName() {
        return usernickName;
    }

    public void setUsernickName(String usernickName) {
        this.usernickName = usernickName;
    }
}
