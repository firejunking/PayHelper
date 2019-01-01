package com.firejunking.payhelperdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firejunking.payhelp.ali.AliPay;
import com.firejunking.payhelp.wx.WxPay;
import com.firejunking.payhelp.wx.WxPayReq;
import com.firejunking.payhelperdemo.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseResp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mAliPayBtn;
    private Button mWxPayBtn;
    private static final String WX_APP_ID = "微信的AppId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAliPayBtn = (Button) findViewById(R.id.main_ali_pay_btn);
        mWxPayBtn = (Button) findViewById(R.id.main_wx_pay_btn);
        mAliPayBtn.setOnClickListener(this);
        mWxPayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_ali_pay_btn:
                toAliPay("订单号");
                break;
            case R.id.main_wx_pay_btn:
                updateWeChatResponseListener();
                toWxPay();
                break;
            default:
                break;
        }
    }

    private void toAliPay(@NonNull String orderInfo) {
        AliPay.getInstance().aliPay(this, orderInfo, new AliPay.AliPayResultCallBack() {
            @Override
            public void onPaySuccess(@NonNull String resultInfo) {
                // 根据需要对支付成功后的业务处理
            }

            @Override
            public void onPayFailure(@NonNull Exception e) {
                // 根据业务需要处理失败异常
            }
        });
    }

    private void toWxPay() {
        if (WxPay.getInstance().isSupportWxPay(this, WX_APP_ID)) {
            WxPayReq req = new WxPayReq();
            req.appId = "appId";
            req.partnerId = "partnerId";
            req.prepayId = "prepayId";
            req.nonceStr = "nonceStr";
            req.timeStamp = "timeStamp";
            req.packageName = "packageName";
            req.sign = "sign";
            req.extData = "app data";
            WxPay.getInstance().wxPay(this, WX_APP_ID, req);
        }
    }

    private void updateWeChatResponseListener() {
        WXPayEntryActivity.registerResponseListener(new WXPayEntryActivity.WeChatPayResponseListener() {
            @Override
            public void onResponse(BaseResp resp) {
                // 根据需要对支付成功后的业务处理
            }
        });
    }
}
