package com.sunonline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sunonline.adpter.MoocRecomAdapter;
import com.sunonline.application.R;
import com.sunonline.bean.MoocRecom;
import com.sunonline.bean.MoocRoomDetail;
import com.sunonline.util.JsonParserUtil;
import com.sunonline.util.PaserMoocRoomJson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by duanjigui on 2016/7/18.
 */
public class PublicTeachActivity extends Activity implements View.OnClickListener{
    private MoocRoomDetail moocRoomDetail;
    private TextView courses_name;//课程名字
    private RecyclerView courses_main;//课程的主要内容区域
    private String url;//详细的url
    private ImageView go_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_teach_third_page);
        moocRoomDetail= (MoocRoomDetail) getIntent().getSerializableExtra("moocRoomDetail_bean_info");
        url="http://139.129.221.162/"+moocRoomDetail.getC_path_url()+"1";
        courses_name= (TextView) findViewById(R.id.courses_name);
        courses_main= (RecyclerView) findViewById(R.id.courses_main);
        go_back= (ImageView) findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
        courses_name.setText(moocRoomDetail.getC_name());//设置课程名称
        //在paser_json设置的
        JsonParserUtil jsonParserUtil=new JsonParserUtil(PublicTeachActivity.this);
        jsonParserUtil.setPaserJson(new PaserMoocRoomJson(this));
        jsonParserUtil.paser(url, courses_main, new MoocRecomAdapter(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_back:
                finish();
                break;

        }
    }
}
