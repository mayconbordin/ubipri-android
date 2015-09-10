package com.gppdi.ubipri.api;

import com.gppdi.ubipri.api.oauth2.AccessToken;
import com.gppdi.ubipri.api.oauth2.Request;
import com.gppdi.ubipri.data.models.Action;
import com.gppdi.ubipri.data.models.Device;
import com.gppdi.ubipri.data.models.Environment;
import com.gppdi.ubipri.data.models.Log;
import com.gppdi.ubipri.data.models.User;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author mayconbordin
 */
public interface ApiAuthService {
    @POST("/oauth/access_token")
    AccessToken getAccessToken(@Body Request request);

    @POST("/oauth/access_token")
    AccessToken refreshAccessToken(@Body Request request);

    @POST("/oauth/access_token")
    Observable<AccessToken> getAccessTokenObservable(@Body Request request);

    @POST("/auth/register")
    void register(@Body User user, Callback<Map> cb);
}
