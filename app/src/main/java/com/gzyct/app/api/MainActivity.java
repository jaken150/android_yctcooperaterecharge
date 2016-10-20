package com.gzyct.app.api;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gzyct.app.api.httpentity.BaseParam;
import com.gzyct.app.api.httpentity.CardAcctBalanceReq;
import com.gzyct.app.api.httpentity.CardAcctBalanceResp;
import com.gzyct.app.api.httpentity.OrderApplyResp;
import com.gzyct.app.api.httpentity.OrderInfo;
import com.gzyct.app.api.httpentity.OrderQueryReq;
import com.gzyct.app.api.httpentity.OrderQueryResp;
import com.gzyct.app.api.httpentity.PaySdkApplyResp;
import com.gzyct.app.api.util.Helper;
import com.gzyct.app.api.util.YctApiMD5;
import com.orhanobut.logger.Logger;
import com.shelwee.update.UpdateHelper;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import net.grandcentrix.tray.TrayAppPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gzyct.app.api.httpentity.OrderApplyReq;
import com.gzyct.app.api.httpentity.PaySdkApplyReq;
import com.gzyct.app.api.util.AppConstant;
import com.gzyct.app.api.util.DL;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends Activity {

    private String TAG = "MainActivity";
    private AutoCompleteTextView mAutoTV;
    private RadioGroup mRG;
    private SweetAlertDialog pDialog;
    //自动填充输入框
    private TrayAppPreferences appPreferences;
    private List<String> mHistorysArray = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    //从第三方应用获取的参数

    private Handler mHandler = new Handler(Looper.myLooper());

    private String mOrder_Id = "";
    private int mAcctBalance = 0;
    private IWXAPI api;
    private int mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainApp.getInstance().Channel_Code = getIntent().getStringExtra("Channel_Code");
        MainApp.getInstance().User_Id = getIntent().getStringExtra("User_Id");
        MainApp.getInstance().Channel_Key = getIntent().getStringExtra("Channel_Key");

        if (MainApp.getInstance().Channel_Code == null || MainApp.getInstance().Channel_Code.length() == 0) {
            MainApp.getInstance().Channel_Code = "70000008";
        }
        if (MainApp.getInstance().User_Id == null || MainApp.getInstance().User_Id.length() == 0) {
//            MainApp.getInstance().User_Id = "70000008_" + Helper.getAndroid_id(this);//可以自行定义User_id，于用订单状态查询
            MainApp.getInstance().User_Id = "android_test";//可以自行定义User_id，于用订单状态查询
        }
        if (MainApp.getInstance().Channel_Key == null || MainApp.getInstance().Channel_Key.length() == 0) {
            MainApp.getInstance().Channel_Key = "7dca10183ac93edc275de275ada3b736";
        }
        api = WXAPIFactory.createWXAPI(this, AppConstant.WX_APP_ID);
        appPreferences = new TrayAppPreferences(MainActivity.this);
        initComponent();
        initListener();

        UpdateHelper updateHelper = new UpdateHelper.Builder(this)
                .checkUrl(AppConstant.getYCTIUrl() + "/update.html")
                .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
                .build();
        updateHelper.check();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DL.log(TAG, "onNewIntent Action = " + intent.getAction());
        if (intent.getAction() == null) {
            int errCode = intent.getIntExtra("errCode", -3);
            DL.log(TAG, "onNewIntent errCode = " + errCode);
            if (errCode == 0) {
                DL.log(TAG, "支付成功" + errCode);
                pDialog.setTitleText("支付成功，请稍候...");
                pDialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        httpPostOrderQuery();
                    }
                }, 3000);//延迟3秒再查询结果


            } else if (errCode == -1) {
                DL.log(TAG, "支付失败" + errCode);
                MainApp.toast("支付失败");
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("支付失败")
                        .setConfirmText(" 确定 ")
                        .show();
            } else if (errCode == -2) {
                DL.log(TAG, "支付取消" + errCode);
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("支付取消")
                        .setConfirmText(" 确定 ")
                        .show();
            } else {
                DL.log(TAG, "未知错误" + errCode);
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("未知错误")
                        .setConfirmText(" 确定 ")
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    private void initComponent() {
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(false);

        mAutoTV = (AutoCompleteTextView) findViewById(R.id.auto_tv);

//        if (DL.DEBUGVERSION) mAutoTV.setText("5600010205");
        mRG = (RadioGroup) findViewById(R.id.radio_group);
        DL.log(TAG, "history = " + appPreferences.getString("history", ""));
        String[] strings = appPreferences.getString("history", "").split(",");
        mHistorysArray.clear();
        for (int i = 0; i < strings.length; i++) {
            mHistorysArray.add(strings[i]);
        }
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mHistorysArray);
        mAutoTV.setAdapter(mAdapter);
        mAutoTV.setThreshold(1);
    }

    private void initListener() {
        if(DL.DEBUGVERSION){
            mAutoTV.setText("0275638978");
        }
        findViewById(R.id.lly_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderQueryActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.lly_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BaseRecyclerViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoTV.setText("");
            }
        });
        findViewById(R.id.tv_clear_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();
                appPreferences.put("history", "");
                MainApp.toast("已清除  ");
            }
        });

        mAutoTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((TextView) findViewById(R.id.tv_acct_balance)).setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() == 10) {
                    httpPostCardAcctBalance(false);
                }

            }
        });

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化参数
                mOrder_Id = "";
                mAmount = 0;
                if (mAutoTV.getText().toString().length() < 8) {
                    MainApp.toast("请输入8-10位羊城通卡号");
                    return;
                } else {
                    if (appPreferences.getString("history", "").indexOf(mAutoTV.getText().toString()) < 0) {
                        appPreferences.put("history", appPreferences.getString("history", "") + mAutoTV.getText().toString() + ",");
                        mAdapter.add(mAutoTV.getText().toString());
                    }
                }
                if (mRG.getCheckedRadioButtonId() == -1) {
                    MainApp.toast("请选择充值金额");
                    return;
                }
                if (DL.DEBUGVERSION) {
                    mAmount = Integer.parseInt((String) findViewById(mRG.getCheckedRadioButtonId()).getTag()) / 50;
                } else {
                    mAmount = Integer.parseInt((String) findViewById(mRG.getCheckedRadioButtonId()).getTag()) * 100;
                }

                final CardAcctBalanceReq cardAcctBalanceReq = new CardAcctBalanceReq();
                MainApp.getInstance().buildBaseParams(cardAcctBalanceReq);
                cardAcctBalanceReq.setService("yct.base.card.acct.balance");
                cardAcctBalanceReq.setCard_num("510000" + mAutoTV.getText().toString());
                cardAcctBalanceReq.setSign(YctApiMD5.encryptObjectMD5(cardAcctBalanceReq, MainApp.getInstance().Channel_Key));

                pDialog.setTitleText("正在检验卡片状态");
                pDialog.show();
                //检查卡片合法性
                MainApp.getInstance().okHttpPost(AppConstant.getBaseUrl(), JSON.toJSONString(cardAcctBalanceReq), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                MainApp.toast("网络请求失败");
                                pDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String resp = response.body().string();
                        Logger.json(resp);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CardAcctBalanceResp cardAcctBalanceResp = JSON.parseObject(resp, CardAcctBalanceResp.class);
                                if (cardAcctBalanceResp.getResult_code() == null || !cardAcctBalanceResp.getResult_code().equals("0")) {
                                    if (cardAcctBalanceResp.getErr_msg() != null && cardAcctBalanceResp.getErr_msg().length() > 0)
                                        MainApp.toast(cardAcctBalanceResp.getErr_msg());
                                    else
                                        MainApp.toast("网络请求异常");
                                    pDialog.dismiss();
                                    return;
                                }
                                pDialog.setTitleText("当前账户余额：" + (float) cardAcctBalanceResp.getBalance() / 100 + "元，请稍候...");
                                mAcctBalance = cardAcctBalanceResp.getBalance();

                                final OrderApplyReq req = new OrderApplyReq();
                                MainApp.getInstance().buildBaseParams(req);
                                req.setService("yct.product.czj.order.apply");
                                req.setCard_num("510000" + mAutoTV.getText().toString());
                                req.setTotal_fee(mAmount);
                                req.setPay_channel("WXIN");
                                req.setPay_channel_type("SDK");
                                req.setPay_fee(mAmount);
                                req.setFee_type("CNY");
                                req.setSign(YctApiMD5.encryptObjectMD5(req, MainApp.getInstance().Channel_Key));

                                MainApp.getInstance().okHttpPost(AppConstant.getProductCzjUrl(), JSON.toJSONString(req), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                MainApp.toast("网络请求失败");
                                                pDialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String resp = response.body().string();
                                        Logger.json(resp);
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                OrderApplyResp orderResp = JSON.parseObject(resp, OrderApplyResp.class);
                                                if (orderResp.getResult_code() == null || !orderResp.getResult_code().equals("0")) {
                                                    if (orderResp.getErr_msg() != null && orderResp.getErr_msg().length() > 0)
                                                        MainApp.toast(orderResp.getErr_msg());
                                                    else
                                                        MainApp.toast("网络请求异常");
                                                    pDialog.dismiss();
                                                    return;
                                                }

                                                PaySdkApplyReq req = new PaySdkApplyReq();
                                                MainApp.getInstance().buildBaseParams(req);
                                                req.setService("yct.paycenter.pay.sdk.apply");
                                                req.setPay_channel("WXIN");
                                                req.setPay_fee(mAmount);
                                                req.setFee_type("CNY");
                                                req.setSource("1001");//1001充值金加值
                                                mOrder_Id = orderResp.getOrder_id();
                                                req.setOrder_id(orderResp.getOrder_id());
                                                req.setOrder_source(orderResp.getOrder_source());
                                                req.setBody("卡号" + mAutoTV.getText().toString() + "充值" + (float) mAmount / 100 + "元");
                                                req.setDetail("510000" + mAutoTV.getText().toString());
                                                req.setPay_channel_type("SDK");
                                                req.setYct_extend("card_num=510000" + mAutoTV.getText().toString() + "&channel_code=" + MainApp.getInstance().Channel_Code + "&product_source=1001" + "&user_id=" + MainApp.getInstance().User_Id);
                                                req.setSign(YctApiMD5.encryptObjectMD5(req, MainApp.getInstance().Channel_Key));

                                                MainApp.getInstance().okHttpPost(AppConstant.getPayCenterUrl(), JSON.toJSONString(req), new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        mHandler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                MainApp.toast("网络请求失败");
                                                                pDialog.dismiss();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        final String resp = response.body().string();
                                                        Logger.json(resp);
                                                        mHandler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                pDialog.dismiss();
                                                                PaySdkApplyResp paySdkApplyResp = JSON.parseObject(resp, PaySdkApplyResp.class);
                                                                if (paySdkApplyResp.getResult_code() == null || !paySdkApplyResp.getResult_code().equals("0")) {
                                                                    if (paySdkApplyResp.getErr_msg() != null && paySdkApplyResp.getErr_msg().length() > 0)
                                                                        MainApp.toast(paySdkApplyResp.getErr_msg());
                                                                    else
                                                                        MainApp.toast("网络请求异常");
                                                                    pDialog.dismiss();
                                                                    return;
                                                                }
                                                                try {
                                                                    JSONObject json = new JSONObject(paySdkApplyResp.getPay_info());
                                                                    if (paySdkApplyResp.getPay_info() == null || paySdkApplyResp.getPay_info().length() == 0) {
                                                                        MainApp.toast("订单生成失败");
                                                                        return;
                                                                    }
                                                                    PayReq req = new PayReq();
                                                                    req.appId = json.getString("appid");
                                                                    req.partnerId = json.getString("partnerid");
                                                                    req.prepayId = json.getString("prepayid");
                                                                    req.nonceStr = json.getString("noncestr");
                                                                    req.timeStamp = json.getString("timestamp");
                                                                    req.packageValue = json.getString("package");
                                                                    req.sign = json.getString("sign");
                                                                    MainApp.toast("正在调起支付");
                                                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                                                    //ecd8f2be7927bb017f23435267d403e3正式
                                                                    //
                                                                    Log.e(TAG, "api.sendReq = " + api.sendReq(req));
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }


                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });
                                    }
                                });


                            }
                        });


                    }
                });


            }
        });
    }


    private void httpPostCardAcctBalance(boolean rt_flag) {//是否强制刷新
        final CardAcctBalanceReq cardAcctBalanceReq = new CardAcctBalanceReq();
        MainApp.getInstance().buildBaseParams(cardAcctBalanceReq);
        cardAcctBalanceReq.setService("yct.base.card.acct.balance");
        if (rt_flag)
            cardAcctBalanceReq.setRt_flag("1");
        else
            cardAcctBalanceReq.setRt_flag("0");
        cardAcctBalanceReq.setCard_num("510000" + mAutoTV.getText().toString());
        cardAcctBalanceReq.setSign(YctApiMD5.encryptObjectMD5(cardAcctBalanceReq, MainApp.getInstance().Channel_Key));
        pDialog.setTitleText("正在查询余额，请稍候");
        pDialog.show();
        //检查卡片合法性
        MainApp.getInstance().okHttpPost(AppConstant.getBaseUrl(), JSON.toJSONString(cardAcctBalanceReq), new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainApp.toast("网络请求失败");
                        pDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();
                Logger.json(resp);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                        CardAcctBalanceResp cardAcctBalanceResp = JSON.parseObject(resp, CardAcctBalanceResp.class);
                        if (cardAcctBalanceResp.getResult_code() == null || !cardAcctBalanceResp.getResult_code().equals("0")) {
                            return;
                        }
                        ((TextView) findViewById(R.id.tv_acct_balance)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.tv_acct_balance)).setText("当前余额：" + (float) cardAcctBalanceResp.getBalance() / 100 + "元");
                        mAcctBalance = cardAcctBalanceResp.getBalance();

                    }
                });

            }
        });
    }

    private void httpPostOrderQuery() {
        final OrderQueryReq req = new OrderQueryReq();
        MainApp.getInstance().buildBaseParams(req);
        req.setService("yct.product.czj.order.query");
        List<String> order_list = new ArrayList<>();
        if (mOrder_Id.length() > 0)
            order_list.add(mOrder_Id);
        else {
            MainApp.toast("订单号为空");
            pDialog.dismiss();
            return;
        }
        req.setYct_order(order_list);
        req.setPage(1);
        req.setPagesize(1);
        req.setSign(YctApiMD5.encryptObjectMD5(req, MainApp.getInstance().Channel_Key));
        pDialog.setTitleText("正在确认到账...");
        pDialog.show();
        MainApp.getInstance().okHttpPost(AppConstant.getProductCzjUrl(), JSON.toJSONString(req), new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainApp.toast("网络请求失败");
                        pDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();
                Logger.json(resp);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        OrderQueryResp orderQueryResp = JSON.parseObject(resp, OrderQueryResp.class);
                        if (orderQueryResp.getResult_code() == null || !orderQueryResp.getResult_code().equals("0")) {
                            pDialog.dismiss();
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("错误提示：" + orderQueryResp.getErr_msg())
                                    .setConfirmText(" 确定 ")
                                    .show();
                            return;
                        }
                        if (orderQueryResp.getOrder().size() == 0) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("查无此订单，如有疑问，\n请联系客服热线：4008440001")
                                    .setConfirmText(" 确定 ")
                                    .show();
                            return;
                        }
                        if (orderQueryResp.getOrder().get(0).getOrderstatus().equals(OrderInfo.ORDER_INFO_STATUS_NOT_PAID)) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("未支付，请稍候...")
                                    .setConfirmText(" 确定 ")
                                    .show();
                        }
                        if (orderQueryResp.getOrder().get(0).getOrderstatus().equals(OrderInfo.ORDER_INFO_STATUS_PAID)) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("扣款成功，正在受理，请稍候...")
                                    .setConfirmText(" 确定 ")
                                    .show();
                        } else if (orderQueryResp.getOrder().get(0).getOrderstatus().equals(OrderInfo.ORDER_INFO_STATUS_DELI_APPLY)) {
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("已受理，正在充值，请稍候")
                                    .setConfirmText(" 确定 ")
                                    .show();
                        } else if (orderQueryResp.getOrder().get(0).getOrderstatus().equals(OrderInfo.ORDER_INFO_STATUS_DELI)) {
                            DL.log(TAG, "mAcctBalance = " + mAcctBalance);
                            DL.log(TAG, "mAmount = " + mAmount);
                            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("充值成功")
                                    .setContentText("原账户余额: " + df.format((float) mAcctBalance / 100) + "元" + "\n" +
                                            "充值金额:\t\t" + df.format((float) mAmount / 100) + "元" + "\n" +
                                            "现账户余额: " + df.format(((float) mAcctBalance + (float) mAmount) / 100) + "元" + "\n"
                                    )
                                    .setConfirmText(" 确定 ")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            //刷新账户余额
                                            sDialog.setConfirmClickListener(null);
                                            sDialog.dismiss();
                                            httpPostCardAcctBalance(true);
                                        }
                                    })
                                    .show();

                        }
                    }
                });

            }
        });
    }
}
