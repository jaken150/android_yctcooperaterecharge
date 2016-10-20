package com.gzyct.app.api.util;

import android.content.Context;
import com.orhanobut.logger.Logger;

import android.util.Log;
import android.widget.Toast;

import com.gzyct.app.api.BuildConfig;

import java.text.SimpleDateFormat;

public class DL {

    public static boolean IS_NEED_LOGIN = true;

    public static boolean DEBUGLOG = true;
    public static boolean DEBUGVERSION = BuildConfig.DEBUG_VERSION;
    public static String DEBUG_MOBILE = "";
    public static String DEBUG_PWD = "";

    public static void log(String tag, String text) {
        if (DEBUGLOG)
            Log.e(tag,text);
    }
    public static void log(String text) {
        if (DEBUGLOG)
            Logger.e(text);
    }

    public static void toast(Context ctx, String toast) {
        Toast.makeText(ctx, toast, Toast.LENGTH_SHORT).show();
    }

    public static void debugToast(Context ctx, String toast) {
        if (DEBUGVERSION)
            Toast.makeText(ctx, "Debug: " + toast, Toast.LENGTH_SHORT).show();
    }

    public static String getTimestamp(){
        SimpleDateFormat    sDateFormat    =   new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String    date    =    sDateFormat.format(new    java.util.Date());
        return date;
    }

}
