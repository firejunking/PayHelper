package com.firejunking.payhelp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.firejunking.payhelp.ali.AliPay;
import com.firejunking.payhelp.wx.WxPayReq;

public interface IPay {

    interface BasePay {

    }

    interface IWxPay extends BasePay {
        boolean isSupportWxPay(@NonNull Context context, @NonNull String key);

        void wxPay(@NonNull Context context, @NonNull String key, @NonNull WxPayReq wxPayReq);
    }

    interface IAliPay extends BasePay {

        void aliPay(@NonNull Activity activity, String orderInfo, @NonNull AliPay.AliPayResultCallBack callBack);

    }

}
