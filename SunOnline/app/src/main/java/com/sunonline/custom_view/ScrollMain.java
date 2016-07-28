package com.sunonline.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by duanjigui on 2016/7/17.
 */
public class ScrollMain extends ScrollView {
    public ScrollMain(Context context) {
        super(context);
    }

    public ScrollMain(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollMain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("ScrollMain","onTouchEvent");
        return super.onTouchEvent(ev);
    }
}
