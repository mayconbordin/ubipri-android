package com.gppdi.ubipri.ui.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.f2prateek.dart.Dart;
import com.gppdi.ubipri.R;
import com.gppdi.ubipri.UbiPriApplication;
import com.gppdi.ubipri.api.ApiService;
import com.gppdi.ubipri.api.AuthConstants;
import com.gppdi.ubipri.api.annotations.ClientId;
import com.gppdi.ubipri.api.annotations.ClientSecret;
import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.rx.EndlessObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
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

    private AccountManager mAccountManager;
    private Thread mAuthThread;
    private String mAuthtoken;
    private String mAuthtokenType;

    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password to be changed on the device.
     */
    private Boolean mConfirmCredentials = false;

    /** for posting authentication attempts back to UI thread */
    private final Handler mHandler = new Handler();
    TextView mMessage;
    private String mPassword;
    @InjectView(R.id.password_edit) EditText mPasswordEdit;

    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;

    private String mUsername;

    @InjectView(R.id.username_edit) EditText mUsernameEdit;

    private Spinner mAccountTypeSpinner;
    private String mAccountType;

    @InjectView(R.id.ok_button) Button mSubmit;

    @Inject AccountManager accountManager;
    @Inject ApiService apiService;
    @Inject @ClientId String clientId;
    @Inject @ClientSecret String clientSecret;

    private ViewGroup container;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {
        Log.i(TAG, "onCreate(" + icicle + ")");
        super.onCreate(icicle);


        inflateLayout(R.layout.activity_authenticator);


        mUsername = getIntent().getStringExtra(PARAM_USERNAME);
        mAuthtokenType = getIntent().getStringExtra(PARAM_AUTHTOKEN_TYPE);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = getIntent().getBooleanExtra(PARAM_CONFIRMCREDENTIALS, false);

        mUsernameEdit.setText(mUsername);

        Log.i(TAG, "Auth token type: " + mAuthtokenType);
        Log.i(TAG, "Client ID: " + clientId);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*apiService.getAccessToken(new Request.Builder()
                                .client(clientId, clientSecret)
                                .user(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString())
                                .build()
                );*/


                doLogin(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString());
            }
        });






        /*mAccountManager = AccountManager.get(this);
        Log.i(TAG, "loading data from Intent");
        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mAuthtokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        mRequestNewAccount = mUsername == null;
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRMCREDENTIALS,
                false);

        Log.i(TAG, "    request new: " + mRequestNewAccount);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);

        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
                android.R.drawable.ic_dialog_alert);

        mMessage = (TextView) findViewById(R.id.message);
        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);

        mUsernameEdit.setText(mUsername);
        mMessage.setText(getMessage());

        mAccountTypeSpinner = (Spinner) findViewById(R.id.accountType_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[] { "Google Account" });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccountTypeSpinner.setAdapter(adapter);*/

        initAccountAuthenticatorResponse();
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
                // accountManager.setUserData(account, AccountAuthenticator.USER_ID, accessToken.userId);
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
        if (authToken != null)
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
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