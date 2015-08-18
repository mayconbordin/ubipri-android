package com.gppdi.ubipri.api;

import android.accounts.AccountManager;
import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.gppdi.ubipri.account.AccountAuthenticator;
import com.gppdi.ubipri.api.annotations.ClientId;
import com.gppdi.ubipri.api.annotations.ClientSecret;
import com.gppdi.ubipri.ui.activities.LoginActivity;
import com.gppdi.ubipri.ui.activities.MainActivity;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(
    injects = {
            MainActivity.class, LoginActivity.class, AccountAuthenticator.class
    },
    complete = false,
    library = true
)
public final class ApiModule {
    public static final String PRODUCTION_API_URL = "http://10.200.116.246/api/";
    private static final String CLIENT_ID = "ubipri-android";
    private static final String CLIENT_SECRET = "d86654a991a8558b7ae5350fdb84457b763ad042";

    @Provides @Singleton @ClientId
    String provideClientId() {
        return CLIENT_ID;
    }

    @Provides @Singleton @ClientSecret
    String provideClientSecret() {
        return CLIENT_SECRET;
    }

    @Provides @Singleton Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }

    @Provides
    Authenticator provideAuthenticator(Application application, AccountManager accountManager) {
        return new ApiAuthenticator(application, accountManager);
    }

    @Provides OkHttpClient provideOkHttpClient(Authenticator authenticator) {
        return new OkHttpClient().setAuthenticator(authenticator);
    }

    @Provides @Singleton Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .create();
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint, Client client, ApiHeaders headers, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(headers)
                //.setErrorHandler(new RestErrorHandler())
                .build();
    }

    @Provides @Singleton
    ApiService provideApiService(RestAdapter restAdapter) {
        return restAdapter.create(ApiService.class);
    }

    /*@Provides @Singleton ApiDatabase provideApiDatabase(ApiService service) {
        return new ApiDatabase(service);
    }*/
}