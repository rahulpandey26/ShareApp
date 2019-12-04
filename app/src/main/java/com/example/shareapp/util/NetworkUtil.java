package com.example.shareapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        if (context == null)
            return false;

        NetworkInfo networkInfo = getNetworkInfo(context);
        return (networkInfo != null && networkInfo.isConnected());
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }
}
