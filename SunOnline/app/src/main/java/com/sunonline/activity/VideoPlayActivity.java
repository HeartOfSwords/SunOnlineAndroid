package com.sunonline.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sunonline.application.R;
import com.sunonline.bean.MoocRecom;
import com.sunonline.bean.Video;
import com.sunonline.util.WindowsUtill;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

/**
 * Created by duanjigui on 2016/7/10.
 */
public class VideoPlayActivity extends Activity implements SurfaceHolder.Callback,View.OnClickListener{
    private int play_click_num=0; //暂停播放点击按钮的次数
    private int fullscren_click_num=0;//全屏按钮点击次数
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar; //进度条
    private ImageView pause_image;//播放按钮
    private SeekBar seekBar;//进度条
    private LinearLayout menu_control;//视频控制器
    private TextView current_value;//当前进度
    private TextView total_value;//总进度
    private ImageView fullScreen; //全屏图片按钮
    private boolean needResume; //用来判断是否暂停
    private String url;//播放视频的网址
    private TextView video_introduce;//视频简介
    private ImageView go_back;//返回按钮
    private String video_name;//视频名称
    private String video_intro;//视频简介
    private LinearLayout play_top;//视频顶部的自定义菜单
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");//格式化时间
    private  Timer timer=new Timer();
    private boolean isNotCancel=true;//用于标示是否取消任务
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查询是否导入libarary
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.videolayout);
        MoocRecom moocRecom= (MoocRecom) getIntent().getSerializableExtra("moocRecom_video_info");
        Video video= (Video) getIntent().getSerializableExtra("videobean_video_info");
        if (null!=moocRecom){
            url=moocRecom.getCl_video_url();
            video_name=moocRecom.getCl_name();
            video_intro=moocRecom.getCl_video_intro();
        }else if (null!=video){
            url=video.getVideoUrl();
            video_name=video.getVideoName();
            video_intro=video.getVideoIntro();
        }else {
            url=null;
            video_name="";
            video_intro="";
        }
        surfaceView= (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder=  surfaceView.getHolder();
        progressBar= (ProgressBar) findViewById(R.id.progress);
        pause_image= (ImageView) findViewById(R.id.pause_image);
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBarListener());
        pause_image.setOnClickListener(this);//设置监听
        menu_control= (LinearLayout) findViewById(R.id.control_menu);
        current_value= (TextView) findViewById(R.id.current_value);
        total_value= (TextView) findViewById(R.id.total_value);
        fullScreen= (ImageView) findViewById(R.id.fullscreen);//全屏按钮
        video_introduce= (TextView) findViewById(R.id.video_introduce);//视频简介
        go_back= (ImageView) findViewById(R.id.go_back);//返回按钮
        play_top= (LinearLayout) findViewById(R.id.video_top);
        go_back.setOnClickListener(this);
        video_introduce.setText("视频名称：\n"+"      "+video_name+"\n"+"视频简介：\n"+"      "+video_intro);
        fullScreen.setOnClickListener(this);
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer=new MediaPlayer(this);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.prepareAsync();
        mediaPlayer.setBufferSize(512 * 1024);
        mediaPlayer.setUseCache(true);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                long time_total=  mediaPlayer.getDuration();
                seekBar.setMax((int) time_total);//设置最大进度
                seekBar.setProgress((int) mediaPlayer.getCurrentPosition());//设置当前的进度
                current_value.setText(simpleDateFormat.format(new Date(mediaPlayer.getCurrentPosition()))); //显示当前的进度
                total_value.setText(simpleDateFormat.format(new Date(time_total))); //设置总进度
                progressBar.setVisibility(View.INVISIBLE);
                new Thread(){
                    @Override
                    public void run() {
                        //获取当前的进度
                        TimerTask timerTask1=new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {  //主线程中运行
                                    @Override
                                    public void run() {
                                        if (isNotCancel){
                                            seekBar.setProgress((int) mediaPlayer.getCurrentPosition());//设置当前的进度
                                            current_value.setText(simpleDateFormat.format(new Date(mediaPlayer.getCurrentPosition()))); //显示当前的进度
                                        }
                                    }
                                });
                            }
                        };
                        //设置5秒后控制按钮自动隐藏
                        TimerTask timerTask2=new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {  //主线程中运行
                                    @Override
                                    public void run() {
                                        menu_control.setVisibility(View.INVISIBLE);
                                        fullScreen.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask1,0,300);
                        timer.schedule(timerTask2,0,5000);
                    }
                }.start();
            }
        });
        //缓存
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:  //开始缓存
                        if (mp.isPlaying()){
                            mp.stop();
                            needResume=true;
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        if (needResume){
                            mp.start();
                            progressBar.setVisibility(View.INVISIBLE);
                            needResume=false;
                        }
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isNotCancel=false;
        timer.cancel();
        mediaPlayer.release();

    }
    //点击事件的方法
    @Override
    public void onClick(View v) {
        int[] values= WindowsUtill.getScreenWidthAndHeight(this);
        switch (v.getId()){
            case R.id.pause_image:    //设置播放器的展厅和播放之间的切换
                if (play_click_num%2==0){
                    pause_image.setImageResource(R.drawable.play);  //将暂停按钮换成播放按钮
                    mediaPlayer.pause();
                    needResume=true;
                }else {
                    pause_image.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    needResume=false;
                }
                play_click_num++;
                break;
            case R.id.fullscreen:

                if (fullscren_click_num%2==0){
                    fullScreen.setImageResource(R.drawable.revoke);  //将暂停按钮换成播放按钮
                    play_top.setVisibility(View.GONE);
                    mediaPlayer.stop();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
                    //设置屏幕大小为全屏
                     values=WindowsUtill.getScreenWidthAndHeight(this);

                   ViewGroup.LayoutParams layoutParams= surfaceView.getLayoutParams();
                    layoutParams.width=values[0];
                    layoutParams.height=values[1];
                    //隐藏状态栏
                    WindowsUtill.fullScreen(this);

                    mediaPlayer.start();
                }else {
                    fullScreen.setImageResource(R.drawable.screenfull);
                    play_top.setVisibility(View.VISIBLE);
                    //设置屏幕的为原来样式的大小
                    mediaPlayer.stop();//暂停
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
                    values=WindowsUtill.getScreenWidthAndHeight(this);
                    ViewGroup.LayoutParams layoutParams= surfaceView.getLayoutParams();
                    layoutParams.width=values[0];
                    layoutParams.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            200,getResources().getDisplayMetrics());  //将px转为dp
                    //隐藏状态栏
                    WindowsUtill.cancleScreen(this);

                    mediaPlayer.start();//开始播放
                }
                fullscren_click_num++;
                break;
            case R.id.go_back:
                 VideoPlayActivity.this.finish();
                break;
        }
    }
    //设置进度条的播放进度
    class  MySeekBarListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int current_pos= seekBar.getProgress();  //获取当前进度
            mediaPlayer.seekTo(current_pos); //拖到进度
        }
    }

    /**
     * 当点击屏幕时控制按钮显示，如3秒种每操作则将其隐藏
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        menu_control.setVisibility(View.VISIBLE);
        fullScreen.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    protected void onDestroy() {
        isNotCancel=false;
        timer.cancel();
        super.onDestroy();
    }
}
