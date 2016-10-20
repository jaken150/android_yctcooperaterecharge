package com.gzyct.app.api.util;

import android.content.Context;
import android.provider.Settings.Secure;

public class Helper {

    public static String getAndroid_id(Context context){
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }


}
