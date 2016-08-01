package com.sunonline.adpter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meg7.widget.CustomShapeImageView;
import com.sunonline.application.R;
import com.sunonline.bean.ReviewBean;
import com.sunonline.util.WindowsUtill;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 评论部分的adpter
 * Created by duanjigui on 2016/7/28.
 */
public class ReviewAdpter extends RecyclerView.Adapter<ReviewAdpter.MyHolder> {
    private Activity context;
    private List<ReviewBean> list;
    private GoodsCallBack goodsCallBack;
    private ReviewCallBack reviewCallBack;
    public void setGoodsCallBack(GoodsCallBack goodsCallBack) {
        this.goodsCallBack = goodsCallBack;
    }

    public void setReviewCallBack(ReviewCallBack reviewCallBack) {
        this.reviewCallBack = reviewCallBack;
    }

    public  interface ReviewCallBack{
        public void callBack(LinearLayout review_area);
    }

    public  interface GoodsCallBack{
        public void callBack(LinearLayout goog_area, int position, List<ReviewBean> list);
    }

    public ReviewAdpter(Activity context, List<ReviewBean> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.review_model,null,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.persion_phone.setText(list.get(position).getPhone());
        holder.persion_review_area.setWidth(WindowsUtill.getScreenWidthAndHeight(context)[0]);
        holder.persion_review_area.setText(list.get(position).getReview_text());
        holder.goods_num.setText(String.valueOf(list.get(position).getGood_num()));
        holder.send_date.setText(list.get(position).getSend_data());
        holder.layer_num.setText((list.size()-position)+"楼");
        final String url=list.get(position).getImage_url();
        new Thread(){
            @Override
            public void run() {
                OkHttpClient httpClient=new OkHttpClient();
                Request request=new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream= response.body().byteStream();
                        final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.persion_head_image.setImageBitmap(bitmap);
                            }
                        });
                    }
                });


            }
        }.start();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout warp_group;//最外层包裹的那一层
        public LinearLayout persion_info; //个人信息展示部分
        public TextView persion_review_area;//信息评论部分
        public CustomShapeImageView persion_head_image;//头像部分
        private TextView layer_num;//层数
        public TextView persion_phone;//手机号码
        public TextView send_date;//距离上次发送的日期
        public LinearLayout info_area;//信息区域
        public LinearLayout operater_area; //操作区
        public LinearLayout review_area; //评论按钮
        public LinearLayout good_area; //赞按钮
        private TextView goods_num;//赞的数量
        public MyHolder(View itemView) {
            super(itemView);
            warp_group= (LinearLayout) itemView;
            persion_info= (LinearLayout) warp_group.getChildAt(0);
            persion_review_area= (TextView) warp_group.getChildAt(1);
            persion_head_image= (CustomShapeImageView) persion_info.getChildAt(0);
            info_area= (LinearLayout) persion_info.getChildAt(1);
            layer_num= (TextView) persion_info.getChildAt(2);
            persion_phone= (TextView) info_area.getChildAt(0);
            send_date= (TextView) info_area.getChildAt(1);
            operater_area= (LinearLayout) warp_group.getChildAt(2);
            review_area= (LinearLayout) operater_area.getChildAt(0);
            good_area= (LinearLayout) operater_area.getChildAt(1);
            goods_num= (TextView) good_area.getChildAt(1);
            review_area.setOnClickListener(this);
            good_area.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.goods_area:
                    if (null!=goodsCallBack){
                        goodsCallBack.callBack((LinearLayout) v,getLayoutPosition(),list);
                    }
                    break;
                case R.id.review_area:
                    if (null!=reviewCallBack){
                        reviewCallBack.callBack((LinearLayout) v);
                    }
                    break;
            }

        }
    }

}
