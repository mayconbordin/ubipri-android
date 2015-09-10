package com.gppdi.ubipri.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.gppdi.ubipri.R;
import com.gppdi.ubipri.api.ApiAuthService;
import com.gppdi.ubipri.data.models.User;

import java.util.Map;

import javax.inject.Inject;

import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    @InjectView(R.id.edtName) EditText edtName;
    @InjectView(R.id.edtEmail) EditText edtEmail;
    @InjectView(R.id.edtPassword) EditText edtPassword;
    @InjectView(R.id.edtPasswordConfirm) EditText edtPasswordConfirm;
    @InjectView(R.id.btnRegister) CircularProgressButton btnRegister;

    @Inject ApiAuthService apiAuthService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflateLayout(R.layout.activity_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setIndeterminateProgressMode(true);
                btnRegister.setProgress(50);

                User user = validate();

                if (user == null) {
                    btnRegister.setProgress(0);
                    return;
                }

                register(user);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected boolean requireLogin() {
        return false;
    }

    private void register(User user) {
        apiAuthService.register(user, new Callback<Map>() {
            @Override
            public void success(Map map, Response response) {
                btnRegister.setProgress(100);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Could not register user", error);

                btnRegister.setProgress(0);
                String message = "";

                switch (error.getKind()) {
                    case NETWORK:
                        message = "Network error";
                        break;
                    case HTTP:
                        if (error.getResponse() != null && error.getResponse().getStatus() == 401) {
                            message = "Invalid credentials";
                        } else {
                            message = "Invalid credentials";
                        }
                        break;
                    case CONVERSION:
                        message = "Response error";
                        break;
                    default:
                        message = "Unexpected error";
                        break;
                }

                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private User validate() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String passwordConfirm = edtPasswordConfirm.getText().toString();

        boolean valid = true;

        if (name.trim().length() == 0) {
            edtName.setError("Can't be empty");
            valid = false;
        }

        if (email.trim().length() == 0) {
            edtEmail.setError("Can't be empty");
            valid = false;
        }

        if (password.trim().length() == 0) {
            edtPassword.setError("Can't be empty");
            valid = false;
        }

        if (!password.equals(passwordConfirm)) {
            edtPassword.setError("Passwords don't match");
            valid = false;
        }

        if (!valid) {
            return null;
        }

        return new User(name, email, password);
    }
}
