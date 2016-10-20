package com.gzyct.app.api.httpentity;

public class OrderInfo {

    public static final String ORDER_INFO_STATUS_NOT_PAID		= "1";		//未支付
    public static final String ORDER_INFO_STATUS_PAID			= "2";		//已支付
    public static final String ORDER_INFO_STATUS_DELI_APPLY		= "3";		//发货受理
    public static final String ORDER_INFO_STATUS_DELI			= "4";		//已发货
    public static final String ORDER_INFO_STATUS_REFUNDED		= "5";		//已退款


    private String channelcode; // 发起渠道
    private String userid; // 发起用户ID
    private String orderid; // 商品订单号
    private String ordersource; // 商品订单来源
    private String cardnum; // 十六位扬尘沟通卡号
    private String paychannelcode; // 支付渠道代码
    private String thirdpayorder; // 第三方支付订单号
    private int totalfee; // 商品总金额
    private int payfee; // 支付金额
    private String feetype; // 支付货币类型
    private int couponfee; // 优惠总金额
    private String couponinfo; // 优惠信息
    private String orderstatus; // 订单状态 - 用中文返回
    private int refundtimes; // 退款笔数
    private String refundid; // 退款单号
    private int refundfee; // 退款金额
    private String remark; // 附加信息
    private String createtime; // 创建时间
    private String updatetime; // 更新时间

    public String getChannelcode() {
        return channelcode;
    }

    public void setChannelcode(String channelcode) {
        this.channelcode = channelcode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrdersource() {
        return ordersource;
    }

    public void setOrdersource(String ordersource) {
        this.ordersource = ordersource;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getPaychannelcode() {
        return paychannelcode;
    }

    public void setPaychannelcode(String paychannelcode) {
        this.paychannelcode = paychannelcode;
    }

    public String getThirdpayorder() {
        return thirdpayorder;
    }

    public void setThirdpayorder(String thirdpayorder) {
        this.thirdpayorder = thirdpayorder;
    }

    public int getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(int totalfee) {
        this.totalfee = totalfee;
    }

    public int getPayfee() {
        return payfee;
    }

    public void setPayfee(int payfee) {
        this.payfee = payfee;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public int getCouponfee() {
        return couponfee;
    }

    public void setCouponfee(int couponfee) {
        this.couponfee = couponfee;
    }

    public String getCouponinfo() {
        return couponinfo;
    }

    public void setCouponinfo(String couponinfo) {
        this.couponinfo = couponinfo;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public int getRefundtimes() {
        return refundtimes;
    }

    public void setRefundtimes(int refundtimes) {
        this.refundtimes = refundtimes;
    }

    public String getRefundid() {
        return refundid;
    }

    public void setRefundid(String refundid) {
        this.refundid = refundid;
    }

    public int getRefundfee() {
        return refundfee;
    }

    public void setRefundfee(int refundfee) {
        this.refundfee = refundfee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getStatusStr(){
        if(orderstatus.equals(ORDER_INFO_STATUS_NOT_PAID)){
            return "未支付";
        }else if(orderstatus.equals(ORDER_INFO_STATUS_PAID)){
            return "已支付";
        }else if(orderstatus.equals(ORDER_INFO_STATUS_DELI_APPLY)){
            return "受理中";
        }else if(orderstatus.equals(ORDER_INFO_STATUS_DELI)){
            return "已发货";
        }else if(orderstatus.equals(ORDER_INFO_STATUS_REFUNDED)){
            return "已退款";
        }else {
            return orderstatus;
        }
    }
}
