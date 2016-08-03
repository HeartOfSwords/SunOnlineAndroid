package com.sunonline.adpter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sunonline.application.R;
import com.sunonline.bean.MoocRecom;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 关于公益课堂recycleview的适配器
 * Created by duanjigui on 2016/7/15.
 */
public class MoocRecomAdapter extends RecyclerView.Adapter<MoocRecomAdapter.PublicTeachViewHolder> {
    private Activity context;//上下文参数，用于加载布局
    private List<MoocRecom> list;//adpter中绑定的数据
    public void setList(List<MoocRecom> list) {
        this.list = list;
    }
    private OnclickListener onClickListener;

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnclickListener{
        public void onclick(List<MoocRecom> list,int Position);
    }


    public MoocRecomAdapter(Activity context){
        this.context=context;
    }
    class PublicTeachViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener{
        public ImageView imageView; //视频截图
        public TextView textView; //标题
        public TextView deploy_date;//发布日期
        public TextView click_num;//点击次数
        public LinearLayout load;//加载中区域
        public PublicTeachViewHolder(View itemView) {
            super(itemView);
            LinearLayout linearLayout= (LinearLayout) itemView;
            FrameLayout frameLayout= (FrameLayout) linearLayout.getChildAt(0);
            imageView= (ImageView) frameLayout.getChildAt(0);
            textView= (TextView)linearLayout.getChildAt(1);
            deploy_date= (TextView) frameLayout.getChildAt(1);
            click_num= (TextView)frameLayout.getChildAt(2);
            load= (LinearLayout) frameLayout.getChildAt(3);
            linearLayout.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Log.e("PublicTeachViewHolder", "is click");
            onClickListener.onclick(list, getLayoutPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("PublicTeachViewHolder","onTouch");
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    textView.setTextColor(Color.GREEN);
                    break;
                case MotionEvent.ACTION_DOWN:
                    textView.setTextColor(Color.BLACK);
                    break;


            }
            return false;
        }
    }


    @Override
    public PublicTeachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycleview_sample,null,false);
        return new PublicTeachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicTeachViewHolder holder, int position) {
        MoocRecom moocRecom=  list.get(position);
        Log.d("message",moocRecom.getCl_name());
        String pic_url=  moocRecom.getCl_pic_url();
        SimpleTarget<Bitmap> target=new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.load.setVisibility(View.INVISIBLE);
                holder.imageView.setImageBitmap(resource);
            }
        };
        if (pic_url!=null&&!pic_url.trim().equals("")){
            Glide.with(context)
                    .load(pic_url)
                    .asBitmap()
                    .override(100, 50)
                    .into(target);

        }
        holder.click_num.setText("播放：" + String.valueOf(moocRecom.getCl_play_time()));
        holder.deploy_date.setText(moocRecom.getCl_upload_time());
        holder.textView.setText(moocRecom.getCl_name());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
