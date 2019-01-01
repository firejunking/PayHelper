package com.firejunking.payhelp.wx;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firejunking.payhelp.IPay;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxPay implements IPay.IWxPay {

    private static WxPay mInstance;

    public static WxPay getInstance() {
        if (mInstance == null) {
            synchronized (WxPay.class) {
                if (mInstance == null) {
                    mInstance = new WxPay();
                }
            }
        }
        return mInstance;
    }

    private IWXAPI createWxApi(@NonNull Context context, @NonNull String key) {
        return WXAPIFactory.createWXAPI(context, key);
    }

    @Override
    public boolean isSupportWxPay(@NonNull Context context, @NonNull String key) {
        IWXAPI api = createWxApi(context, key);
        return api.isWXAppInstalled() && api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    @Override
    public void wxPay(@NonNull Context context, @NonNull String key, @NonNull WxPayReq response) {
        IWXAPI api = createWxApi(context, key);
        api.registerApp(key);
        PayReq req = new PayReq();
        req.appId = response.appId;
        req.partnerId = response.partnerId;
        req.prepayId = response.prepayId;
        req.nonceStr = response.nonceStr;
        req.timeStamp = response.timeStamp;
        req.packageValue = response.packageName;
        req.sign = response.sign;
        req.extData = response.extData;
        api.sendReq(req);
    }
}
