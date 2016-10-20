package com.gzyct.app.api;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzyct.app.api.adapter.DataServer;
import com.gzyct.app.api.adapter.QuickAdapter;
import com.gzyct.app.api.httpentity.OrderInfo;
import com.gzyct.app.api.httpentity.OrderQueryReq;
import com.gzyct.app.api.httpentity.OrderQueryResp;
import com.gzyct.app.api.util.AppConstant;
import com.gzyct.app.api.util.DL;
import com.gzyct.app.api.util.YctApiMD5;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BaseRecyclerViewActivity extends Activity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "BaseRecyclerViewActivity";
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View notLoadingView;


    private static final int PAGE_SIZE = 5;

    private int delayMillis = 1000;


    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycler);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
//        addHeadView();
//        mRecyclerView.setAdapter(mQuickAdapter);
    }
    @Override
    public void onRefresh() {
        mPage = 1;
        httpPostOrderQuery(mPage, PAGE_SIZE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();
                Logger.json(resp);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        OrderQueryResp orderQueryResp = JSON.parseObject(resp, OrderQueryResp.class);
                        if (!orderQueryResp.getResult_code().equals("0")) {
                            new SweetAlertDialog(BaseRecyclerViewActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(orderQueryResp.getErr_msg())
                                    .setConfirmText(" 确定 ")
                                    .show();
                            return;
                        }
                        DL.log(TAG, "getOrder().size() = " + orderQueryResp.getOrder().size());
                        if (orderQueryResp.getOrder().size() > 0) {
                            final List<OrderInfo> final_orderList = orderQueryResp.getOrder();
                            mQuickAdapter.setNewData(final_orderList);
                            mQuickAdapter.openLoadMore(PAGE_SIZE, true);
                            mQuickAdapter.removeAllFooterView();
                            mSwipeRefreshLayout.setRefreshing(false);
//                            if (orderQueryResp.getOrder().size() < PAGE_SIZE) {
//                                mQuickAdapter.notifyDataChangedAfterLoadMore(false);
//                                if (notLoadingView == null) {
//                                    notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
//                                }
//                                mQuickAdapter.addFooterView(notLoadingView);
//                            }
                        }
                    }
                });
            }
        });

    }

    private void initAdapter() {
        httpPostOrderQuery(mPage, PAGE_SIZE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body().string();
                Logger.json(resp);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        OrderQueryResp orderQueryResp = JSON.parseObject(resp, OrderQueryResp.class);
                        if (!orderQueryResp.getResult_code().equals("0")) {
                            new SweetAlertDialog(BaseRecyclerViewActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(orderQueryResp.getErr_msg())
                                    .setConfirmText(" 确定 ")
                                    .show();
                            return;
                        }
                        DL.log(TAG, "getOrder().size() = " + orderQueryResp.getOrder().size());
                        //
                        if (orderQueryResp.getOrder().size() > 0) {
                            mQuickAdapter = new QuickAdapter(orderQueryResp.getOrder());
                            mQuickAdapter.openLoadAnimation();
                            mRecyclerView.setAdapter(mQuickAdapter);
                            mQuickAdapter.setOnLoadMoreListener(BaseRecyclerViewActivity.this);
                            mQuickAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
                            mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Toast.makeText(BaseRecyclerViewActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
                                }
                            });
                            addHeadView();
//                            if (orderQueryResp.getOrder().size() < PAGE_SIZE) {
//                                mQuickAdapter.notifyDataChangedAfterLoadMore(false);
//                                if (notLoadingView == null) {
//                                    notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
//                                }
//                                mQuickAdapter.addFooterView(notLoadingView);
//                            }
                        }
                        //
                    }
                });

            }
        });

    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) headView.findViewById(R.id.tv)).setText("click use custom loadmore view");
        final View customLoading = getLayoutInflater().inflate(R.layout.custom_loading, (ViewGroup) mRecyclerView.getParent(), false);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickAdapter.setLoadingView(customLoading);//自定义加载更多视图
                mRecyclerView.setAdapter(mQuickAdapter);
                Toast.makeText(BaseRecyclerViewActivity.this, "use ok!", Toast.LENGTH_LONG).show();
            }
        });
        mQuickAdapter.addHeaderView(headView);
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mQuickAdapter.getData().size()!= 0 && mQuickAdapter.getData().size() % PAGE_SIZE != 0) {
                    mQuickAdapter.notifyDataChangedAfterLoadMore(false);
                    if (notLoadingView == null) {
                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
                    }
                    mQuickAdapter.addFooterView(notLoadingView);
                } else {

                mPage++;
                httpPostOrderQuery(mPage, PAGE_SIZE, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String resp = response.body().string();
                        Logger.json(resp);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                OrderQueryResp orderQueryResp = JSON.parseObject(resp, OrderQueryResp.class);
                                if (!orderQueryResp.getResult_code().equals("0")) {
                                    new SweetAlertDialog(BaseRecyclerViewActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(orderQueryResp.getErr_msg())
                                            .setConfirmText(" 确定 ")
                                            .show();
                                    return;
                                }
                                DL.log(TAG, "getOrder().size() = " + orderQueryResp.getOrder().size());
                                if (orderQueryResp.getOrder().size() > 0) {
                                    final List<OrderInfo> final_orderList = orderQueryResp.getOrder();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mQuickAdapter.notifyDataChangedAfterLoadMore(final_orderList, true);
                                        }
                                    }, delayMillis);

//                                    if (orderQueryResp.getOrder().size() < PAGE_SIZE) {
//                                        mQuickAdapter.notifyDataChangedAfterLoadMore(false);
//                                        if (notLoadingView == null) {
//                                            notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
//                                        }
//                                        mQuickAdapter.addFooterView(notLoadingView);
//                                    }
                                }
                                //
                            }
                        });
                    }
                });


//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mQuickAdapter.notifyDataChangedAfterLoadMore(DataServer.getSampleData(PAGE_SIZE), true);
//                        mCurrentCounter = mQuickAdapter.getData().size();
//                    }
//                }, delayMillis);
                }
            }

        });
    }

    private void httpPostOrderQuery(int page, int pagesize, Callback callback) {
        final OrderQueryReq req = new OrderQueryReq();
        MainApp.getInstance().buildBaseParams(req);
        req.setService("yct.product.czj.order.query");
        final List<String> order_list = new ArrayList<>();
        req.setYct_order(order_list);
        req.setPage(page);
        req.setPagesize(pagesize);
        req.setSign(YctApiMD5.encryptObjectMD5(req, MainApp.getInstance().Channel_Key));
        MainApp.getInstance().okHttpPost(AppConstant.getProductCzjUrl(), JSON.toJSONString(req), callback);
//        MainApp.getInstance().okHttpPost(AppConstant.getProductCzjUrl(), JSON.toJSONString(req), new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String resp = response.body().string();
//                Logger.json(resp);
//                OrderQueryResp orderQueryResp = JSON.parseObject(resp, OrderQueryResp.class);
//                if (!orderQueryResp.getResult_code().equals("0")) {
//                    new SweetAlertDialog(BaseRecyclerViewActivity.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(orderQueryResp.getErr_msg())
//                            .setConfirmText(" 确定 ")
//                            .show();
//                    return;
//                }
//                DL.log(TAG, "getOrder().size() = " + orderQueryResp.getOrder().size());
//                if (orderQueryResp.getOrder().size() > 0) {
//                    if (orderQueryResp.getOrder().size() < PAGE_SIZE) {
//                    } else {
//                    }
//                }
//            }
//        });
    }

}
