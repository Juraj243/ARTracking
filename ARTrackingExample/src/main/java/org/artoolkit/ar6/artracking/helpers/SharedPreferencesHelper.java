package org.artoolkit.ar6.artracking.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by krist on 17-Feb-18.
 */

public class SharedPreferencesHelper {
    private static final String PREFS_KEY = "ar";

    public static boolean write(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return sharedPref.edit().putString(key, value).commit();
    }

    public static String read(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }
}
