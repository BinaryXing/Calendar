package com.xing.android.calendar.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zhaoxx on 15/12/30.
 */
public class LogUtil {

    private static final String DEFAULT_TAG = "XING";

    public static void v(String tag, String msg) {
        v(tag, msg, true);
    }

    public static void v(String tag, String msg, boolean isLogged) {
        if(Config.DEBUG && isLogged && !TextUtils.isEmpty(msg)) {
            if(TextUtils.isEmpty(tag)) {
                tag = DEFAULT_TAG;
            }
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        d(tag, msg, true);
    }

    public static void d(String tag, String msg, boolean isLogged) {
        if(Config.DEBUG && isLogged && !TextUtils.isEmpty(msg)) {
            if(TextUtils.isEmpty(tag)) {
                tag = DEFAULT_TAG;
            }
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        i(tag, msg, true);
    }

    public static void i(String tag, String msg, boolean isLogged) {
        if(Config.DEBUG && isLogged && !TextUtils.isEmpty(msg)) {
            if(TextUtils.isEmpty(tag)) {
                tag = DEFAULT_TAG;
            }
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        w(tag, msg, true);
    }

    public static void w(String tag, String msg, boolean isLogged) {
        if(Config.DEBUG && isLogged && !TextUtils.isEmpty(msg)) {
            if(TextUtils.isEmpty(tag)) {
                tag = DEFAULT_TAG;
            }
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg, true);
    }

    public static void e(String tag, String msg, boolean isLogged) {
        if(Config.DEBUG && isLogged && !TextUtils.isEmpty(msg)) {
            if(TextUtils.isEmpty(tag)) {
                tag = DEFAULT_TAG;
            }
            Log.e(tag, msg);
        }
    }
}
