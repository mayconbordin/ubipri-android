package com.gppdi.ubipri.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.data.Fixtures;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Device;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author mayconbordin
 */
public class MockApiService implements ApiService {
    @Override
    public List<Environment> getEnvironments(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius) {
        return new ArrayList<Environment>(Fixtures.ENVIRONMENTS.values());
    }

    @Override
    public Observable<List<Environment>> getEnvironmentsObservable(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius) {
        return null;
    }

    @Override
    public Observable<Environment> getEnvironment(@Path("id") int id) {
        return Observable.from(Fixtures.ENVIRONMENTS.get(id));
    }

    @Override
    public Observable<List<Action>> updateUserLocationObservable(@Body Log log) {
        return null;
    }

    @Override
    public List<Action> updateUserLocation(@Body Log log) {
        return Fixtures.getActions();
    }

    @Override
    public void registerUserDevice(@Body Device device, Callback<Map> cb) {

    }

    @Override
    public Observable<Map> registerUserDeviceObservable(@Body Device device) {
        return Observable.<Map>from(new HashMap());
    }
}
