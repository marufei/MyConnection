package com.jingyi.myconnection.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jingyi.myconnection.R;
import com.jingyi.myconnection.utils.MyUtils;


/**
 * TODO web页面
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private String urlShow;
    private Toolbar toolbar;
    private String TAG = "WebActivity";
    private TextView web_title;
    private ImageButton web_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setDarkStatusIcon(true);
        initView();
        webView = (WebView) findViewById(R.id.webview);
        Intent intent = getIntent();
        urlShow = intent.getStringExtra("URL");
        String title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            web_title.setText(title);
        }
        MyUtils.Loge("aaa", "urlShow::" + urlShow);
        if (urlShow.length() > 0 && urlShow.contains("https")) {
            urlShow = urlShow.replaceFirst("https", "http");
        }
        if (!TextUtils.isEmpty(urlShow)) {
            initData();
        }
    }

    private void initView() {
        web_title=findViewById(R.id.web_title);
        web_back=findViewById(R.id.web_back);
        web_back.setOnClickListener(this);
    }

    public static void start(Context context, String url, String title) {
        Intent intent = new Intent();
        intent.putExtra("URL", url);
        intent.putExtra("title", title);
        intent.setClass(context, WebActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra("URL", url);
        intent.setClass(context, WebActivity.class);
        context.startActivity(intent);
    }


    private void initData() {

        MyUtils.Loge("aaa", "改后----urlShow::" + urlShow);
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);//WebView中包含一个ZoomButtonsController，当使用web.getSettings().setBuiltInZoomControls(true);启用后，用户一旦触摸屏幕，就会出现缩放控制图标。
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setPluginsEnabled(true);//可以使用插件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置加载进来的页面自适应手机屏幕
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);//启用Dom内存（不加就显示不出来）
        webView.setDownloadListener(new MyWebViewDownLoadListener()); //捕捉下载
        webView.setWebChromeClient(new WebChromeClient());
//        加载需要显示的网页
        webView.loadUrl(urlShow);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    web_title.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        webView.setVisibility(View.GONE);//ZoomButtonsController有一个register和unregister的过程
        webView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.web_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    /**
     * webview的下载
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    /**
     * 使点击回退按钮不会直接退出整个应用程序而是返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序
    }


}
