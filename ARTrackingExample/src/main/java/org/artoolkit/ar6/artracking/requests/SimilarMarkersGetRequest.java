package org.artoolkit.ar6.artracking.requests;

import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.SimilarMarkers;

/**
 * Created by krist on 04-Mar-18.
 */

public class SimilarMarkersGetRequest extends GsonRequest<SimilarMarkers>{
    public SimilarMarkersGetRequest(String id, Response.Listener<SimilarMarkers> listener, Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(id), SimilarMarkers.class, null, listener, errorListener);
        addAuthorizationHeader();
    }

    private static String buildUrl(String id) {
        return BASE_URL +"/images/getSimilarImages/" +id;
    }
}
