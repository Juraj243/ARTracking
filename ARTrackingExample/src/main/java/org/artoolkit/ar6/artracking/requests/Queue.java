package org.artoolkit.ar6.artracking.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by duda on 5.11.2017.
 */

public class Queue {
    private static Queue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private Queue(Context context) {
        mCtx = context;
        mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    }

    public static synchronized Queue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Queue(context);
        }
        return mInstance;
    }

    public <T> void add(Request<T> req) {
        mRequestQueue.add(req);
    }
}
