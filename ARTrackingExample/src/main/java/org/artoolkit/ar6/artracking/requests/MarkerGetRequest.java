package org.artoolkit.ar6.artracking.requests;

import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.MarkerG;

/**
 * Created by duda on 8.12.2017.
 */

public class MarkerGetRequest extends GsonRequest<MarkerG> {
    public MarkerGetRequest(String id, Response.Listener<MarkerG> listener, Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(id), MarkerG.class, null, listener, errorListener);
        addAuthorizationHeader();
    }

    private static String buildUrl(String id) {
        return BASE_URL +"/images/getImageByID/" +id;
    }
}
