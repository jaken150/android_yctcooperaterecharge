package com.gzyct.app.api.httpentity;


import java.util.List;

public class OrderQueryResp extends BaseParam{
	private String			result_code;	//返回码
	private String			err_msg;		//错误描述
	private List<OrderInfo> order;			//订单信息
	private String			yct_extend;		//羊城通扩展字段
	private String			biz_extend;		//合作社扩展字段

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public List<OrderInfo> getOrder() {
		return order;
	}

	public void setOrder(List<OrderInfo> order) {
		this.order = order;
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
