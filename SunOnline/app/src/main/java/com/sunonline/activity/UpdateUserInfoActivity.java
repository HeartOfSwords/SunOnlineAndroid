package com.sunonline.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sunonline.application.R;
import com.sunonline.bean.UserInfo;
import com.sunonline.global.Information;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 职责： 修改用户信息的activity
 * Created by duanjigui on 2016/7/26.
 */
public class UpdateUserInfoActivity extends Activity implements View.OnClickListener{
    private EditText update_email;
    private EditText update_nickname;
    private EditText update_password;
    private EditText update_head_url;
    private EditText update_phone;
    private Button update_ok;
    private ImageView go_back;//返回按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_info);
        update_email= (EditText) findViewById(R.id.update_email);
        update_nickname= (EditText) findViewById(R.id.update_nickname);
        update_password= (EditText) findViewById(R.id.update_password);
        update_head_url= (EditText) findViewById(R.id.update_head_url);
        update_phone= (EditText) findViewById(R.id.update_phone);
        update_ok= (Button) findViewById(R.id.update_ok);
        go_back= (ImageView) findViewById(R.id.go_back);
        update_ok.setOnClickListener(this);
        go_back.setOnClickListener(this);
        update_phone.setText(Information.userInfo.getUserMobile());
        update_phone.setEnabled(false);
        update_email.setText(Information.userInfo.getUserEmail());
        update_email.setEnabled(false);
        update_nickname.setText(Information.userInfo.getUsernickName());
        update_head_url.setText(Information.userInfo.getUserAvatar());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_ok: //执行提交信息
                String update_email_text=update_email.getText().toString().trim();
                final String update_nickname_text=update_nickname.getText().toString().trim();
                final String update_password_text=update_password.getText().toString().trim();
                final String update_head_url_text=update_head_url.getText().toString().trim();
                new Thread(){
                    @Override
                    public void run() {
                        connectUrl(Information.BASE_URL+"/user/changer/nickname?mobile="+ Information.userInfo.getUserMobile()+"&nickname="+update_nickname_text,UpdateUserInfoActivity.this);
                        connectUrl(Information.BASE_URL+"/user/changer/userpwd/logged?mobile="+Information.userInfo.getUserMobile()+"&userpwd="+update_password_text,UpdateUserInfoActivity.this);
                        connectUrl(Information.BASE_URL+"/user/avatar/upload?u_id="+Information.userInfo.getUser_id()+"&avatar_url="+update_head_url_text,UpdateUserInfoActivity.this);
                        Information.userInfo=connectUrl(Information.BASE_URL+"/user/login?inputString="+Information.userInfo.getUserMobile()+"&userpwd="+update_password_text,UpdateUserInfoActivity.this);
                        setResult(2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateUserInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                }.start();
                break;
            case R.id.go_back:
                finish();
                break;
        }
    }

    /**
     * 连接网络端获取信息
     * @param url
     * @param activity
     * @return
     */
    public static UserInfo connectUrl(String url,Activity activity) {
        UserInfo userInfo=null;
        RequestBody body=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),"");

        OkHttpClient httpClient=new OkHttpClient();

        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response=  httpClient.newCall(request).execute();
            String tip= response.body().string();
            if (!url.contains("webapi/user/login")){
                //Toast.makeText(activity,tip,Toast.LENGTH_SHORT).show();
            }else {
                Gson gson=new Gson();
                JsonParser parser=new JsonParser();
                JsonElement jsonElement= parser.parse(tip);
                userInfo= gson.fromJson(jsonElement, UserInfo.class);

            }
            return  userInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
