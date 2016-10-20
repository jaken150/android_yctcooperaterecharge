package com.gzyct.app.api.httpentity;

import java.util.List;

//创建充值金加值商品订单
public class OrderQueryReq extends BaseParam{

	private List<String>	yct_order;		//羊城通商品订单
	private int				page;			//页数
	private int				pagesize;		//每页条数
	private String			condition;		//查询条件
	private String			yct_extend;		//羊城通扩展字段
	private String			biz_extend;		//合作社扩展字段

	public List<String> getYct_order() {

		return yct_order;
	}

	public void setYct_order(List<String> yct_order) {
		this.yct_order = yct_order;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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
