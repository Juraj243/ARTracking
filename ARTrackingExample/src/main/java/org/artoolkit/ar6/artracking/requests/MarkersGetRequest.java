package org.artoolkit.ar6.artracking.requests;


import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.Wifi;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by duda on 9.12.2017.
 */

public class MarkersGetRequest extends GsonRequest<String[]>{
    public MarkersGetRequest(ArrayList<Wifi> wifis, Response.Listener<String[]> listener, Response.ErrorListener errorListener) {
        super(Method.POST, buildUrl(), String[].class, wifis, listener, errorListener);
        addAuthorizationHeader();
    }

    private static String buildUrl() {
        return BASE_URL +"/images/getImagesIDsByBssid";
    }
}
