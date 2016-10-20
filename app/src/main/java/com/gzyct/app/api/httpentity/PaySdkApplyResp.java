package com.gzyct.app.api.httpentity;

public class PaySdkApplyResp extends BaseParam {
    private String result_code;
    private String err_msg;
    private String pay_channel;
    private String order_id;
    private String pay_info;
    private String yct_extend;
    private String biz_extend;

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

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
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
