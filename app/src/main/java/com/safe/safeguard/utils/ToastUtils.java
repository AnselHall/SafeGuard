package com.safe.safeguard.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ansel on 16/2/20.
 */
public class ToastUtils {
    private static Toast toast;
    public static void toast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }

        toast.setText(msg);
        toast.show();
    }
}
