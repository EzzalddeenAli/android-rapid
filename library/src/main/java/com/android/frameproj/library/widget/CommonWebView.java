package com.android.frameproj.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.frameproj.library.util.ScreenUtil;

public class CommonWebView extends FrameLayout {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mUrl;

    private IWebViewClienCallBack mWebViewClient;
    private Context mContext;

    public WebView getWebView() {
        return mWebView;
    }

    public void setIOverideUrl(IWebViewClienCallBack l) {
        mWebViewClient = l;
    }

    public CommonWebView(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public CommonWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    public CommonWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        mWebView = new WebView(context);
        mProgressBar = new ProgressBar(context);
        addView(mWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mProgressBar, new LayoutParams(ScreenUtil.dipToPx(mContext, 30), ScreenUtil.dipToPx(mContext, 30), Gravity.CENTER));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {

        final WebSettings webSettings = mWebView.getSettings();
        if (webSettings == null)
            return;

        webSettings.setJavaScriptEnabled(true);     // enable navigator.geolocation
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setDisplayZoomControls(false);//设定缩放控件隐藏
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUserAgentString(mWebView.getSettings().getUserAgentString());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
//        mWebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
//        mWebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
//        mWebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
//        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
//        mWebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebView.getSettings().setAppCacheEnabled(false);//是否使用缓存
        mWebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        webSettings.setJavaScriptEnabled(true);//启动JavaScript功能
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows();  //多窗口
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //关闭webview中缓存
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setSupportZoom(false); //

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
//                webSettings.setBlockNetworkImage(false);
                if (mWebViewClient != null) {
                    mWebViewClient.onPageFinished();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mWebViewClient != null) {
                    return mWebViewClient.reload(view, url);
                }
                return false;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                // MainActivity.this.setProgress(newProgress * 100);
                mProgressBar.setProgress(newProgress);
            }
        });
        mWebView.setScrollBarStyle(SCROLLBARS_OUTSIDE_INSET);
        mWebView.loadUrl(mUrl);
    }

    public void setCommonWebView(String url) {
        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookie();
        cm.removeAllCookie();
        mUrl = url;
        setupWebView();
    }

    public interface IWebViewClienCallBack {
        void onPageFinished();

        boolean reload(WebView view, String url);
    }

    public void destroy() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.destroy();
        }
    }

    public boolean goBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public void refresh() {
        if (mWebView != null) {
            mWebView.reload();
        }
    }

    private static final int API = android.os.Build.VERSION.SDK_INT;

}