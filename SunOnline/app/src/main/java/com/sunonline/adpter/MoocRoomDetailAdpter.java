package com.sunonline.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunonline.application.R;
import com.sunonline.bean.MoocRoomDetail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 针对MoocRoomDetail类型的数据进行设置适配器
 * Created by duanjigui on 2016/7/18.
 */
public class MoocRoomDetailAdpter extends RecyclerView.Adapter <MoocRoomDetailAdpter.MoocRoomDetailHolder>{
    private Activity activity;
    private List<MoocRoomDetail> list;
    private OnclickListener onclickListener;

    public MoocRoomDetailAdpter(Activity activity) {
        this.activity = activity;
    }

    public void setList(List<MoocRoomDetail> list) {
        this.list = list;
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public  interface OnclickListener{
        public void onclick(List<MoocRoomDetail> list,int Position);
    }

    @Override
    public MoocRoomDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.public_teach_second_adpter_model,null,false);
        return new MoocRoomDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoocRoomDetailHolder holder, int position) {
        MoocRoomDetail moocRoomDetail= list.get(position);
        String pic_url=moocRoomDetail.getC_pic_url();
        OkHttpClient httpClient=new OkHttpClient();
        Request request=new Request.Builder().url(pic_url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               activity.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(activity, "信息获取失败！", Toast.LENGTH_SHORT).show();
                   }
               });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream= response.body().byteStream();
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inJustDecodeBounds=false;
                options.inSampleSize=10;
                final Bitmap bitmap= BitmapFactory.decodeStream(inputStream,null,options);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.course_icon.setImageBitmap(bitmap);
                    }
                });
            }
        });
        holder.course_intro.setText("课程名称：" + moocRoomDetail.getC_name() + "\n" + "课程介绍：" + moocRoomDetail.getC_introduce());
        holder.teacher.setText(moocRoomDetail.getC_teacher_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MoocRoomDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener{
        public ImageView course_icon;//课程图标
        public TextView teacher;//讲师名
        public TextView course_intro;//课程介绍

        public MoocRoomDetailHolder(View itemView) {
            super(itemView);
            LinearLayout warp_linner= (LinearLayout) itemView;
            LinearLayout first_warp= (LinearLayout) warp_linner.getChildAt(0);
            course_icon= (ImageView) first_warp.getChildAt(0);
            teacher= (TextView) first_warp.getChildAt(2);
            course_intro= (TextView) warp_linner.getChildAt(1);
            warp_linner.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onclickListener.onclick(list, getLayoutPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    course_intro.setTextColor(Color.GREEN);
                    break;
                case MotionEvent.ACTION_DOWN:
                    course_intro.setTextColor(Color.BLACK);
                    break;


            }
            return false;
        }

    }

}

