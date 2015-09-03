package com.gppdi.ubipri.notification.api;

import com.gppdi.ubipri.notification.data.models.Notification;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface ApiNotificationService {

    // TODO: 25/08/15 Update the server's URL and fields
    @POST("/history")
    void historyUpdate(@Body Notification newestNotification, Callback<List<Notification>> cb);

    @POST("/gcm/token")
    void registerGcmToken(@Body String gcmToken, Callback cb);

}
