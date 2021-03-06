package com.gppdi.ubipri.api;

import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Device;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.Log;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author mayconbordin
 */
public interface ApiService {
    @GET("/environments")
    List<Environment> getEnvironments(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius);

    @GET("/environments")
    Observable<List<Environment>> getEnvironmentsObservable(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius);

    @GET("/environments/{id}")
    Observable<Environment> getEnvironment(@Path("id") int id);

    @PUT("/user/location")
    Observable<List<Action>> updateUserLocationObservable(@Body Log log);

    @PUT("/user/location")
    List<Action> updateUserLocation(@Body Log log);

    @POST("/user/devices")
    void registerUserDevice(@Body Device device, Callback<Map> cb);

    @POST("/user/devices")
    Observable<Map> registerUserDeviceObservable(@Body Device device);
}
