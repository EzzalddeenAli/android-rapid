package com.android.frameproj.library.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.android.frameproj.library.R;


/**
 * Created by ywl on 2017/2/28.
 */

public class PayDialog extends BaseDialog{

    private PayPwdEditText payPwdEditText;

    private PayPwdEditText.OnTextFinishListener mOnTextFinishListener;

    public void setOnTextFinishListener(PayPwdEditText.OnTextFinishListener onTextFinishListener) {
        mOnTextFinishListener = onTextFinishListener;
    }

    public PayDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_dialog_lyaout);
        payPwdEditText = (PayPwdEditText) findViewById(R.id.ppet);

        payPwdEditText.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.statefulLayoutImageColor, R.color.statefulLayoutImageColor, 20);
        payPwdEditText.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {//密码输入完后的回调
//                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                if(mOnTextFinishListener!=null){
                    mOnTextFinishListener.onFinish(str);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                payPwdEditText.setFocus();
            }
        }, 100);

    }
}
