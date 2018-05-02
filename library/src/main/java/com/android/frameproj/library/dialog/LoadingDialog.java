package com.android.frameproj.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.frameproj.library.R;

/**
 * dialog
 */
public class LoadingDialog extends Dialog {

    private Context mContext;

    private static TextView mTxtContent;
    private String mStrContent;

    public LoadingDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    public LoadingDialog(Context context, String content) {
        super(context, R.style.MyDialog);
        this.mStrContent = content;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.MyDialog);
        this.mContext = context;
    }

    public static LoadingDialog show(Context context, String content) {
        LoadingDialog loadingDialog = new LoadingDialog(context, content);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        return loadingDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        mTxtContent = (TextView) findViewById(R.id.dialog_loading_text);
        if (!TextUtils.isEmpty(mStrContent)) {
            mTxtContent.setText(mStrContent);
        }
    }

}
