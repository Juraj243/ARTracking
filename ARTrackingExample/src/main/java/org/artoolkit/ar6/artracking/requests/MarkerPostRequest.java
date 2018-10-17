package org.artoolkit.ar6.artracking.requests;

import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.Id;
import org.artoolkit.ar6.artracking.models.Marker;

/**
 * Created by duda on 8.12.2017.
 */

public class MarkerPostRequest extends GsonRequest<Id>{
    public MarkerPostRequest(Marker marker, Response.Listener<Id> listener, Response.ErrorListener errorListener) {
        super(Method.POST, buildUrl(), Id.class, marker, listener, errorListener);
        addAuthorizationHeader();
    }

    private static String buildUrl() {
        return BASE_URL + "/images/";
    }
}
