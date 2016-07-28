package com.sunonline.fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunonline.application.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里还差一个滚动条和界面相互绑定
 *
 * 主界面上方的水平滚动条，
 * 随着上方滚动条的滚动，下方的内容也发生变化
 *
 * 在这里实现菜单、页面的联动
 * Created by duanjigui on 2016/7/15.
 */
public class MainHorizationScrollFragment extends Fragment implements View.OnClickListener{
    private ViewPager viewPager;
    private List<Fragment> list=new ArrayList<>();
    private Fragment videoFrgment; //推荐视频部分
    private Fragment publicTeachFragment;//公益讲堂部分
    private  OlddriverFragment olddriverFragment;//影视老司机部分
    private HigoCollegeFragment higoCollegeFragment;//higo大学季部分
    private HigoVoiceFragment higoVoiceFragment;//高校最强音部分
    private HorizontalScrollView horizontalScrollView;//水平滚动菜单
    private TextView recmmond_video;//推荐视频
    private TextView public_class;//公益课堂
    private TextView video_oldDriver;//影视老司机
    private TextView higo_college;//HIGO大学季
    private TextView highschool_voice;//高校最强音
    private TextView highschool_person;//高校人物风采录
    private TextView highschool_image;//高校图片库

    private int [] width=new int[7]; //水平滚动条每个子按钮的宽度
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("message", "onCreate");
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("message","onCreateView");
        View view= inflater.inflate(R.layout.content_menu_part, null);
        init_view(view);
        mearserdWidth(recmmond_video, 0);
        mearserdWidth(public_class,1);
        mearserdWidth(video_oldDriver,2);
        mearserdWidth(higo_college,3);
        mearserdWidth(highschool_voice,4);
        mearserdWidth(highschool_person,5);
        mearserdWidth(highschool_image,6);
        initPagerView();
        viewPager.setAdapter(new HorizationViewPageAdpter(getActivity().getSupportFragmentManager(), list));
        viewPager.setCurrentItem(0);
        return view;
    }

    /**
     * 测量控件的高度
     * 由于在oncreate（）方法时onDraw（）没有被调用，所以需要在其绘制完以后再调用
     * @param textView
     * @param i
     */
    private void mearserdWidth(final TextView textView, final int i) {
      ViewTreeObserver observer= textView.getViewTreeObserver();
      observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          boolean hasMesured=false;
          @Override
          public boolean onPreDraw() {
              if (!hasMesured){
                 width[i]=textView.getMeasuredWidth()+10;
                 hasMesured=true;
             }
              return true;
          }
      });
    }

    /**
     * 加载数据
     * @param view
     */
    private void init_view(View view) {
        viewPager= (ViewPager) view.findViewById(R.id.page);
        horizontalScrollView= (HorizontalScrollView) view.findViewById(R.id.scrollview_menu);
        LinearLayout linearLayout= (LinearLayout) horizontalScrollView.getChildAt(0);
        recmmond_video= (TextView) linearLayout.getChildAt(0);
        public_class= (TextView) linearLayout.getChildAt(1);
        video_oldDriver= (TextView) linearLayout.getChildAt(2);
        higo_college= (TextView) linearLayout.getChildAt(3);
        highschool_voice= (TextView) linearLayout.getChildAt(4);
        highschool_person= (TextView) linearLayout.getChildAt(5);
        highschool_image= (TextView) linearLayout.getChildAt(6);
        recmmond_video.setOnClickListener(this);
        public_class.setOnClickListener(this);
        video_oldDriver.setOnClickListener(this);
        higo_college.setOnClickListener(this);
        highschool_voice.setOnClickListener(this);
        highschool_person.setOnClickListener(this);
        highschool_image.setOnClickListener(this);
        //测量子元素距离
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                clearScrollTextColor();
                switch (position){
                    case 0:
                        horizontalScrollView.smoothScrollTo(0,0);
                        recmmond_video.setTextColor(Color.rgb(153,204,0));
                        break;
                    case 1:
                        horizontalScrollView.smoothScrollTo(width[0],0);
                        public_class.setTextColor(Color.rgb(153,204,0));
                        break;
                    case 2:
                        horizontalScrollView.smoothScrollTo(width[0]+width[1],0);
                        video_oldDriver.setTextColor(Color.rgb(153,204,0));
                        break;
                    case 3:
                        horizontalScrollView.smoothScrollTo(width[0]+width[1]+width[2],0);
                        higo_college.setTextColor(Color.rgb(153,204,0));
                        break;
                    case 4:
                        horizontalScrollView.smoothScrollTo(width[0]+width[1]+width[2]+width[3],0);
                        highschool_voice.setTextColor(Color.rgb(153,204,0));
                        break;
                    case 5:
                        horizontalScrollView.smoothScrollTo(width[0]+width[1]+width[2]+width[3]+width[4],0);
                        highschool_person.setTextColor(Color.rgb(153,204,0));
                        break;
                    case 6:
                        horizontalScrollView.smoothScrollTo(width[0]+width[1]+width[2]+width[3]+width[4]+width[5],0);
                        highschool_image.setTextColor(Color.rgb(153,204,0));
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化viewpager中的视图数据
     */
    private void initPagerView() {
        videoFrgment=new VideoFragment();
        list.add(videoFrgment);
        //主界面下的那些模块的内容直接加在这里就可以了
        publicTeachFragment=new PublicTeachFragment();
        list.add(publicTeachFragment);
        olddriverFragment=new OlddriverFragment();
        list.add(olddriverFragment);
        higoCollegeFragment=new HigoCollegeFragment();
        list.add(higoCollegeFragment);
        higoVoiceFragment=new HigoVoiceFragment();
        list.add(higoVoiceFragment);
    }

    @Override
    public void onClick(View v) {
        clearScrollTextColor();
        switch (v.getId()){
            case R.id.recommed_video_text:
                recmmond_video.setTextColor(Color.rgb(153, 204, 0));
                //切换fragment关联

                viewPager.setCurrentItem(0);
                break;
            case R.id.public_class_text:
                public_class.setTextColor(Color.rgb(153,204,0));
                horizontalScrollView.smoothScrollTo((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics()),0);
                viewPager.setCurrentItem(1);
                break;
            case R.id.video_oldDriver_text:
                video_oldDriver.setTextColor(Color.rgb(153,204,0));
                viewPager.setCurrentItem(2);
                break;
            case R.id.higo_college_text:
                higo_college.setTextColor(Color.rgb(153,204,0));
                viewPager.setCurrentItem(3);
                break;
            case R.id.highschool_voice_text:
                highschool_voice.setTextColor(Color.rgb(153,204,0));
                viewPager.setCurrentItem(4);
                break;
            case R.id.highschool_person_text:
                highschool_person.setTextColor(Color.rgb(153,204,0));

                break;
            case R.id.highschool_image_text:
                highschool_image.setTextColor(Color.rgb(153,204,0));

                break;
            default:
                Toast.makeText(getActivity(),"错误！",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 将选项卡的字体颜色设置为默认颜色
     */
    private void clearScrollTextColor() {
        recmmond_video.setTextColor(Color.BLACK);
        public_class.setTextColor(Color.BLACK);
        video_oldDriver.setTextColor(Color.BLACK);
        higo_college.setTextColor(Color.BLACK);
        highschool_voice.setTextColor(Color.BLACK);
        highschool_person.setTextColor(Color.BLACK);
        highschool_image.setTextColor(Color.BLACK);
    }


    class HorizationViewPageAdpter extends FragmentStatePagerAdapter {
        public HorizationViewPageAdpter(FragmentManager fm, List<Fragment> list) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }

    @Override
    public void onDetach() {
        Log.d("message","onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d("message","onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("message","onResume");
        Log.d("message", String.valueOf(list.size()));
        super.onResume();
    }

}
