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

import com.sunonline.application.R;
import com.sunonline.bean.Video;

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
public class VideoBeanAdapter extends RecyclerView.Adapter<VideoBeanAdapter.PublicTeachViewHolder> {
    private Activity context;//上下文参数，用于加载布局
    private List<Video> list;//adpter中绑定的数据
    public void setList(List<Video> list) {
        this.list = list;
    }
    private OnclickListener onClickListener;

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public VideoBeanAdapter(Activity context){
        this.context=context;
    }
    public  interface OnclickListener{
        public void onclick(List<Video> list,int Position);
    }

    class PublicTeachViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener{
        public ImageView imageView; //视频截图
        public TextView textView; //标题
        public TextView deploy_date;//发布日期
        public TextView click_num;//点击次数
        public PublicTeachViewHolder(View itemView) {
            super(itemView);
            LinearLayout linearLayout= (LinearLayout) itemView;
            FrameLayout frameLayout= (FrameLayout) linearLayout.getChildAt(0);
            imageView= (ImageView) frameLayout.getChildAt(0);
            textView= (TextView)linearLayout.getChildAt(1);
            deploy_date= (TextView) frameLayout.getChildAt(1);
            click_num= (TextView)frameLayout.getChildAt(2);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onclick(list, getLayoutPosition());
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {

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
        Video video=  list.get(position);
        String pic_url=  video.getVideoPicUrl();
        if (pic_url!=null&&!pic_url.trim().equals("")){
            OkHttpClient httpClient=new OkHttpClient();
            Request request=new Request.Builder()
                    .url(pic_url)
                    .build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "加载失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();
                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds=false;
                    options.inSampleSize=10;
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.imageView.setImageBitmap(
                                    bitmap);
                        }
                    });
                }
            });
        }
        holder.click_num.setText("播放："+String.valueOf(video.getVideoPlayedNumber()));
        holder.deploy_date.setText(video.getVideoDate());
        holder.textView.setText(video.getVideoName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
