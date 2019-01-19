# PayHelper

### 简介
集成支付宝支付、微信支付，对支付更好地二次封装，有利于模块化等工作

### 依赖使用
```
dependencies {
  implementation 'com.firejun:payhelper:1.0.1'
}
```
### 使用案例

#### 支付宝支付
```
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
```

#### 微信支付
```
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
```

### 版本记录

#### v1.0.1
1. 支付宝SDK升级至v15.5.5

#### v1.0.0
1. 增加支付宝支付
2. 增加微信支付


### 联系方式
QQ:435559203