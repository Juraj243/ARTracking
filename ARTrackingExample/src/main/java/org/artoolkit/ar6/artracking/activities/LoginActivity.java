package org.artoolkit.ar6.artracking.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.artoolkit.ar6.artracking.Constants;
import org.artoolkit.ar6.artracking.R;
import org.artoolkit.ar6.artracking.helpers.UserContext;
import org.artoolkit.ar6.artracking.helpers.MsgHelper;
import org.artoolkit.ar6.artracking.helpers.SharedPreferencesHelper;
import org.artoolkit.ar6.artracking.models.Token;
import org.artoolkit.ar6.artracking.models.User;
import org.artoolkit.ar6.artracking.requests.LoginRequest;
import org.artoolkit.ar6.artracking.requests.Queue;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout mUserLayout;
    private EditText mUser;
    private TextInputLayout mPasswordLayout;
    private EditText mPassword;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUser = (EditText) findViewById(R.id.et_user);
        mUserLayout = (TextInputLayout) findViewById(R.id.user);
        mPassword = (EditText) findViewById(R.id.et_password);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean userValid = validateUser();
                if(userValid) {
                    login();
                }
            }
        });
    }

    private void login() {
        User user = new User();
        user.setUserName(mUser.getText().toString());
        user.setPassword(mPassword.getText().toString());

        LoginRequest request = new LoginRequest(user,
                new Response.Listener<Token>() {
                    @Override
                    public void onResponse(Token response) {
                        String token = response.getToken();
                        SharedPreferencesHelper.write(LoginActivity.this, Constants.TOKEN_KEY, token);
                        UserContext.getInstance().setToken(token);
                        LoadingEnd();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 401) {
                            MsgHelper.show(LoginActivity.this, R.string.msg_unauthorized);
                        }
                        else {
                            MsgHelper.show(LoginActivity.this, R.string.msg_error);
                        }
                        LoadingEnd();
                    }
                });
        LoadingStart();
        Queue.getInstance(LoginActivity.this).add(request);
    }

    private void LoadingStart() {
        mLoginBtn.setEnabled(false);
    }

    private void LoadingEnd() {
        mLoginBtn.setEnabled(true);
    }

    private boolean validateUser() {
        if(mUser.getText().toString().trim().isEmpty()) {
            mUserLayout.setError(getString(R.string.err_user));
            mUserLayout.setErrorEnabled(true);
            return false;
        }
        else {
            mUserLayout.setErrorEnabled(false);
            return true;
        }
    }
}
