package com.lh.rapid.ui.orderpay;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import com.alipay.sdk.app.AuthTask;
import com.android.frameproj.library.util.ToastUtil;
import com.lh.rapid.R;
import com.lh.rapid.bean.PayResult;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.widget.MyActionBar;
import com.lh.rapid.util.BusUtil;
import com.lh.rapid.util.CommonEvent;
import com.squareup.otto.Subscribe;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import butterknife.BindView;

/**
 * Created by lh on 2018/5/3.
 */
public class OrderPayActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    MyActionBar mActionbar;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private int mOrderId;
    private double mPrice;

    @Override
    public int initContentView() {
        return R.layout.activity_order_pay;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        BusUtil.getBus().register(this);
        mOrderId = getIntent().getIntExtra("orderId", -1);
        mPrice = getIntent().getDoubleExtra("price", -1);
        mTvPrice.setText("￥" + mPrice);

        mActionbar.setBackClickListener(new MyActionBar.IActionBarClickListener() {
            @Override
            public void onActionBarClicked() {
                finish();
            }
        });
        mActionbar.setTitle("订单支付");
    }

    // ==========================支付宝支付开始=========================
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private void aliPay(final String authInfo) {
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                AuthTask authTask = new AuthTask(OrderPayActivity.this);
                Map<String, String> result = authTask.authV2(authInfo, true);
                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 调用确认订单
                        // TODO
                    } else {
                        ToastUtil.showToast("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    // ==========================支付宝支付结束=========================

    // ==========================微信支付开始=========================
    private void wechatPay(final String content) throws JSONException {

        JSONObject json = new JSONObject(content);
        if (null != json && !json.has("retcode")) {
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            IWXAPI api = WXAPIFactory.createWXAPI(OrderPayActivity.this, req.appId);
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");
            req.extData = "app data";
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        } else {
            ToastUtil.showToast("返回错误" + json.getString("retmsg"));
        }

    }

    @Subscribe
    public void wechatResq(CommonEvent.WeChatEvent weChatEvent) {
        // 调用确认订单
        // TODO
    }
    // ==========================微信支付结束=========================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusUtil.getBus().unregister(this);
    }
}
