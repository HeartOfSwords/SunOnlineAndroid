package com.sunonline.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.sunonline.application.R;
import com.sunonline.global.Information;
import com.sunonline.util.WindowsUtill;

/**
 * 自定义水平滚动条
 * Created by duanjigui on 2016/7/6.
 */
public class SlideMenu extends HorizontalScrollView implements View.OnClickListener{
    private int ScreenWidth;//屏幕的宽度
    private int ScreenHeight;//屏幕的高度
    private int right_position=100; //右边屏幕的距离
    private int menu_width; //菜单的宽度
    private LinearLayout left_menu; //左边菜单部分
    private LinearLayout main_content;//右侧内容部分
    private ImageView menu_image;  //点击菜单按钮

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] scrren_value=  WindowsUtill.getScreenWidthAndHeight(context);
        ScreenWidth=scrren_value[0];
        ScreenHeight=scrren_value[1];
        //设置默认主界面显示的视图【fragment1，new_student_director】
    }


    //测量子控件设置的大小，测量自己的长度和宽度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout warp_linnerlayout=(LinearLayout) getChildAt(0);
        left_menu=(LinearLayout) warp_linnerlayout.getChildAt(0); //左侧菜单部分布局
        main_content= (LinearLayout) warp_linnerlayout.getChildAt(1);//右侧按钮部分布局
       menu_image= (ImageView) main_content.findViewById(R.id.menu_image);
        menu_image.setOnClickListener(this);
        //设置了左边菜单部分的大小
        ViewGroup.LayoutParams left_menu_param=  left_menu.getLayoutParams();
        menu_width=ScreenWidth-right_position;
        left_menu_param.width=menu_width;
        left_menu_param.height= ViewGroup.LayoutParams.MATCH_PARENT;
        //设置了右边内容部分的大小
        ViewGroup.LayoutParams right_content_param= main_content.getLayoutParams();
        right_content_param.width=ScreenWidth;  //设置了内容的宽度为屏幕的宽度
        right_content_param.height= ViewGroup.LayoutParams.MATCH_PARENT;
    }

    //设置一下子元素摆放方位
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.scrollTo(menu_width, 0);//将内容部分正常显示
    }

    /**
     *   处理滑动事件  自上向下传递
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
             switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                int flex=  this.getScrollX(); //获取水平偏移距离
                if (flex>menu_width/2){
                    this.smoothScrollTo(menu_width,0);
                    Information.IS_MENU_OPEN=false;

                } else {
                    this.smoothScrollTo(0,0);
                    Information.IS_MENU_OPEN=true;
                }

                return false;
            case MotionEvent.ACTION_DOWN:
                break;
            default:
                break;


        }
        return super.onTouchEvent(ev);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / menu_width; // 1 ~ 0

        /**
         * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        // 调用属性动画，设置TranslationX
       ViewHelper.setTranslationX(left_menu, menu_width * scale * 0.8f);//梯度变化的颜色

//        ViewHelper.setScaleX(left_menu, leftScale);
//        ViewHelper.setScaleY(left_menu, leftScale);
          ViewHelper.setAlpha(left_menu, leftAlpha);
        // 设置content的缩放的中心点
//        ViewHelper.setPivotX(main_content, 0);
//        ViewHelper.setPivotY(main_content, main_content.getHeight() / 2);
//        ViewHelper.setScaleX(main_content, rightScale);
//        ViewHelper.setScaleY(main_content, rightScale);
       // ViewHelper.setAlpha(main_content,rightScale);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_image:
                if (!Information.IS_MENU_OPEN){ //如果打开则关闭
                    this.smoothScrollTo(0,0);
                    Information.IS_MENU_OPEN=true;
                }else {//如果关闭则打开
                    this.smoothScrollTo(menu_width,0);
                    Information.IS_MENU_OPEN=false;
                }
                break;
            default:
                Log.d("slideMenu","click");
        }
    }

    /**
     * 每个手势事件必调用该方法
     *
     * 拦截手势事件  自下向上传递
     * 决定是否传递给自身的ontouch事件还是子部件的onTouch事件
     *
     * 流程：先执行onInterceptTouchEvent 然后在执行 onTouch
     *
     * 根据onInterceptTouchEvent的返回值来确定传递给自身的onTouch还是子部件的onTouch事件
     *
     * 事件必是从底层子部件传递给上层控件
     *
     * 在viewGroup中才定义的
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
     //返回false会传递给子部件
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
      //  getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
