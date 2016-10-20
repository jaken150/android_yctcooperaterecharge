package com.gzyct.app.api.httpentity;


public class CardAcctBalanceResp extends BaseParam{

	private String	result_code;	//返回码
	private String	err_msg;		//错误描述

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
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

	private int balance;


	
}
