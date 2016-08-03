package com.sunonline.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunonline.adpter.MoocRoomDetailAdpter;
import com.sunonline.application.R;
import com.sunonline.util.JsonParserUtil;
import com.sunonline.util.Mydivider;
import com.sunonline.util.PaserMoocRoomDetailJson;

/**
 * 水平滚动条下对应的公益课堂部分的frgment
 * Created by duanjigui on 2016/7/18.
 */
public class PublicTeachFragment extends Fragment {

    private RecyclerView soft_devlop;//软件开发
    private RecyclerView mediasAfter;//影视后期
    private RecyclerView radioInterview;//播音主持

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.public_teach_second_page, null, false);
        Log.d("PublicTeachFragment","have exec!");
        soft_devlop= (RecyclerView) view.findViewById(R.id.soft_devlop);
        mediasAfter= (RecyclerView) view.findViewById(R.id.mediasAfter);
        radioInterview= (RecyclerView) view.findViewById(R.id.radioInterview);
        initAdpter();
        return view;
    }

    /**
     * 初始化adpter,即为recycleview添加适配器
     */
    private void initAdpter() {
        String mooc_index_url="http://139.129.221.162/webapi/mooc/index";
        JsonParserUtil jsonParserUtil=new JsonParserUtil(getActivity());
        jsonParserUtil.setPaserJson(new PaserMoocRoomDetailJson(getActivity(),PaserMoocRoomDetailJson.SOFTWARE_DEVELOPING));
        jsonParserUtil.paser(mooc_index_url, soft_devlop, new MoocRoomDetailAdpter(getActivity()));

        JsonParserUtil jsonParserUti2=new JsonParserUtil(getActivity());
        jsonParserUti2.setPaserJson(new PaserMoocRoomDetailJson(getActivity(),PaserMoocRoomDetailJson.MEDIAS_AFTER));
        jsonParserUti2.paser(mooc_index_url, mediasAfter, new MoocRoomDetailAdpter(getActivity()));

        JsonParserUtil jsonParserUti3=new JsonParserUtil(getActivity());
        jsonParserUti3.setPaserJson(new PaserMoocRoomDetailJson(getActivity(),PaserMoocRoomDetailJson.RADIO_INTERVIEW));
        jsonParserUti3.paser(mooc_index_url, radioInterview, new MoocRoomDetailAdpter(getActivity()));

        soft_devlop.setLayoutManager(new LinearLayoutManager(getActivity()));
        soft_devlop.addItemDecoration(new Mydivider(getActivity()));
        mediasAfter.setLayoutManager(new LinearLayoutManager(getActivity()));
        mediasAfter.addItemDecoration(new Mydivider(getActivity()));
        radioInterview.setLayoutManager(new LinearLayoutManager(getActivity()));
        radioInterview.addItemDecoration(new Mydivider(getActivity()));
    }

}
