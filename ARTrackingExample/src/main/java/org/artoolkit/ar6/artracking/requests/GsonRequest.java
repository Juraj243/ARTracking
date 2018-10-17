package org.artoolkit.ar6.artracking.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.artoolkit.ar6.artracking.helpers.UserContext;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duda on 8.12.2017.
 */

abstract class GsonRequest<T> extends Request<T> {
    protected static final String BASE_URL = "https://imagedbartoolkit.herokuapp.com";

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Object body;
    private final Response.Listener<T> listener;

    protected GsonRequest(int method, String url, Class<T> clazz, Object body,
                          Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = new HashMap<String, String>();
        this.headers.put("Content-Type", "application/json");
        this.body = body;
        this.listener = listener;
    }

    protected void addAuthorizationHeader() {
        this.headers.put("Authorization", "Token " +UserContext.getInstance().getToken());
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? gson.toJson(body).getBytes() : super.getBody();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
