package com.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunonline.application.R;
import com.sunonline.bean.Video;
import com.sunonline.util.JsonUtill;
import com.sunonline.util.NetUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanjigui on 2016/7/8.
 */
public class testActivity extends Activity {
    private RecyclerView recyclerView;
    private List<Video> list=new ArrayList<>(); //数据源
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        recyclerView=(RecyclerView)findViewById(R.id.recyview);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager2=  new GridLayoutManager(testActivity.this,2);
        RecyclerView.LayoutManager manager3=  new StaggeredGridLayoutManager(10,StaggeredGridLayoutManager.HORIZONTAL);  //流式布局
        recyclerView.setLayoutManager(manager);  //设置布局【内部以什么形式存在】
        initdata();
       // recyclerView.addItemDecoration(new mydivider(testActivity.this));//自定义分割线
        Myadpater adpter=new Myadpater();
        adpter.setClickListener(new clickListener() {
            @Override
            public void click(int position) {
                Toast.makeText(testActivity.this,"click"+position,Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adpter); //设置适配器
    }
    //初始化数据
    private void initdata() {
        final String url="http://192.168.7.117:8080/SunonlineWebSiteAndWebservice/webapi/videos/higovideo/all";
        NetUtil netutill=new NetUtil(testActivity.this);
        netutill.getStringInformationFromNet(url, "GET",false,false);
        netutill.setOnValueRecived(new NetUtil.OnValueRecived() {
            @Override
            public void recieve(String stringInformationFromNet, InputStream inputStream) {
                JsonUtill jsonutill=new JsonUtill();
                list=  jsonutill.fetchVideoAllInfo(stringInformationFromNet);
            }

            @Override
            public void recieveBitmap(Bitmap bitmap) {

            }
        });



    }

    //自定义响应事件
    interface clickListener{
        public  void click(int position);
    }
    class Myadpater extends RecyclerView.Adapter<Myadpater.MyviewHodler>{

        private clickListener listener;
        //自定义事件在adpter中设置
        public void setClickListener(clickListener listener){
            this.listener=listener;
        }
        public Myadpater() {
            super();
        }

        //重新继承ViewHolder类
        //自己使用的view,初始化布局界面
        //自己设置的监听回调方法，可以再viewHolder类中去继承实现，即获取布局的时候去设置监听事件
        //通过去调用click事件的方法，去显示的调用其它的回调函数
        class MyviewHodler extends RecyclerView.ViewHolder implements View.OnClickListener{


            private TextView textview;  //布局文件中的部件
            private ImageView imageview;
            private LinearLayout linelayout;

            public MyviewHodler(View itemView) {
                super(itemView);
                linelayout= (LinearLayout) itemView;
                textview= (TextView) linelayout.getChildAt(1);
                imageview=(ImageView) linelayout.getChildAt(0);
                linelayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (null!=listener){
                    listener.click(getLayoutPosition());
                }
            }
        }
        //要加载的数目
        @Override
        public int getItemCount() {
            return list.size();
        }

        //与指定的布局绑定,注意返回内容为viewholder,即自己创建的viewholder
        @Override
        public MyviewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=   LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_itrm,null,false);
            MyviewHodler viewhodler=new MyviewHodler(view);
            return viewhodler;
        }
        //与具体的数据相互绑定
        @Override
        public void onBindViewHolder( final MyviewHodler holder, final int position) {
            Log.d("infomation","have invoke!");
            final String picurl=  list.get(position).getVideoPicUrl();
            NetUtil netUtil=new NetUtil(testActivity.this);
            //可能存在多个线程同时读取导致变量被覆盖的问题
            synchronized (netUtil){
                netUtil.getImageResourceFromNet(picurl, "GET", false, false);

                netUtil.setOnValueRecived(new NetUtil.OnValueRecived() {
                    @Override
                    public void recieve(String stringInformationFromNet, InputStream inputStream) {
                        //BitmapFactory.decodeStream(inputStream)

                    }

                    @Override
                    public void recieveBitmap(Bitmap bitmap) {
                        holder.imageview.setImageBitmap(bitmap);
                        holder.textview.setText(list.get(position).getVideoName());
                    }
                });

            }
        }
    }

    /**
     * 对每一个项目进行分割线的绘制
     */

//    class mydivider extends RecyclerView.ItemDecoration{
//
//        private int[] ATTRS=new int[]{android.R.attr.listDivider};  //系统默认的下划线样式
//
//        private Drawable drawable;
//
//        public mydivider(Context context){
//            TypedArray typearray= context.obtainStyledAttributes(ATTRS);
//            drawable=  typearray.getDrawable(0);
//            typearray.recycle();
//        }
//
//        /**
//         *  画下划线【从子控件的底部开始到子控件的底部+线条自身的高度】
//         * @param c
//         * @param parent
//         * @param state
//         */
//        @Override
//        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            super.onDraw(c, parent, state);
//            final int left = parent.getPaddingLeft();
//            final int right = parent.getWidth() - parent.getPaddingRight();
//
//            final int childCount = parent.getChildCount();
//
//            for (int i = 0; i < childCount; i++) {
//                final View child = parent.getChildAt(i);
//                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child  //获取控件本身具有的一些属性
//                        .getLayoutParams();
//                final int top = child.getBottom() + params.bottomMargin; //控件相对于父布局的高度
//                final int bottom = top + drawable.getIntrinsicHeight();  //固有的高度
//
//
//                drawable.setBounds(left, top, right, bottom);  //绘制控件的大小
//                drawable.draw(c);
//            }
//        }
//
//        @Override
//        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            super.onDrawOver(c, parent, state);
//        }
//        //设置两个item之间的距离
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            outRect.set(0,0,0,drawable.getIntrinsicHeight());
//            super.getItemOffsets(outRect, view, parent, state);
//        }
//    }

}
