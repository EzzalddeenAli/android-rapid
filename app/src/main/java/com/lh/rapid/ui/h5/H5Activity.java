package com.lh.rapid.ui.h5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lh.rapid.R;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;

import butterknife.BindView;

/**
 * Created by lh on 2017/12/17.
 */

public class H5Activity extends BaseActivity {

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.progress_bar_loading_web)
    ProgressBar mProgressBar;
    @BindView(R.id.webView)
    WebView mWebView;

    private String mUrl;

    @Override
    public int initContentView() {
        return R.layout.activity_h5;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        mUrl = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        mActionbar.setTitle(title);
        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });

        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // 支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        // ==============
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        if (mUrl.startsWith("http")) {
            mWebView.loadUrl(mUrl);
        } else {
            mWebView.loadDataWithBaseURL(null, mUrl, "text/html", "utf-8", null);
        }

        // 设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去mWebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new XHSWebChromeClient());

    }


    private ValueCallback mUploadMessage;
    private int FILECHOOSER_RESULTCODE = 10;

    // ==============================================================
    // https://isming.me/2015/12/21/android-webview-upload-file/
    // Android WebView 上传文件支持全解析
    // ==============================================================
    public class XHSWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //更新进度
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(newProgress);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        //Android 5.0+
        @Override
        @SuppressLint("NewApi")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                    && fileChooserParams.getAcceptTypes().length > 0) {
                i.setType(fileChooserParams.getAcceptTypes()[0]);
            } else {
                i.setType("*/*");
            }
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            return true;
        }
    }

    //onActivityResult回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            String path = FileUtils.getPath(this, result);
            if (TextUtils.isEmpty(path)) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            Uri uri = Uri.fromFile(new File(path));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessage.onReceiveValue(new Uri[]{uri});
            } else {
                mUploadMessage.onReceiveValue(uri);
            }
            mUploadMessage = null;
        }
    }

}
