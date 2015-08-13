package com.gppdi.ubipri.api;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author mayconbordin
 */
public interface ApiService {
    @POST("/login")
    void login(@Path("username") String username, @Path("password") String password, Callback<Map> cb);

    @POST("/logout")
    void logout(Callback<Void> cb);

    @GET("/environments")
    void getEnvironments(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius, Callback<List<Environment>> cb);

    @GET("/environments/{id}")
    void getEnvironment(@Path("id") int id, Callback<Environment> cb);
}
