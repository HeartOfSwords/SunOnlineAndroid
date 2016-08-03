package com.sunonline.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sunonline.application.R;
import com.sunonline.bean.UserInfo;
import com.sunonline.global.Information;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登录的acitivity
 * Created by duanjigui on 2016/7/21.
 */
public class LoginActivity extends Activity implements View.OnClickListener,View.OnFocusChangeListener{
    private EditText login_account;//账号
    private TextView login_password;//密码
    private Button login_btn; //登录按钮
    private TextView want_regiester;//注册显示
    private FrameLayout regieste_warp;//注册页面
    private LinearLayout login_warp;//登陆页面
    private ImageButton close_register;//关闭按钮
    private EditText register_email;//注册邮箱
    private EditText register_phone;//注册手机号
    private EditText register_password;//注册密码
    private EditText register_enter_password;//注册确认密码
    private Button register_btn;//注册按钮
    private SwitchCompat remember_me;//记住我按钮
    private Boolean is_remember_me;//记录是否记住账号密码
    private ImageView register_email_img;//注册邮箱提示图片
    private ImageView register_phone_img;//注册手机号提示图片
    private ImageView register_password_img;//注册密码提示图片
    private ImageView register_enter_password_img;//注册确认密码提示图片
    private String register_pass_string; //注册密码的string
    private String register_enter_pass_string;//注册确认密码的string
    private boolean [] judge=new boolean[]{false,false,false,false};//正则标志位判断【四个验证】
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        login_account= (EditText) findViewById(R.id.login_account);
        login_password= (TextView) findViewById(R.id.login_password);
        login_btn= (Button) findViewById(R.id.login_btn);
        want_regiester= (TextView) findViewById(R.id.want_regiester);
        regieste_warp= (FrameLayout) findViewById(R.id.regieste_warp);
        login_warp= (LinearLayout) findViewById(R.id.login_warp);
        close_register= (ImageButton) findViewById(R.id.close_register);
        register_email= (EditText) findViewById(R.id.register_email);
        register_phone= (EditText) findViewById(R.id.register_phone);
        register_password= (EditText) findViewById(R.id.register_password);
        register_enter_password= (EditText) findViewById(R.id.register_enter_password);
        register_btn= (Button) findViewById(R.id.register_btn);
        remember_me= (SwitchCompat) findViewById(R.id.remember_me);
        setButtonIsEnabled(register_btn,false);
        register_email_img= (ImageView) findViewById(R.id.register_email_img);
        register_phone_img= (ImageView) findViewById(R.id.register_phone_img);
        register_password_img= (ImageView) findViewById(R.id.register_password_img);
        register_enter_password_img= (ImageView) findViewById(R.id.register_enter_password_img);
        //根据存储文件的信息来设置textview中的值
        readAccountAndPasswordInLocal();
        register_btn.setOnClickListener(this);
        close_register.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        want_regiester.setOnClickListener(this);
        register_email.setOnFocusChangeListener(this);
        register_phone.setOnFocusChangeListener(this);
        register_enter_password.setOnFocusChangeListener(this);
        register_enter_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(register_pass_string)){
                    changeDirectImage(register_enter_password_img,R.drawable.ok);
                    judge[3]=true;
                }else {
                    judge[3]=false;
                }
                setButtonIsEnabled(register_btn,true);
                for (boolean is_pass:judge){
                    if (is_pass==false){
                        setButtonIsEnabled(register_btn,false);
                    }
                }
            }
        });
        register_password.setOnFocusChangeListener(this);

        Log.d("max_memory", String.valueOf(Runtime.getRuntime().maxMemory()/1024));

    }

    /**
     * 读取存储在本地的账号和密码的信息
     */
    private void readAccountAndPasswordInLocal() {
        SharedPreferences preferences= getSharedPreferences("storeInfo", MODE_PRIVATE);
        String user_email=  preferences.getString("user_email","");
        String user_password=  preferences.getString("user_password","");
        boolean is_remember= preferences.getBoolean("is_remember", false);
        if (is_remember==true){
            login_account.setText(user_email);
            login_password.setText(user_password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:  //处理登陆页面逻辑
                new Thread(){
                    @Override
                    public void run() {
                        String user_email=login_account.getText().toString().trim();
                        final String user_password=login_password.getText().toString().trim();
                        is_remember_me= remember_me.isChecked();
                        //无论是否选择记住我这个按钮，都会将账号、密码的信息存入文件中
                        storeAccountAndPasswordInLocal(user_email,user_password,is_remember_me);
                        OkHttpClient httpClient=new OkHttpClient();
                        RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                                "inputString="+ user_email+"&userpwd="+user_password);

                        Request request=new Request.Builder()
                                .url(Information.BASE_URL +"/user/login?inputString="+ user_email+"&userpwd="+user_password)
                                .post(requestBody)
                                .build();

                        httpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar snackbar= Snackbar.make(want_regiester, "登录失败！请连接网络后再试", Snackbar.LENGTH_SHORT);
                                        TextView textView= (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                                        textView.setTextColor(Color.WHITE);
                                        snackbar.show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                int status_code= response.code();

                                if (status_code==204){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Snackbar snackbar=    Snackbar.make(want_regiester, "sorry,没有该用户或密码出错！", Snackbar.LENGTH_SHORT);
                                            snackbar .setAction("去注册一个", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //跳转到注册页面的逻辑
                                                    regieste_warp.setVisibility(View.VISIBLE);

                                                }
                                            })
                                                    .setActionTextColor(Color.parseColor("#FF3030"))
                                                    .show();
                                            View view=  snackbar.getView();
                                            view .setBackgroundColor(Color.parseColor("#AAAAAA"));
                                            TextView snackView= (TextView) view .findViewById(R.id.snackbar_text); //源码中的682行
                                            snackView.setTextColor(Color.WHITE);
                                        }
                                    });
                                }else if (status_code==200){
                                    //查看一下返回逻辑，判断登录是否成功
                                    Gson gson=new Gson();
                                    JsonParser parser=new JsonParser();
                                    JsonElement element= parser.parse(response.body().string());
                                    UserInfo userInfo=   gson.fromJson(element, UserInfo.class); //解析返回来的json数据
                                    Intent intent= getIntent();
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("UserInfo",userInfo);
                                    intent.putExtras(bundle);
                                    LoginActivity.this.setResult(1, intent);
                                    LoginActivity.this.finish();
                                }
                            }
                        });

                    }
                }.start();

                break;

            case R.id.want_regiester: //处理跳转到注册页面逻辑
                regieste_warp.setVisibility(View.VISIBLE);
                break;
            case R.id.close_register:  //处理关闭注册按钮逻辑
                regieste_warp.setVisibility(View.INVISIBLE);
                break;
            case R.id.register_btn:  //处理注册逻辑
                String re_user_emaill=register_email.getText().toString().trim();  //用户邮箱
                String re_user_phone=register_phone.getText().toString().trim();//用户手机号吗
                String re_user_pass=register_password.getText().toString().trim();//用户密码
                //下面是一些注册操作
                OkHttpClient httpClient=new OkHttpClient();
                RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),"pwd=*&usermobile=*&useremail=*");
                Request request=new Request.Builder()
                        .url(Information.BASE_URL + "/user/register?pwd=" + re_user_pass + "&usermobile=" + re_user_phone + "&useremail=" + re_user_emaill)
                        .post(requestBody)
                        .build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        if ("注册成功，用户已创建".equals(result)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"注册成功，用户已创建",Toast.LENGTH_SHORT).show();
                                    regieste_warp.setVisibility(View.INVISIBLE);
                                    //设置登录时的回显
                                    login_account.setText(register_phone.getText().toString());
                                    login_password.setText("");
                                    //清空注册部分的输入的值，还原初始状态
                                    register_password.setText("");
                                    register_phone.setText("");
                                    register_email.setText("");
                                    register_enter_password.setText("");

                                }
                            });
                        }else if("您使用的手机号或邮箱已经被他人占用，请更换之后重新注册".equals(result)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"您使用的手机号或邮箱已经被他人占用，请更换之后重新注册",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }



                    }
                });
                for (int i=0;i<judge.length;i++){
                    judge[i]=false;
                }
                break;

        }
    }

    /**
     *将账号和密码存储到本地
     * @param user_email  账号
     * @param user_password 密码
     */
    private void storeAccountAndPasswordInLocal(String user_email, String user_password,boolean is_remember) {
        SharedPreferences preferences= getSharedPreferences("storeInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("user_email",user_email);
        editor.putString("user_password", user_password);
        editor.putBoolean("is_remember",is_remember);
        editor.commit();
    }

    /**
     * 执行输入框失去焦点事件的业务逻辑
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.register_phone:
                String phone_string=register_phone.getText().toString().trim();
                if (!hasFocus&&!phone_string.equals("")&&phone_string.length()==11){
                    OkHttpClient okHttpClient=new OkHttpClient();
                    RequestBody body=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "mobile="+phone_string);
                    Request request=new Request.Builder()
                            .url(Information.BASE_URL+"/user/changer/userpwd/notlogged?mobile="+phone_string)
                            .post(body)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result=  response.body().string();
                            if ("用户不存在，请核对信息后重试".equals(result)){
                                changeDirectImage(register_phone_img,R.drawable.ok);
                                judge[0]=true;

                            }else{
                                changeDirectImage(register_phone_img,R.drawable.fail);
                                judge[0]=false;
                            }
                        }
                    });
                }else if ("".equals(phone_string)){
                    changeDirectImage(register_phone_img,R.drawable.fail);
                    judge[0]=false;
                }
                break;
            case R.id.register_email:
                String email_string=register_email.getText().toString().trim();
                Pattern pattern=Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
                Matcher matcher= pattern.matcher(email_string);
                boolean is_match= matcher.matches();
                if (!hasFocus&&!"".equals(email_string)){
                    if (is_match){
                        changeDirectImage(register_email_img,R.drawable.ok);
                        judge[1]=true;
                    }else {
                        changeDirectImage(register_email_img,R.drawable.fail);
                        judge[1]=false;
                    }
                }else if ("".equals(email_string)){
                    changeDirectImage(register_email_img,R.drawable.fail);
                    judge[1]=false;
                }

                break;

            case R.id.register_password:
                register_pass_string=register_password.getText().toString().trim();
                if (null!=register_pass_string&&""!=register_pass_string&&Pattern.compile("(?!^\\\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{8,}").matcher(register_pass_string).matches()){
                    changeDirectImage(register_password_img,R.drawable.ok);
                    judge[2]=true;
                }else {
                    changeDirectImage(register_password_img,R.drawable.fail);
                    judge[2]=false;
                }

                break;

            case R.id.register_enter_password:
                register_enter_pass_string=register_enter_password.getText().toString().trim();
                if (null!=register_enter_pass_string&&!"".equals(register_enter_pass_string)&&register_enter_pass_string.equals(register_pass_string)){
                    changeDirectImage(register_enter_password_img,R.drawable.ok);
                    judge[3]=true;
                }else {
                    changeDirectImage(register_enter_password_img,R.drawable.fail);
                    judge[3]=false;
                }

                break;
        }
        setButtonIsEnabled(register_btn,true);
        for (boolean is_pass:judge){
            if (is_pass==false){
                setButtonIsEnabled(register_btn,false);
            }
        }
    }

    /**
     * 同时设置是否可点击
     * 设置按钮的颜色
     * @param enabled
     */
    private void setButtonIsEnabled(Button btn ,boolean enabled) {
        if (enabled==true){
            btn.setBackgroundColor(Color.parseColor("#ff33b5e5"));
        }else {
            btn.setBackgroundColor(Color.parseColor("#CDC9C9"));
        }
        btn.setEnabled(enabled);
    }

    /**
     * 在主线程中修改指定inageview的图片资源
     * @param imageView
     * @param resouseid
     */
    private void changeDirectImage(final ImageView imageView ,final int resouseid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(resouseid);
            }
        });
    }
}
