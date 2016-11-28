package com.xing.android.calendar.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by zxx09506 on 2016/11/16.
 */

public class ToastUtil {

    private static final String LOG_TAG = "ToastUtil";

    public static void showShortToast(Context context, String text) {
        if(context == null) {
            LogUtil.w(LOG_TAG, "showShortToast:context is null");
            return;
        } else if(TextUtils.isEmpty(text)) {
            LogUtil.w(LOG_TAG, "showShortToast:text is empty");
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, int res) {
        if(context == null) {
            LogUtil.w(LOG_TAG, "showShortToast:context is null");
            return;
        }
        String text = context.getResources().getString(res);
        if(TextUtils.isEmpty(text)) {
            LogUtil.w(LOG_TAG, "showShortToast:text is empty, res = " + res);
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        if(context == null) {
            LogUtil.w(LOG_TAG, "showLongToast:context is null");
            return;
        } else if(TextUtils.isEmpty(text)) {
            LogUtil.w(LOG_TAG, "showLongToast:text is empty");
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(Context context, int res) {
        if(context == null) {
            LogUtil.w(LOG_TAG, "showLongToast:context is null");
            return;
        }
        String text = context.getResources().getString(res);
        if(TextUtils.isEmpty(text)) {
            LogUtil.w(LOG_TAG, "showLongToast:text is empty, res = " + res);
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
