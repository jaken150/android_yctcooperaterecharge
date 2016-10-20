package com.gzyct.app.api;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gzyct.app.api.adapter.OrderAdapter;
import com.gzyct.app.api.httpentity.OrderInfo;
import com.gzyct.app.api.httpentity.OrderQueryReq;
import com.gzyct.app.api.httpentity.OrderQueryResp;
import com.gzyct.app.api.util.AppConstant;
import com.gzyct.app.api.util.DL;
import com.gzyct.app.api.util.YctApiMD5;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderQueryActivity extends Activity {
    private String TAG = "OrderQueryActivity";
    private UltimateRecyclerView mListView;
    private LinearLayoutManager mLinearLayoutManager;
    private SweetAlertDialog pDialog;
    private Handler mHandler = new Handler(Looper.myLooper());
    private OrderAdapter mAdapter;
    private List<OrderInfo> mList = new ArrayList<>();
    private int mPage = 1;
    private int mPageSize = 10;
    private boolean mIsLoadmoreNow = false;
    private boolean mIsRefreshNew = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_query);
        ((TextView) findViewById(R.id.tv_title)).setText("充值记录");
        initComponent();
        initListener();
        httpPostOrderQuery(mPage, mPageSize);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initComponent() {
        initListView();
        initProgressDialog();

    }

    private void initListView() {
        mListView = (UltimateRecyclerView) findViewById(R.id.ultimate_recycler_view);
        mListView.setHasFixedSize(false);
        mAdapter = new OrderAdapter(mList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLinearLayoutManager);

        enableLoadMore();
        mListView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        enableRefreshGoogleMaterialStyle();
        mListView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_CLEAR_ALL);


        mListView.setAdapter(mAdapter);

    }

    private void initProgressDialog() {
        pDialog = new SweetAlertDialog(OrderQueryActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(true);
    }

    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void httpPostOrderQuery(int page, int pagesize) {
        final OrderQueryReq req = new OrderQueryReq();
        MainApp.getInstance().buildBaseParams(req);
        req.setService("yct.product.czj.order.query");
        final List<String> order_list = new ArrayList<>();
        req.setYct_order(order_list);
        req.setPage(page);
        req.setPagesize(pagesize);
        req.setSign(YctApiMD5.encryptObjectMD5(req, MainApp.getInstance().Channel_Key));
        if (!mIsLoadmoreNow && !mIsLoadmoreNow) {
            pDialog.setTitleText("正在查询...");
            pDialog.show();
        }
        MainApp.getInstance().okHttpPost(AppConstant.getProductCzjUrl(), JSON.toJSONString(req), new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainApp.toast("网络请求失败");
                        if (pDialog.isShowing())
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
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        try {
                            OrderQueryResp orderQueryResp = JSON.parseObject(resp, OrderQueryResp.class);
                            if (!orderQueryResp.getResult_code().equals("0")) {
                                new SweetAlertDialog(OrderQueryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(orderQueryResp.getErr_msg())
                                        .setConfirmText(" 确定 ")
                                        .show();
                                return;
                            }
                            DL.log(TAG, "getOrder().size() = " + orderQueryResp.getOrder().size());
                            if (orderQueryResp.getOrder().size() > 0) {
//                            if (mIsRefreshNew) {//下拉刷新，先清除list里的数据
//                                mList.clear();
//                                mAdapter.notifyDataSetChanged();
//                            }
                                if (orderQueryResp.getOrder().size() < mPageSize) {
                                    //当返回数据小于pagesize时，禁止自动加载
                                    mListView.disableLoadmore();
                                    mAdapter.enableLoadMore(false);
                                } else {
                                    mListView.reenableLoadmore();
                                    mAdapter.enableLoadMore(true);
                                }
                                for (OrderInfo orderInfo : orderQueryResp.getOrder()) {
//                                    if (mIsRefreshNew && DL.DEBUGVERSION)
//                                        orderInfo.setCardnum(orderInfo.getCardnum() + "刷新标志");
                                    mAdapter.insertLastInternal(mList, orderInfo);
                                }
                                DL.log(TAG, "getItemCount = " + mAdapter.getItemCount());
                                DL.log(TAG, "getItemViewType = " + mAdapter.getItemViewType(mAdapter.getItemCount() - 1));
                                if (mIsLoadmoreNow) {
                                    mIsLoadmoreNow = false;
                                }
                                if (mIsRefreshNew) {
                                    mIsRefreshNew = false;
                                    mListView.setRefreshing(false);
                                    mLinearLayoutManager.scrollToPosition(0);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MainApp.toast("服务器返回数据格式不正确，请稍后重试");
                        }
                    }


                });

            }
        });
    }

    private void enableRefreshGoogleMaterialStyle() {
        mListView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mList.clear();
                mAdapter.notifyDataSetChanged();
                mListView.reenableLoadmore();
                mIsRefreshNew = true;
                mPage = 1;
                httpPostOrderQuery(mPage, mPageSize);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mListView.setDefaultSwipeToRefreshColorScheme(
                    getColor(android.R.color.holo_orange_light),
                    getColor(android.R.color.holo_orange_dark),
                    getColor(android.R.color.holo_red_dark),
                    getColor(android.R.color.holo_red_light));
        } else {
            mListView.setDefaultSwipeToRefreshColorScheme(
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_orange_dark),
                    getResources().getColor(android.R.color.holo_red_dark),
                    getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void enableLoadMore() {
        mListView.setLoadMoreView(R.layout.custom_bottom_progressbar);
        mListView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        DL.log(TAG, "loadMor... ");
                        mIsLoadmoreNow = true;
                        mPage++;
                        httpPostOrderQuery(mPage, mPageSize);
                    }
                }, 1000);
            }
        });
    }
}
