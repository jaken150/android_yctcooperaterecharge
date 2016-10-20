package com.gzyct.app.api.httpentity;

public class CardAcctBalanceReq extends BaseParam {
    public static String BUSI_FLAG_DEFAULT = "0";    //0:默认
    public static String BUSI_FLAG_RESTRICTED = "1";        //1:仅广州(比如支付宝的限制)
    public static String RF_FLAG_DEFAULT = "0";    //0:默认
    public static String RF_FLAG_YES = "1";        //1:实时（即时从数据库中拉取）

    private String card_num;
    private String busi_flag;
    private String rt_flag;

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {

        this.card_num = card_num;
    }

    public String getRt_flag() {
        return rt_flag;
    }

    public void setRt_flag(String rt_flag) {
        this.rt_flag = rt_flag;
    }

    public String getBusi_flag() {
        return busi_flag;
    }

    public void setBusi_flag(String busi_flag) {
        this.busi_flag = busi_flag;
    }
}
