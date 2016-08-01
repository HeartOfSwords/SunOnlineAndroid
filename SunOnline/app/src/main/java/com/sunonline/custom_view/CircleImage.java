package com.sunonline.custom_view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.meg7.widget.CustomShapeImageView;
import com.sunonline.application.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 图片变为圆形的原理：以原来的图为基础，
 * 然后才去用大小一样的圆形和大小一样的正方形取交集
 * 然后出来的效果就是圆形
 * Created by duanjigui on 2016/7/12.
 */
public class CircleImage extends ImageView {
    /**
     * TYPE_CIRCLE / TYPE_ROUND
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;  //常量0代表圆形
    private static final int TYPE_ROUND = 1;   //常量1代表椭圆，圆角

    /**
     * 图片
     */
    private Bitmap mSrc;

    /**
     * 圆角的大小
     */
    private int mRadius;

    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getmSrc() {
        return mSrc;
    }

    public void setmSrc(Bitmap mSrc) {
        this.mSrc = mSrc;
        requestLayout();
        invalidate();
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public CircleImage(Context context) {
        this(context,null);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CircleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImage, defStyleAttr, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.CircleImage_src:
                    mSrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CircleImage_type:
                    type = a.getInt(attr, 0);// 默认为Circle
                    break;
                case R.styleable.CircleImage_borderRadius:
                    mRadius= a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
                            getResources().getDisplayMetrics()));// 默认为10DP
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 计算控件的高度和宽度
     * onMeasure方法默认行为是当模式为UNSPECIFIED时，设置尺寸为mMinWidth(通常为0)或者背景drawable的最小尺寸，
     * 当模式为EXACTLY或者AT_MOST时，尺寸设置为传入的MeasureSpec的大小。
     * widthMeasureSpec  父窗体提供的水平方向的值
     * heightMeasureSpec  父窗体提供的垂直方向的值
     * 父窗体即自己的自定义控件
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 设置宽度
         * MeasureSpec封装了父布局传给子布局的布局要求
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);//获取
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurateEXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小
        {
            mWidth = specSize;
        } else
        {
            // 由图片决定的宽【自身的宽度】
            int desireByImg = getPaddingLeft() + getPaddingRight()
                    + mSrc.getWidth();
            if (specMode == MeasureSpec.AT_MOST)//  子元素至多达到指定大小的值
            {
                mWidth = Math.min(desireByImg, specSize);
            } else

                mWidth = desireByImg;//wrap_content
        }

        /***
         * 设置高度
         */

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else
        {
            int desire = getPaddingTop() + getPaddingBottom()
                    + mSrc.getHeight();

            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, specSize);
            } else
                mHeight = desire;
        }

        setMeasuredDimension(mWidth, mHeight);

    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas)
    {

        switch (type)
        {
            // 如果是TYPE_CIRCLE绘制圆形
            case TYPE_CIRCLE:

                int min = Math.min(mWidth, mHeight);
                /**
                 * 长度如果不一致，按小的值进行压缩
                 */
                if (null!=mSrc){
                    mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);//首先先压缩图片的宽高
                }else {
                    mSrc = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.c), min, min, false);//首先先压缩图片的宽高
                }

                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);//重新绘制图片
                break;
            case TYPE_ROUND:
                canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
                break;

        }

    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    private Bitmap createRoundConerImage(Bitmap source)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);//仿制图片出现边缘锯齿
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888); //代表创建出的bitmap对象是32位ARGB位图
        Canvas canvas = new Canvas(target);  //创建画布
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight()); //构造矩形
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 给CircleImage设置图片路径
     * @param activity  当前的上下文
     * @param userAvatar_address  设置图片 image_adrress
     * @param circleImage   设置组件id
     * @param default_resoursedId  设置资源id
     */
    public static void setImageResourse(final Activity activity,final String userAvatar_address, final CustomShapeImageView circleImage, final int default_resoursedId) {
        if ("".equals(userAvatar_address)){
            circleImage.setImageResource(default_resoursedId);
        }else {
            new Thread(){
                @Override
                public void run() {
                    OkHttpClient httpClient=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url(userAvatar_address)
                            .get()
                            .build();
                    httpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    circleImage.setImageResource(default_resoursedId);
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            InputStream inputStream= response.body().byteStream();
                            final Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (null==bitmap){
                                        circleImage.setImageResource(default_resoursedId);
                                    }else
                                    {
                                        circleImage.setImageBitmap(bitmap);
                                    }
                                }
                            });

                        }
                    });
                }
            }.start();
        }
    }

}
