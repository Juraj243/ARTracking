package org.artoolkit.ar6.artracking.requests;

import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.Image;

import java.util.Map;

/**
 * Created by krist on 20-Dec-17.
 */

public class ThumbGetRequest extends GsonRequest<Image>{
    public ThumbGetRequest(String id, Response.Listener<Image> listener, Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(id), Image.class, null, listener, errorListener);
        addAuthorizationHeader();
    }

    private static String buildUrl(String id) {
        return BASE_URL +"/images/getThumbnail/" +id;
    }
}
