package org.artoolkit.ar6.artracking.helpers;

/**
 * Created by krist on 17-Feb-18.
 */

public class UserContext {
    private static UserContext instance = null;

    private String Token;

    private UserContext() {
    }

    public static UserContext getInstance() {
        if (instance == null) {
            instance = new UserContext();
        }

        return instance;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
