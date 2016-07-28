package com.sunonline.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by duanjigui on 2016/7/18.
 */
public class Mydivider extends RecyclerView.ItemDecoration{

    private int[] ATTRS=new int[]{android.R.attr.listDivider};  //系统默认的下划线样式

    private Drawable drawable;

    public Mydivider(Context context){
        TypedArray typearray= context.obtainStyledAttributes(ATTRS);
        drawable=  typearray.getDrawable(0);
        typearray.recycle();
    }

    /**
     *  画下划线【从子控件的底部开始到子控件的底部+线条自身的高度】
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child  //获取控件本身具有的一些属性
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin; //控件相对于父布局的高度
            final int bottom = top + drawable.getIntrinsicHeight();  //固有的高度


            drawable.setBounds(left, top, right, bottom);  //绘制控件的大小
            drawable.draw(c);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
    //设置两个item之间的距离
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0,0,drawable.getIntrinsicHeight());
        super.getItemOffsets(outRect, view, parent, state);
    }
}
