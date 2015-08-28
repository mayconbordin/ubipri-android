package com.gppdi.ubipri.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.data.Fixtures;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author mayconbordin
 */
public class MockApiService implements ApiService {
    @Override
    public AccessToken getAccessToken(@Body Request request) {
        return null;
    }

    @Override
    public AccessToken refreshAccessToken(@Body Request request) {
        return null;
    }

    @Override
    public Observable<AccessToken> getAccessTokenObservable(@Body Request request) {
        return null;
    }

    @Override
    public Observable<List<Environment>> getEnvironments(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius) {
        return Observable.<List<Environment>>from(new ArrayList<Environment>(Fixtures.ENVIRONMENTS.values()));
    }

    @Override
    public Observable<Environment> getEnvironment(@Path("id") int id) {
        return Observable.from(Fixtures.ENVIRONMENTS.get(id));
    }

    @Override
    public Observable<Void> updateUserLocation(@Body Log log) {
        return null;
    }
}
