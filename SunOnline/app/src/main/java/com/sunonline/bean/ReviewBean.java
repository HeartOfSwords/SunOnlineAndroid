package com.sunonline.bean;

import java.io.Serializable;

/**
 * Created by duanjigui on 2016/7/28.
 */
public class ReviewBean implements Serializable{
    private String phone;
    private String image_url;
    private String review_text;
    private int good_num;//赞的数量
    private String send_data;//发送日期

    public String getSend_data() {
        return send_data;
    }

    public void setSend_data(String send_data) {
        this.send_data = send_data;
    }

    public int getGood_num() {
        return good_num;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }
}
