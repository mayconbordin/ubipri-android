package com.gppdi.ubipri.api;

import android.accounts.AccountManager;
import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.gppdi.ubipri.api.account.AccountAuthenticator;
import com.gppdi.ubipri.api.annotations.ClientId;
import com.gppdi.ubipri.api.annotations.ClientSecret;
import com.gppdi.ubipri.api.json.EnvironmentDeserializer;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.notification.api.ApiNotificationService;
import com.gppdi.ubipri.notification.services.RegistrationIntentService;
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
    injects = {AccountAuthenticator.class, RegistrationIntentService.class},
    complete = false,
    library = true
)
public final class ApiModule {
    private static final String NOTIFICATION_API_URL = "http://10.200.116.240:8080/";
    private static final String SIGAI_API_URL = "http://10.200.116.37/api/";
    private static final String UBIPRI_API_URL = "http://10.200.116.37:9000/";
    private static final String CLIENT_ID = "ubipri-android";
    private static final String CLIENT_SECRET = "d86654a991a8558b7ae5350fdb84457b763ad042";

    @Provides @Singleton @ClientId String provideClientId() {
        return CLIENT_ID;
    }

    @Provides @Singleton @ClientSecret String provideClientSecret() {
        return CLIENT_SECRET;
    }

    /*@Provides @Singleton Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }*/

    @Provides Authenticator provideAuthenticator(Application application, AccountManager accountManager) {
        return new ApiAuthenticator(application, accountManager);
    }

    @Provides OkHttpClient provideOkHttpClient(Authenticator authenticator) {
        return new OkHttpClient().setAuthenticator(authenticator);
    }

    @Provides @Singleton Client provideClient(OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides @Singleton ApiHeaders provideApiHeaders(Application application) {
        return new ApiHeaders(application);
    }

    @Provides Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(Environment.class, new EnvironmentDeserializer())
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .create();
    }

    /*@Provides @Singleton RestAdapter provideRestAdapter(Endpoint endpoint, Client client, ApiHeaders headers, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(headers)
                .build();
    }*/

    @Provides @Singleton ApiService provideApiService(Client client, ApiHeaders headers, Gson gson) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(Endpoints.newFixedEndpoint(UBIPRI_API_URL))
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(headers)
                .build();

        return restAdapter.create(ApiService.class);
    }

    @Provides @Singleton ApiAuthService provideApiAuthService(Client client, ApiHeaders headers, Gson gson) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(Endpoints.newFixedEndpoint(SIGAI_API_URL))
                .setConverter(new GsonConverter(gson))
                //.setRequestInterceptor(headers)
                .build();

        return restAdapter.create(ApiAuthService.class);
    }

    /*@Provides @Singleton ApiDatabase provideApiDatabase(ApiService service) {
        return new ApiDatabase(service);
    }*/

    @Provides @Singleton
    ApiNotificationService provideApiNotificationService(Client client, ApiHeaders headers, Gson gson) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(Endpoints.newFixedEndpoint(NOTIFICATION_API_URL))
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(headers)
                .build();

        return restAdapter.create(ApiNotificationService.class);
    }
}