package com.gzyct.app.api.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzyct.app.api.R;
import com.gzyct.app.api.httpentity.OrderInfo;
import com.gzyct.app.api.util.DL;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

public class OrderAdapter extends UltimateViewAdapter {

    private List<OrderInfo> mList;

    public class OrderViewHolder extends UltimateRecyclerviewViewHolder {

        private TextView card_num;
        private TextView amount;
        private TextView time;
        private TextView order_id;
        private TextView order_status;

        public OrderViewHolder(View itemView, boolean isNormal) {
            super(itemView);
            if (isNormal) {
                card_num = (TextView) itemView.findViewById(R.id.tv_card_num);
                amount = (TextView) itemView.findViewById(R.id.tv_amount);
                time = (TextView) itemView.findViewById(R.id.tv_time);
                order_id = (TextView) itemView.findViewById(R.id.tv_order_id);
                order_status = (TextView) itemView.findViewById(R.id.tv_status);
            }
        }
    }

    public OrderAdapter(List<OrderInfo> mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new OrderViewHolder(view, false);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_order, parent, false);
        // set the view's size, margins, paddings and layout parameters
        OrderViewHolder vh = new OrderViewHolder(v, true);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.NORMAL) {
            OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
            orderViewHolder.card_num.setText(mList.get(position).getCardnum());
            orderViewHolder.amount.setText("充值 " + df.format((float) mList.get(position).getTotalfee() / 100) + "元");
            orderViewHolder.order_status.setText(mList.get(position).getStatusStr());
            orderViewHolder.time.setText(mList.get(position).getCreatetime());
            orderViewHolder.order_id.setText(mList.get(position).getOrderid());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


}
