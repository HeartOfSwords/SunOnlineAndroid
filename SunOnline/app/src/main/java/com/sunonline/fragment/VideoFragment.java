package com.sunonline.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sunonline.adpter.MoocRecomAdapter;
import com.sunonline.adpter.VideoBeanAdapter;
import com.sunonline.application.R;
import com.sunonline.custom_view.ScrollViewPager;
import com.sunonline.global.Information;
import com.sunonline.util.JsonParserUtil;
import com.sunonline.util.PaserMoocRoomJson;
import com.sunonline.util.PaserVideoJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主内容区1
 * 即视频选项卡区域
 * Created by duanjigui on 2016/6/28.
 */
public class VideoFragment extends Fragment implements View.OnTouchListener{
    private ScrollViewPager scroll_image_viewpager;
    private RecyclerView public_teach;//公益课堂
    private RecyclerView video_driver;//老司机
    private RecyclerView higo_college;//higo大学季
    private RecyclerView colloge_most_voice;//高校最强音
    private List<View> list=new ArrayList<>();
    private int i=0;//计数器
    private Timer timer=null;//定时器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.video_display, null,false);
        initFindView(view);
        initRecycleAdpter();
        initListView(); //初始化listview
        scroll_image_viewpager.setAdapter(new ScrollPageAdapter());
        scrollView();
        return view;
    }

    /**
     * 设置图片进行轮播
     */
    private void scrollView() {
        timer=new Timer();
        new Thread(){
            @Override
            public void run() {
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        if (!Information.IS_MENU_OPEN){//当菜单未打开时滚动
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    scroll_image_viewpager.setCurrentItem(i%3);
                                    i++;
                                }
                            });
                        }
                    }
                };

                timer.schedule(timerTask,0,5000);
            }
        }.start();
    }

    /**
     * 对控件进行赋值，初始化
     * @param view
     */
    private void initFindView(View view) {
        scroll_image_viewpager= (ScrollViewPager) view.findViewById(R.id.scroll_image_viewpager);
        public_teach= (RecyclerView) view.findViewById(R.id.public_teach);
        video_driver= (RecyclerView) view.findViewById(R.id.video_driver);
        higo_college= (RecyclerView) view.findViewById(R.id.higo_college);
        colloge_most_voice= (RecyclerView) view.findViewById(R.id.colloge_most_voice);
    }

    /**
     * 为recycleview设置adpter并加载数据
     */
    private void initRecycleAdpter() {
        String public_teach_url="http://139.129.221.162/webapi/mooc/index/recommendation";
        JsonParserUtil parserUtil=new JsonParserUtil(getActivity());
        parserUtil.setPaserJson(new PaserMoocRoomJson(getActivity()));
        parserUtil.paser(public_teach_url, public_teach, new MoocRecomAdapter(getActivity()));

        String video_url="http://139.129.221.162/webapi/videos/olddriver/all/recommendation";
        JsonParserUtil parserUtil_2=new JsonParserUtil(getActivity());
        parserUtil_2.setPaserJson(new PaserVideoJson(getActivity()));
        parserUtil_2.paser(video_url,video_driver,new VideoBeanAdapter(getActivity()));

        String higo_url="http://139.129.221.162/webapi/videos/higovideo/all/recommendation";
        JsonParserUtil parserUtil_3=new JsonParserUtil(getActivity());
        parserUtil_3.setPaserJson(new PaserVideoJson(getActivity()));
        parserUtil_3.paser(higo_url, higo_college, new VideoBeanAdapter(getActivity()));

        String highSchoolVoice="http://139.129.221.162/webapi/videos/collegevoice/all/recommendation";

        JsonParserUtil parserUtil_4=new JsonParserUtil(getActivity());
        parserUtil_4.setPaserJson(new PaserVideoJson(getActivity()));
        parserUtil_4.paser(highSchoolVoice, colloge_most_voice, new VideoBeanAdapter(getActivity()));
    }


    /**
     * 初始化轮播list视图的集合
     */
    private void initListView() {
        View view1= LayoutInflater.from(getActivity()).inflate(R.layout.scrollview1, null);
        View view2= LayoutInflater.from(getActivity()).inflate(R.layout.scrollview2,null);
        View view3= LayoutInflater.from(getActivity()).inflate(R.layout.scrollview3,null);
        list.add(view1);
        list.add(view2);
        list.add(view3);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("public_teach", "onTouch");
        return false;
    }



    //轮播的adpter
    class ScrollPageAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view=  list.get(position);
            ViewGroup parent= (ViewGroup) view.getParent();   //解决子布局与父布局之间的关联嵌套问题
            if (parent!=null){
                parent.removeAllViews();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }

    @Override
    public void onPause() {
        Log.d("fragment", "pasuse has execute!");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("message1","onResume");
    }

}
