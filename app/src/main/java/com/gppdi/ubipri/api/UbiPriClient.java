package com.gppdi.ubipri.api;

import android.content.Context;

import com.gppdi.ubipri.account.UbiPriCredentials;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author mayconbordin
 */
public class UbiPriClient {
    private static UbiPriClient instance;

    private Context context;
    private ApiService api;
    private UbiPriCredentials credentials;

    public static UbiPriClient getInstance(Context context) {
        if (instance == null) {
            instance = new UbiPriClient(context);
        }
        return instance;
    }

    private UbiPriClient(Context context) {
        this.context = context;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://localhost:9000")
                .build();

        api = restAdapter.create(ApiService.class);
    }

    public boolean isAuthenticated() {
        return UbiPriCredentials.ensureCredentials(context);
    }

    public UbiPriCredentials getCredentials() {
        if (credentials == null) {
            credentials = UbiPriCredentials.get(context);
        }
        return credentials;
    }

    public void reAuthenticate() {
        api.login(getCredentials().getUsername(), getCredentials().getPassword(), new Callback<Map>() {
            @Override
            public void success(Map map, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void authenticate(final String username, final String password, final Callback<Map> cb) {
        api.login(username, password, new Callback<Map>() {
            @Override
            public void success(Map map, Response response) {
                getCredentials().setCredentials(username, password, (String)map.get("authToken"));
                cb.success(map, response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

}
