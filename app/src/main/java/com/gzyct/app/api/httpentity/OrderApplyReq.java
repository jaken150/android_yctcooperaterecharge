package com.gzyct.app.api.httpentity;

import java.util.List;

//创建充值金加值商品订单
public class OrderApplyReq extends BaseParam{

	private String 			card_num;		//16位卡号
	private int				total_fee;		//商品总金额
	private String 			pay_channel;	//支付渠道
    private String			pay_channel_type;//支付渠道类型
	private int				pay_fee;		//支付金额
	private String			fee_type;		//支付货币类型
	private int				coupon_fee;		//优惠总金额
	private List<String>	coupon_info;	//优惠券信息
	private String			yct_extend;		//羊城通扩展字段
	private String			biz_extend;		//合作社扩展字段


	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	public String getPay_channel() {
		return pay_channel;
	}
	public void setPay_channel(String pay_channel) {
		this.pay_channel = pay_channel;
	}
    public String getPay_channel_type() {
        return pay_channel_type;
    }
    public void setPay_channel_type(String pay_channel_type) {
        this.pay_channel_type = pay_channel_type;
    }
	public int getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(int pay_fee) {
		this.pay_fee = pay_fee;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public int getCoupon_fee() {
		return coupon_fee;
	}
	public void setCoupon_fee(int coupon_fee) {
		this.coupon_fee = coupon_fee;
	}
	public List<String> getCoupon_info() {
		return coupon_info;
	}
	public void setCoupon_info(List<String> coupon_info) {
		this.coupon_info = coupon_info;
	}
	public String getYct_extend() {
		return yct_extend;
	}
	public void setYct_extend(String yct_extend) {
		this.yct_extend = yct_extend;
	}
	public String getBiz_extend() {
		return biz_extend;
	}
	public void setBiz_extend(String biz_extend) {
		this.biz_extend = biz_extend;
	}




}
