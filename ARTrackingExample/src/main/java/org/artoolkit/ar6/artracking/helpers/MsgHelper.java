package org.artoolkit.ar6.artracking.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by duda on 8.11.2017.
 */

public class MsgHelper {
    public static void show(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
