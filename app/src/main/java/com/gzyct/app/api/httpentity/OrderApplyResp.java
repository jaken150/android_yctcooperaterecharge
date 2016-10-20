package com.gzyct.app.api.httpentity;


public class OrderApplyResp extends BaseParam{
	
	private String	result_code;	//返回码
	private String	err_msg;		//错误描述
	private String	order_id;		//商品订单号
	private String	order_source;	//订单来源
	private String	yct_extend;		//羊城通扩展字段
	private String	biz_extend;		//合作社扩展字段
	
	public OrderApplyResp()
	{
		result_code	= "";	//返回码
		err_msg		= "";	//错误描述
		order_id	= "";	//商品订单号
		order_source= "";	//订单来源
		yct_extend	= "";	//羊城通扩展字段
		biz_extend	= "";	//合作社扩展字段
	}
	
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
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_source() {
		return order_source;
	}
	public void setOrder_source(String order_source) {
		this.order_source = order_source;
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
