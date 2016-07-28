package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sunonline.application.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanjigui on 2016/7/20.
 */
public class SwipeLayoutActivity2 extends Activity implements SwipeRefreshLayout.OnRefreshListener{
   private SwipeRefreshLayout  swipeRefreshLayout;
    private ListView listView;
    private List<String> list=new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipelayout_test);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe);
        listView= (ListView) findViewById(R.id.list_view);
        swipeRefreshLayout.setOnRefreshListener(this);
        for (int i=0;i<10;i++){
            list.add(String.valueOf(i));
        }
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onRefresh() {
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               swipeRefreshLayout.setRefreshing(false);
               list.add("这是我新加的数据");
                arrayAdapter.notifyDataSetChanged();
           }
       },300);

    }
}
