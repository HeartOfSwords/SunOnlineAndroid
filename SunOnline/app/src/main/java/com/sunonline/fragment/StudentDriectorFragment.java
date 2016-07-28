package com.sunonline.fragment;


import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sunonline.application.R;
import com.sunonline.global.Information;

/**
 * 新生指南区域
 * Created by duanjigui on 2016/6/28.
 */
public class StudentDriectorFragment extends Fragment implements View.OnTouchListener {
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           View view= inflater.inflate(R.layout.new_student_director, container,false);
           webView= (WebView) view.findViewById(R.id.web_view);
           webView.addJavascriptInterface(new JsObject(), "injectedObject");
           webView.getSettings().setSupportZoom(true);
           webView.getSettings().setDomStorageEnabled(true);
           webView.requestFocus();
           webView.getSettings().setUseWideViewPort(true);
           webView.getSettings().setLoadWithOverviewMode(true);
           webView.getSettings().setSupportZoom(true);
           webView.getSettings().setBuiltInZoomControls(true);
           webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

           webView.setWebViewClient(new WebViewClient() {
               @Override
               public void onPageStarted(WebView view, String url, Bitmap favicon) {
                   super.onPageStarted(view, url, favicon);
               }

               @Override
               public boolean shouldOverrideUrlLoading(WebView view, String url) {
                   view.loadUrl(url);
                   return false;
               }

               @Override
               public void onPageFinished(WebView view, String url) {
                   view.loadUrl("javascript:window.onload=function(){  document.getElementsByClassName('video-player')[0].style.display='none'}");
                   //  super.onPageFinished(view, url);
               }

               @Override
               public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                   super.onReceivedError(view, request, error);
               }
           });
           webView.loadUrl("http://139.129.221.162/studentGuide.html");
           return view;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("StudentDriectorFragment","StudentDriectorFragment onTouch");
        return false;
    }
    class JsObject {
        @JavascriptInterface
        public String toString() { return "injectedObject"; }
    }

    @Override
    public void onResume() {
        webView.reload();
        super.onResume();
    }
}
