package com.gppdi.ubipri.api;

import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author mayconbordin
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/oauth/access_token")
    AccessToken getAccessToken(@Body Request request);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    AccessToken refreshAccessToken(@Body Request request);

    @FormUrlEncoded
    @POST("/login")
    void login(@Field("username") String username, @Field("password") String password, Callback<Map> cb);

    @POST("/logout")
    void logout(Callback<Void> cb);

    @GET("/environments")
    void getEnvironments(@Query("lat") Double lat, @Query("lon") Double lon, @Query("radius") Double radius, Callback<List<Environment>> cb);

    @GET("/environments/{id}")
    void getEnvironment(@Path("id") int id, Callback<Environment> cb);
}
