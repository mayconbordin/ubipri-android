package com.gppdi.ubipri.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.api.UbiPriClient;
import com.gppdi.ubipri.utils.DialogUtils;

import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author mayconbordin
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private UbiPriClient client;

    private Button btnSignIn;
    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_login);

        client = UbiPriClient.getInstance(this);

        btnSignIn   = (Button) findViewById(R.id.btnSingIn);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnSignIn.setOnClickListener(this);
    }

    private Login validateAndGetLogin() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        boolean valid = true;

        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username can't be empty");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password can't be empty");
            valid = false;
        }

        return valid ? new Login(username, password) : null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSingIn) {
            Log.i(TAG, "Logging in");

            Login login = validateAndGetLogin();

            if (login == null) {
                return;
            }

            client.authenticate(login.username, login.password, new Callback<Map>() {
                @Override
                public void success(Map map, Response response) {
                    Log.i(TAG, "Logged");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Unable to log in: " + error.getMessage(), error);

                    if (error.getResponse() != null && error.getResponse().getStatus() == 401) {
                        DialogUtils.error(LoginActivity.this, "Invalid credentials", "The given credentials are not valid");
                    } else {
                        Toast.makeText(LoginActivity.this, "Unable to authenticate", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private static class Login {
        public String username;
        public String password;

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
