package org.artoolkit.ar6.artracking.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.artoolkit.ar6.artracking.Constants;
import org.artoolkit.ar6.artracking.R;
import org.artoolkit.ar6.artracking.helpers.MsgHelper;
import org.artoolkit.ar6.artracking.helpers.SharedPreferencesHelper;
import org.artoolkit.ar6.artracking.helpers.StorageHelper;
import org.artoolkit.ar6.artracking.helpers.UserContext;
import org.artoolkit.ar6.artracking.models.MarkerG;
import org.artoolkit.ar6.artracking.models.Token;
import org.artoolkit.ar6.artracking.requests.LoginRequest;
import org.artoolkit.ar6.artracking.requests.MarkerGetRequest;
import org.artoolkit.ar6.artracking.requests.Queue;
import org.artoolkit.ar6.artracking.requests.TokenRequest;

import java.util.Set;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String token = SharedPreferencesHelper.read(SplashScreenActivity.this, Constants.TOKEN_KEY);
        if(token == null) {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            UserContext.getInstance().setToken(token);

            Intent intent = getIntent();
            if(intent.getAction() == "android.intent.action.VIEW") {
                Uri data = intent.getData();
                String markerId = data.getPath().substring(1);
                findMarker(markerId);
            }
            else {
                login();
            }
        }
    }

    private void showAR(String id) {
        //Bitmap b = StorageHelper.read(this, id);

        Intent intent = new Intent(this, ARTrackingActivity.class);
        intent.putExtra(Constants.MARKER_PATH_KEY, StorageHelper.buildPath(this, id));
        startActivity(intent);
    }

    private void findMarker(final String id) {
        if(StorageHelper.exists(this, id)) {
            showAR(id);
        }
        else {
            MarkerGetRequest request = new MarkerGetRequest(id, new Response.Listener<MarkerG>(){
                @Override
                public void onResponse(MarkerG response) {
                    StorageHelper.write(SplashScreenActivity.this, response);
                    showAR(id);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            Queue.getInstance(this).add(request);
        }
    }


    private void login() {
        TokenRequest request = new TokenRequest(new Response.Listener<Token>() {
            @Override
            public void onResponse(Token response) {
                SharedPreferencesHelper.write(SplashScreenActivity.this, Constants.TOKEN_KEY, response.getToken());
                UserContext.getInstance().setToken(response.getToken());
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Queue.getInstance(this).add(request);
    }
}
