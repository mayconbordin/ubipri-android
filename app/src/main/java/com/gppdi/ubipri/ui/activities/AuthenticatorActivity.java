package com.gppdi.ubipri.ui.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gppdi.ubipri.R;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.api.AuthConstants;
import com.gppdi.ubipri.api.annotations.ClientId;
import com.gppdi.ubipri.api.annotations.ClientSecret;
import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.rx.EndlessObserver;

import javax.inject.Inject;

import butterknife.InjectView;
import retrofit.RetrofitError;
import rx.Observable;

/**
 * Activity which displays login screen to the user.
 */
public class AuthenticatorActivity extends BaseActivity {
    public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    private static final String TAG = "AuthenticatorActivity";

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;

    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password to be changed on the device.
     */
    private Boolean mConfirmCredentials = false;

    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;

    private String mUsername;
    private String mAccountType;
    private String mAuthTokenType;

    @InjectView(R.id.ok_button) Button mSubmit;
    @InjectView(R.id.username_edit) EditText mUsernameEdit;
    @InjectView(R.id.password_edit) EditText mPasswordEdit;

    @Inject AccountManager accountManager;
    @Inject ApiService apiService;
    @Inject @ClientId String clientId;
    @Inject @ClientSecret String clientSecret;


    @Override
    public void onCreate(Bundle icicle) {
        Log.d(TAG, "onCreate("+icicle+")");
        super.onCreate(icicle);

        inflateLayout(R.layout.activity_authenticator);

        mUsername = getIntent().getStringExtra(PARAM_USERNAME);
        mAuthTokenType = getIntent().getStringExtra(PARAM_AUTHTOKEN_TYPE);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = getIntent().getBooleanExtra(PARAM_CONFIRMCREDENTIALS, false);

        mUsernameEdit.setText(mUsername);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString());
            }
        });

        initAccountAuthenticatorResponse();
    }

    @Override
    protected boolean requireLogin() {
        return false;
    }

    private void doLogin(final String email, String password) {
        Observable<AccessToken> accessTokenObservable =
                apiService.getAccessTokenObservable(new Request.Builder()
                        .client(clientId, clientSecret)
                        .user(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString())
                        .build());

        subscribe(accessTokenObservable, new EndlessObserver<AccessToken>() {
            @Override
            public void onNext(AccessToken accessToken) {
                Account account = addOrFindAccount(email, accessToken.getRefreshToken());
                accountManager.setAuthToken(account, AuthConstants.AUTHTOKEN_TYPE, accessToken.getAccessToken());
                finishAccountAdd(email, accessToken.getAccessToken(), accessToken.getRefreshToken());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "Could not sign in", throwable);
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();

                if (throwable instanceof RetrofitError) {
                    RetrofitError err = (RetrofitError) throwable;
                    Log.e(TAG, "URL: " + err.getUrl());
                }
            }
        });
    }

    private Account addOrFindAccount(String email, String password) {
        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        Account account = accounts.length != 0 ? accounts[0] :
                new Account(email, AuthConstants.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            accountManager.addAccountExplicitly(account, password, null);
        } else {
            accountManager.setPassword(accounts[0], password);
        }
        return account;
    }

    private void finishAccountAdd(String accountName, String authToken, String password) {
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthConstants.ACCOUNT_TYPE);

        if (authToken != null) {
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
        }

        intent.putExtra(AccountManager.KEY_PASSWORD, password);
        setAccountAuthenticatorResult(intent.getExtras());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();

        // Go back to the main activity
        startActivity(new Intent(this, MainActivity.class));
    }

    public final void initAccountAuthenticatorResponse() {
        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    public void finish() {
        Log.d(TAG, "finish()");

        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }
}