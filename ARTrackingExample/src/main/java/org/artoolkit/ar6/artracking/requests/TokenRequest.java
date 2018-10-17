package org.artoolkit.ar6.artracking.requests;

import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.Token;

import java.util.Map;

/**
 * Created by krist on 17-Feb-18.
 */

public class TokenRequest extends GsonRequest<Token> {
    public TokenRequest(Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(), Token.class, null, listener, errorListener);
        addAuthorizationHeader();
    }

    private static String buildUrl() {
        return BASE_URL +"/user/renewToken";
    }
}
