package org.artoolkit.ar6.artracking.requests;

import com.android.volley.Response;

import org.artoolkit.ar6.artracking.models.Token;
import org.artoolkit.ar6.artracking.models.User;

/**
 * Created by krist on 17-Feb-18.
 */

public class LoginRequest extends GsonRequest<Token> {
    public LoginRequest(User user, Response.Listener<Token> listener, Response.ErrorListener errorListener) {
        super(Method.POST, buildUrl(), Token.class, user, listener, errorListener);
    }

    private static String buildUrl() {
        return BASE_URL +"/user/auth";
    }
}
