package com.gzyct.app.api.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzyct.app.api.R;
import com.gzyct.app.api.httpentity.OrderInfo;
import com.gzyct.app.api.httpentity.OrderQueryResp;
import com.gzyct.app.api.httpentity.Status;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class QuickAdapter extends BaseQuickAdapter<OrderInfo> {
//    public QuickAdapter() {
//        super(R.layout.tweet, DataServer.getSampleData(100));
//    }

    public QuickAdapter( List<OrderInfo> dataList) {
        super( R.layout.listview_order, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfo item) {
        helper.setText(R.id.tv_card_num, item.getCardnum())
                .setText(R.id.tv_amount, item.getTotalfee()+"元")
                .setText(R.id.tv_status, item.getStatusStr())
                .setText(R.id.tv_order_id, item.getOrderid());

//        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.mipmap.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) helper.getView(R.id.tweetAvatar));
    }


}
