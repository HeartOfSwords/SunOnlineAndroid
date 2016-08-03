package com.sunonline.adpter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sunonline.application.R;
import com.sunonline.bean.Video;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 图片缓存核心是：取图片时去缓存中下载，没有的话在下载图片，完成后将其加入到缓存中
 *
 * 关于公益课堂recycleview的适配器
 * Created by duanjigui on 2016/7/15.
 */
public class VideoBeanAdapter extends RecyclerView.Adapter<VideoBeanAdapter.PublicTeachViewHolder> {
    private Activity context;//上下文参数，用于加载布局
    private List<Video> list;//adpter中绑定的数据
    private LruCache<String,Bitmap> cache; //图片缓存区
    private boolean is_first=true;  //判断是否为第一次加载
    private Set<MyTask> taskCollection;//任务集合
    public void setList(List<Video> list) {
        this.list = list;
    }
    private OnclickListener onClickListener;
    private RecyclerView recyclerView;  //具体的recycle
    private  int first_item;//第一个可见的item
    private int last_item; //最后一个可见的item

    public Bitmap getCacheFromMemory(String image_url){
        return  cache.get(image_url);
    }

    public void setCacheFromDownload(String image_url,Bitmap bitmap){
        if (cache.get(image_url)==null){  //目的是防止重名
            cache.put(image_url,bitmap);
        }
    }

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public VideoBeanAdapter(Activity context){
        this.context=context;
    }

    public VideoBeanAdapter(Activity context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public  interface OnclickListener{
        public void onclick(List<Video> list,int Position);
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
        View view= LayoutInflater.from(context).inflate(R.layout.recycleview_sample, null, false);
        return new PublicTeachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicTeachViewHolder holder, int position) {
        Video video=  list.get(position);
        final String pic_url=  video.getVideoPicUrl();
        SimpleTarget<Bitmap> target=new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (null!=pic_url){
                    holder.load.setVisibility(View.INVISIBLE);
                    holder.imageView.setImageBitmap(resource);
                }

            }
        };
        if (pic_url!=null&&!pic_url.trim().equals("")){

            Glide.with(context)
                    .load(pic_url)
                    .asBitmap()
                    .override(100, 50)
                    .into(target);
        }
        holder.click_num.setText("播放："+String.valueOf(video.getVideoPlayedNumber()));
        holder.deploy_date.setText(video.getVideoDate());
        holder.textView.setText(video.getVideoName());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class VideoBeanScrollListener extends RecyclerView.OnScrollListener {

        public VideoBeanScrollListener() {
        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState==RecyclerView.SCROLL_STATE_IDLE){ //当滚动停止时，加载数据
                loadImage(first_item,last_item);
            }else {
                cancelAllTask();
            }


        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            GridLayoutManager manager= (GridLayoutManager) recyclerView.getLayoutManager();
            first_item=  manager.findFirstVisibleItemPosition();
            last_item=manager.findLastVisibleItemPosition();
            if (is_first&&manager.getItemCount()>0){
                loadImage(first_item,last_item);
                is_first=false;
            }

        }
    }
    //取消所有任务
    public void cancelAllTask() {
        for (MyTask task:taskCollection){
            task.cancel(true);
        }
    }
    //加载指定部分的图片
    private void loadImage(int first_item, int last_item) {

        for (int i=first_item;i<last_item;i++){
          String pic_url= list.get(i).getVideoPicUrl();//获取指定部分图片的url
          Bitmap bitmap=  getCacheFromMemory(pic_url);
          if (null!=bitmap){ //当缓存中存在图片的话
              ImageView imageView= (ImageView) recyclerView.findViewWithTag(pic_url);
              if (null!=imageView&&null!=bitmap){
                  imageView.setImageBitmap(bitmap);
              }
          }else { //如果没有的话就去下载
                MyTask myTask=new MyTask();
                taskCollection.add(myTask);
                myTask.execute(pic_url);
          }

        }

    }

    class MyTask extends AsyncTask<String,Void,Bitmap> {

        private String image_url;//图片url


        /**
         * 通过构造参数将信息传递到后台执行
         * 多线程，异步执行，在比较耗时的操作上使用，联网下载
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {

            image_url=params[0];
            Bitmap bitmap= null;
            try {
                bitmap = downloadImage(params[0]);
                if (null!=image_url&&null!=bitmap){
                    setCacheFromDownload(image_url,bitmap); //存储到缓存中
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        /**
         * 下载图片
         * @param image_url
         * @return
         */
        private Bitmap downloadImage(String image_url) throws IOException {
            Bitmap bitmap=null;
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder()
                    .get()
                    .url(image_url)
                    .build();
            Response respone= client.newCall(request).execute();
            InputStream input= respone.body().byteStream();
            bitmap=BitmapFactory.decodeStream(input);
            return bitmap;
        }

        /**
         * 当下载完成后调用
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView view= (ImageView) recyclerView.findViewWithTag(image_url);
            if (null!=view&&bitmap!=null){
                view.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }
    }
}
