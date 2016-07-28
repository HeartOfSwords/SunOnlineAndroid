package com.sunonline.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunonline.application.R;
import com.sunonline.bean.UserInfo;
import com.sunonline.custom_view.CircleImage;
import com.sunonline.custom_view.SlideMenu;
import com.sunonline.db.DButil;
import com.sunonline.fragment.ContentFragment;
import com.sunonline.global.Information;
import com.sunonline.util.WindowsUtill;

/**
 * 首界面显示
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private SlideMenu slideMenu; //整体的侧滑按钮
    private LinearLayout left_login_info;  //左侧的登录信息部分
    private LinearLayout left_person_info; //左侧的个人信息部分
    private Button left_menu_login; //左侧的登录按钮
    private CircleImage user_img;  //用户头像
    private TextView user_name;   //用户姓名

    private RelativeLayout left_menu_video; //视频区域
    private RelativeLayout left_menu_directoer;//新生导航区域
    private RelativeLayout left_menu_about_us;//关于我们区域
    private RelativeLayout left_menu_myinfo;//我的信息区域

    private Fragment video; //视频区域
    private Fragment new_student_director;//新生指南
    private  Fragment fragment3;//关于我们
    private  Fragment myinfo;//我的信息

    private Intent intent;
    //程序主界面的四大区域
    private LinearLayout video_area;
    private LinearLayout director_area;
    private LinearLayout about_us_area;
    private LinearLayout my_info_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.globel_main_view);

        slideMenu= (SlideMenu) findViewById(R.id.slide_menu);
        left_login_info= (LinearLayout) findViewById(R.id.left_login_info);
        left_person_info= (LinearLayout) findViewById(R.id.left_person_info);
        left_menu_login= (Button) findViewById(R.id.left_menu_login);
        user_img= (CircleImage) findViewById(R.id.user_img);
        user_name= (TextView) findViewById(R.id.user_name);

        //左边菜单部分下方的四个按钮
        left_menu_video= (RelativeLayout) findViewById(R.id.left_menu_video);
        left_menu_directoer= (RelativeLayout) findViewById(R.id.left_menu_directoer);
        left_menu_about_us= (RelativeLayout) findViewById(R.id.left_menu_about_us);
        left_menu_myinfo= (RelativeLayout) findViewById(R.id.left_menu_myinfo);
        left_menu_video.setOnClickListener(this);
        left_menu_directoer.setOnClickListener(this);
        left_menu_about_us.setOnClickListener(this);
        left_menu_myinfo.setOnClickListener(this);


        SharedPreferences preferences= getSharedPreferences("storeInfo", MODE_PRIVATE);
        final String user_email=  preferences.getString("user_email","");
        final String user_password=  preferences.getString("user_password","");
        boolean is_remember= preferences.getBoolean("is_remember", false);
        if (is_remember){
            //加载用户信息,在加载前再次登录一下进行验证用户是否存在
            new Thread(){
                @Override
                public void run() {
                    UserInfo userInfo= UpdateUserInfoActivity.connectUrl(Information.BASE_URL + "/user/login?inputString=" + user_email + "&userpwd=" + user_password, MainActivity.this);
                    if (null!=userInfo&&user_email.equals(userInfo.getUserEmail())){ //判定是否获取到真实的信息
                        Information.IS_LOGIN=true;
                        Information.userInfo=userInfo;
                        left_person_info.setVisibility(View.VISIBLE);
                        left_login_info.setVisibility(View.GONE);
                        DButil dButil=new DButil(MainActivity.this);
                        dButil.updateUserInfo(true,Information.userInfo);
                        if (null!=Information.userInfo){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    user_name.setText(Information.userInfo.getUsernickName());
                                    String userAvatar_address = Information.userInfo.getUserAvatar().trim();
                                    CircleImage.setImageResourse(MainActivity.this, "", user_img, R.drawable.c);
                                }
                            });
                        }
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switchNoLogin();
                            }
                        });
                    }
                }
            }.start();


        }else {
            switchNoLogin();
        }
        left_menu_login.setOnClickListener(this);

        //设置 右侧的内容区域的布局fragment
        ContentFragment contentFragment=new ContentFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, contentFragment, "contentFragment");
        fragmentTransaction.commit();

    }

    /**
     * 将Fragment信息隐藏
     */
    private void hideView() {
        FragmentManager fragmentManager2=getSupportFragmentManager();
        video=  fragmentManager2.findFragmentByTag("fragment1");
        new_student_director=  fragmentManager2.findFragmentByTag("new_student_director");
        fragment3=  fragmentManager2.findFragmentByTag("about_us_page");
        myinfo=  fragmentManager2.findFragmentByTag("myinfo");
        View view= fragmentManager2.findFragmentByTag("contentFragment").getView();
        video_area=(LinearLayout) view.findViewById(R.id.video_area);
        director_area=(LinearLayout) view.findViewById(R.id.director_area);
        about_us_area=(LinearLayout) view.findViewById(R.id.about_us_area);
        my_info_area=(LinearLayout) view.findViewById(R.id.my_info_area);
        FragmentTransaction transaction= fragmentManager2.beginTransaction();
        transaction.hide(video);
        transaction.hide(new_student_director);
        transaction.hide(fragment3);
        transaction.hide(myinfo);
        transaction.commit();
    }


    /**
     * 转换到未登录状态
     */
    public void switchNoLogin() {
        Information.IS_LOGIN=false;
        left_person_info.setVisibility(View.GONE);
        left_login_info.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.left_menu_login:
                intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.left_menu_video:
                slideMenu.smoothScrollTo(WindowsUtill.getScreenWidthAndHeight(this)[0] - 100, 0);
                Information.IS_MENU_OPEN=false;
                hideView();
                setChoice(1);
                break;
            case R.id.left_menu_directoer:
                slideMenu.smoothScrollTo(WindowsUtill.getScreenWidthAndHeight(this)[0] - 100, 0);
                Information.IS_MENU_OPEN=false;
                hideView();
                setChoice(2);
                break;
            case R.id.left_menu_about_us:
                slideMenu.smoothScrollTo(WindowsUtill.getScreenWidthAndHeight(this)[0] - 100, 0);
                Information.IS_MENU_OPEN=false;
                hideView();
                setChoice(3);
                break;
            case R.id.left_menu_myinfo:
                slideMenu.smoothScrollTo(WindowsUtill.getScreenWidthAndHeight(this)[0] - 100, 0);
                Information.IS_MENU_OPEN=false;
                hideView();
                setChoice(4);
                break;
        }
    }
    //设置选择的部分
    private void setChoice(int i) {
        initColor();
        FragmentManager manager=  getSupportFragmentManager();
        FragmentTransaction transaction= manager.beginTransaction();
        switch (i){
            case 1:
                transaction.show(video);
                break;
            case 2:
                transaction.show(new_student_director);
                break;
            case 3:
                transaction.show(fragment3);
                break;
            case 4:
                transaction.show(myinfo);
                break;
        }
        setChoiceColor(i);
        transaction.commit();
    }

    /**
     * 同上
     * @param i
     */
    private void setChoiceColor(int i) {
        ImageView imageView = null;
        TextView textView = null;
        switch (i){
            case 1:
                imageView= (ImageView) video_area.getChildAt(0);
                textView= (TextView) video_area.getChildAt(1);
                imageView.setImageResource(R.drawable.video_click);
                break;
            case 2:
                imageView= (ImageView) director_area.getChildAt(0);
                textView= (TextView) director_area.getChildAt(1);
                imageView.setImageResource(R.drawable.director_click);
                break;
            case 3:
                imageView= (ImageView) about_us_area.getChildAt(0);
                textView= (TextView) about_us_area.getChildAt(1);
                imageView.setImageResource(R.drawable.about_us_click);
                break;
            case 4:
                imageView= (ImageView) my_info_area.getChildAt(0);
                textView= (TextView) my_info_area.getChildAt(1);
                imageView.setImageResource(R.drawable.my_info_click);
                break;
        }

        textView.setTextColor(Color.rgb(234, 128, 16));

    }


    /**
     * 设置背景归零
     */
    private void initColor() {
        initDirect(video_area,R.drawable.video);
        initDirect(director_area, R.drawable.director);
        initDirect(about_us_area,R.drawable.about_us);
        initDirect(my_info_area,R.drawable.my_info);
    }

    /**
     *  初始化背景初始化
     * @param view
     * @param Resid
     */
    private void initDirect(ViewGroup view,int Resid) {
        ImageView imageView=null;
        TextView textView=null;
        imageView= (ImageView) view.getChildAt(0);
        textView= (TextView) view.getChildAt(1);
        imageView.setImageResource(Resid);
        textView.setTextColor(Color.rgb(255, 255, 255));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().findFragmentByTag("myinfo").onActivityResult(requestCode, resultCode, data);//消息传递
        updateUI(requestCode, data);

    }

    public void updateUserInfo(){
        CircleImage.setImageResourse(this, Information.userInfo.getUserAvatar(), user_img, R.drawable.c);
        user_name.setText(Information.userInfo.getUsernickName());
    }

    public void updateUI(int requestCode, Intent data) {
        if (null!=data&&1==requestCode){
            //执行=》 设置图像和文字，同时将账号和密码存入sharpreference中,两个地方需要更改
            Information.IS_LOGIN=true;
            UserInfo userInfo= (UserInfo) data.getSerializableExtra("UserInfo");
            Toast.makeText(MainActivity.this, userInfo.getUsernickName(), Toast.LENGTH_SHORT).show();
            left_person_info.setVisibility(View.VISIBLE);
            left_login_info.setVisibility(View.GONE);
            user_name.setText(userInfo.getUsernickName());
            String userAvatar_address=userInfo.getUserAvatar().trim();
            CircleImage.setImageResourse(this, userAvatar_address, user_img, R.drawable.c);
            //将登陆后的信息存储到数据库中
            DButil dButil=new DButil(this);
            if (dButil.userIsExists(userInfo)){
                dButil.updateLoginStatus(true,userInfo);
            }else {
                dButil.insertUserInfo(userInfo,true);
            }

        }
    }

    /**
     * 监听按键,返回键，实现提醒功能
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("确认退出？");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
