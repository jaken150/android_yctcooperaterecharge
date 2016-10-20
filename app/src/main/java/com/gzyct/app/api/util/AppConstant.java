package com.gzyct.app.api.util;


import com.gzyct.app.api.BuildConfig;

public class AppConstant {
    public static boolean DEBUGVERSION = BuildConfig.DEBUG_VERSION;
//    public static String WX_APP_ID = "wx36d499f4e33d2d44";
    public static String WX_APP_ID = "wx94e6d3d53b09a871";

    //商品中心
    public static String PRODUCT_CZJ_URL = "http://wxtest.gzyct.com:8000/test/gw/product";
    public static String PRODUCT_CZJ_URL_DEBUG = "http://wxtest.gzyct.com:8000/test/gw/product";

    //支付中心
    public static String PAY_CENTER_URL = "http://wxtest.gzyct.com:8000/test/gw/paycenter/";
    public static String PAY_CENTER_URL_DEBUG = "http://wxtest.gzyct.com:8000/test/gw/paycenter/";

    //基础功能
    public static String BASE_URL =  "http://wxtest.gzyct.com:8000/test/gw/base/";
    public static String BASE_URL_DEBUG = "http://wxtest.gzyct.com:8000/test/gw/base/";

    //APP后台
    public static String YCTI_URL = "http://112.94.161.30/yctI/";
    public static String YCTI_URL_DEBUG = "http://218.20.156.175:9110/yctI/";

    public static String getPayCenterUrl() {
        if (DEBUGVERSION)
            return PAY_CENTER_URL_DEBUG;
        else
            return PAY_CENTER_URL;
    }

    public static String getProductCzjUrl() {
        if (DEBUGVERSION)
            return PRODUCT_CZJ_URL_DEBUG;
        else
            return PRODUCT_CZJ_URL;
    }

    public static String getBaseUrl() {
        if (DEBUGVERSION)
            return BASE_URL_DEBUG;
        else
            return BASE_URL;
    }

    public static String getYCTIUrl() {
        if (DEBUGVERSION)
            return YCTI_URL_DEBUG;
        else
            return YCTI_URL;
    }

}
