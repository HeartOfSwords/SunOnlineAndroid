package com.sunonline.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunonline.application.R;

/**   右边部分的内容页部分
 * 主布局部分的代码
 * Created by duanjigui on 2016/7/6.
 */
public class ContentFragment extends Fragment implements View.OnClickListener{
    private LinearLayout video_area;
    private LinearLayout director_area;
    private LinearLayout about_us_area;
    private LinearLayout my_info_area;
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;
    public ContentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_body_bottom,null,false);
        init(view);
        return view;
    }
    //初始化布局及事件
    public void init(View view){
        //初始化布局
        video_area=(LinearLayout) view.findViewById(R.id.video_area);
        director_area=(LinearLayout) view.findViewById(R.id.director_area);
        about_us_area=(LinearLayout) view.findViewById(R.id.about_us_area);
        my_info_area=(LinearLayout) view.findViewById(R.id.my_info_area);
        //初始化fragement
        fragment1=new MainHorizationScrollFragment();
        fragment2=new StudentDriectorFragment();
        fragment3=new AboutUsFragment();
        fragment4=new MyInfoFragment();
        //初始化事件
        video_area.setOnClickListener(this);
        director_area.setOnClickListener(this);
        about_us_area.setOnClickListener(this);
        my_info_area.setOnClickListener(this);
        setChoice(1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.video_area :
                setChoice(1);
                break;
            case R.id.director_area :
                setChoice(2);
                break;
            case R.id.about_us_area :
                setChoice(3);
                break;
            case R.id.my_info_area :
                setChoice(4);
                break;
        }
    }
    //设置选中的选项卡
    public void setChoice(int i){
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        initHideFragment(fragmentManager, fragmentTransaction);
        clearAllColor();
        switch (i){
            case 1:
                fragmentTransaction.show(fragment1);
                break;
            case 2:
                fragmentTransaction.show(fragment2);
                break;
            case 3:
                fragmentTransaction.show(fragment3);
                break;
            case 4:
                fragmentTransaction.show(fragment4);
                break;
        }
        setChoiceColor(i);
        fragmentTransaction.commit();
    }

    /**
     * 隐藏其它的fragment
     * @param fragmentManager
     * @param fragmentTransaction
     */
    private void initHideFragment(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction) {
        if( fragmentManager.findFragmentByTag("fragment1")==null){
            fragmentTransaction.add(R.id.fragement,fragment1,"fragment1");
        }
        if( fragmentManager.findFragmentByTag("new_student_director")==null){
            fragmentTransaction.add(R.id.fragement,fragment2,"new_student_director");
        }
        if( fragmentManager.findFragmentByTag("about_us_page")==null){
            fragmentTransaction.add(R.id.fragement,fragment3,"about_us_page");
        }
        if( fragmentManager.findFragmentByTag("myinfo")==null){
            fragmentTransaction.add(R.id.fragement,fragment4,"myinfo");
        }
        fragmentTransaction.hide(fragment1);
        fragmentTransaction.hide(fragment2);
        fragmentTransaction.hide(fragment3);
        fragmentTransaction.hide(fragment4);
    }

    /**
     * 清除所有选项的样式，设置为初始样式
     */
    private void clearAllColor() {
       initDirect(video_area,R.drawable.video);
        initDirect(director_area, R.drawable.director);
        initDirect(about_us_area,R.drawable.about_us);
        initDirect(my_info_area,R.drawable.my_info);
    }

    /**
     * 初始化指定的底部选项
     * @param view  视图view
     * @param Resid  图片资源
     */
    private void initDirect(ViewGroup view,int Resid) {
        ImageView imageView=null;
        TextView textView=null;
        imageView= (ImageView) view.getChildAt(0);
        textView= (TextView) view.getChildAt(1);
        imageView.setImageResource(Resid);
        textView.setTextColor(Color.rgb(255, 255, 255));
    }

    /**
     * 设置底部被选中的区域颜色
     * @param i  当点击第i个区域时
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
}
