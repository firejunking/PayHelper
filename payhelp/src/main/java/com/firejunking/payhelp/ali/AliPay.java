package com.firejunking.payhelp.ali;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.alipay.sdk.app.PayTask;
import com.firejunking.payhelp.IPay;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class AliPay implements IPay.IAliPay {

    private static AliPay mInstance;

    public static AliPay getInstance() {
        if (mInstance == null) {
            synchronized (AliPay.class) {
                if (mInstance == null) {
                    mInstance = new AliPay();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void aliPay(@NonNull final Activity activity, final @NonNull String orderInfo, final @NonNull AliPay.AliPayResultCallBack callBack) {
        Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                WeakReference<Activity> weakReference = new WeakReference<>(activity);
                if (weakReference.get() != null) {
                    // 调起支付页面, 支付宝的SDK
                    PayTask aliPay = new PayTask(weakReference.get());
                    // SDK返回结果
                    return aliPay.pay(orderInfo, true);
                }
                return null;
            }
        }).onSuccess(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) throws Exception {
                callBack.onPaySuccess(task.getResult());
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                if (task.isFaulted()) {
                    callBack.onPayFailure(task.getError());
                }
                return null;
            }
        });
    }


    public interface AliPayResultCallBack {

        void onPaySuccess(String resultInfo);

        void onPayFailure(Exception e);
    }

}
