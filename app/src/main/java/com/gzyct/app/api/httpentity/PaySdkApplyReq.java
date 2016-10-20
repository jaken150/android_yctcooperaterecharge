package com.gzyct.app.api.httpentity;

public class PaySdkApplyReq extends BaseParam {

    private String pay_channel;
    private String pay_channel_type;//支付渠道类型
    private int pay_fee;
    private String fee_type;
    private String source;
    private String order_id;
    private String order_source;
    private String body;
    private String detail;
    private String yct_extend;//"羊城通扩展字段，JSON格式 false"
    private String biz_extend;//"回带的扩展字段，JSON格式合作单位填入 false"

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


    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getPay_channel_type() {
        return pay_channel_type;
    }

    public void setPay_channel_type(String pay_channel_type) {
        this.pay_channel_type = pay_channel_type;
    }
}
