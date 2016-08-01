package com.sunonline.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meg7.widget.CustomShapeImageView;
import com.sunonline.activity.LoginActivity;
import com.sunonline.activity.MainActivity;
import com.sunonline.activity.UpdateUserInfoActivity;
import com.sunonline.application.R;
import com.sunonline.bean.UserInfo;
import com.sunonline.custom_view.CircleImage;
import com.sunonline.db.DButil;
import com.sunonline.global.Information;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 个人信息区域
 * Created by duanjigui on 2016/6/28.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout my_info_all; //最外面的那个界面
    private Button myinfo_login; //登录按钮
    private LinearLayout user_info_go_login_area; //用户登录区域
    private LinearLayout user_info_login_after_area;//用户信息区域
    private CustomShapeImageView my_info_area_user_image;//登录用户的头像
    private TextView my_info_area_user_name; //登录用户名
    private TextView data_time; //登录日期
    private EditText my_info_area_user_email; //用户注册邮箱
    private EditText my_info_area_user_phone;  //用户的注册账号
    private LinearLayout my_info_area_update_pass;//修改密码
    private LinearLayout my_info_area_upload_image; //上传用户头像
    private Button log_out; //注销按钮
    private ImageButton name_edit_btn;//编辑昵称按钮
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.myinfo, null,false);
        my_info_all= (RelativeLayout) view.findViewById(R.id.my_info_all);
        user_info_go_login_area= (LinearLayout) view.findViewById(R.id.user_info_go_login_area);
        myinfo_login= (Button)view.findViewById(R.id.myinfo_login);
        user_info_login_after_area= (LinearLayout) view.findViewById(R.id.user_info_login_after_area);
        my_info_area_user_image= (CustomShapeImageView) view.findViewById(R.id.my_info_area_user_image);
        my_info_area_user_name= (TextView) view.findViewById(R.id.my_info_area_user_name);
        data_time= (TextView) view.findViewById(R.id.data_time);
        my_info_area_user_email= (EditText) view.findViewById(R.id.my_info_area_user_email);
        my_info_area_user_phone= (EditText) view.findViewById(R.id.my_info_area_user_phone);
        my_info_area_update_pass= (LinearLayout) view.findViewById(R.id.my_info_area_update_pass);
        my_info_area_upload_image= (LinearLayout) view.findViewById(R.id.my_info_area_upload_image);
        name_edit_btn= (ImageButton) view.findViewById(R.id.name_edit_btn);
        log_out= (Button) view.findViewById(R.id.log_out);
        my_info_area_user_phone.setOnClickListener(this);
        my_info_area_update_pass.setOnClickListener(this);
        my_info_area_upload_image.setOnClickListener(this);
        name_edit_btn.setOnClickListener(this);
        log_out.setOnClickListener(this);
        my_info_area_user_email.setOnClickListener(this);
        my_info_area_user_email.setEnabled(false); //设置输入框不可用
        my_info_area_user_phone.setEnabled(false);//设置输入框不可用
        if (!Information.IS_LOGIN){ //当用户未登录时
            switchNoLogin();
        }else { //当用户登录后
            switchLogin();
        }
        return view;
    }

    /**
     * 转换到未登录的视图
     */
    private void switchNoLogin() {
        //my_info_all.setBackgroundColor(Color.WHITE);
        myinfo_login.setOnClickListener(this);
        user_info_go_login_area.setVisibility(View.VISIBLE);
        user_info_login_after_area.setVisibility(View.GONE);
    }

    /**
     * 转换为登录后的视图
     */
    private void switchLogin() {
        my_info_all.setBackgroundColor(Color.parseColor("#EAEAEA"));
        user_info_go_login_area.setVisibility(View.GONE);
        user_info_login_after_area.setVisibility(View.VISIBLE);
        //设置登录日期
        Date date=new Date();
        data_time.setText("日期：" + new SimpleDateFormat("yyyy-MM-dd").format(date));
        //设置用户昵称
        my_info_area_user_name.setText(Information.userInfo.getUsernickName());
        //设置用户图片
        CircleImage.setImageResourse(getActivity(),Information.userInfo.getUserAvatar(), my_info_area_user_image, R.drawable.c);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myinfo_login: //点击登录按钮
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                this.startActivityForResult(intent, 1);
                break;
            case R.id.log_out: //点击注销按钮
                switchNoLogin();
                DButil dButil=new DButil(getActivity());
                dButil.updateLoginStatus(false,Information.userInfo);
                Information.IS_LOGIN=false;
                SharedPreferences preferences=getActivity().getSharedPreferences("storeInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                editor.putBoolean("is_remember",false);
                editor.commit();
                Information.userInfo=null;
                MainActivity activity= (MainActivity) getActivity();
                activity.switchNoLogin();
                Toast.makeText(getActivity(),"注销成功！",Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent update_intent=new Intent(getActivity(), UpdateUserInfoActivity.class);
                //由于该页面只有在登录后才能修改，故此时的数据应该已经存放到一个全局的静态类中
                //当数据修改了以后，注意将全局的那个变量重新赋值
                this.startActivityForResult(update_intent,2);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainActivity activity= (MainActivity) getActivity();
        activity.updateUI(requestCode,data);
        //上面部分代码的作用其实就是用来在frgment和activity中进行消息传递，同步更新UI
        if (data!=null&&1==requestCode){
            //执行=》 设置图像和文字，同时将账号和密码存入sharpreference中,两个地方需要更改
            Information.IS_LOGIN=true;
            UserInfo userInfo= (UserInfo) data.getSerializableExtra("UserInfo");
            Information.userInfo=userInfo;
            switchLogin();
        }else if (requestCode==2){ //修改信息成功后执行
            my_info_area_user_name.setText(Information.userInfo.getUsernickName());
            my_info_area_user_email.setText(Information.userInfo.getUserEmail());
            CircleImage.setImageResourse(getActivity(), Information.userInfo.getUserAvatar(), my_info_area_user_image, R.drawable.c);
            my_info_area_user_email.setText(Information.userInfo.getUserEmail());
            MainActivity activity2= (MainActivity) getActivity();
            activity2.updateUserInfo();
        }
    }

}
