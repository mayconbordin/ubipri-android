package com.gppdi.ubipri.notification.api;

import com.gppdi.ubipri.notification.Notification;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.POST;

public interface NotificationClient {

    // TODO: 25/08/15 Update the server's URL and fields
    @POST("/history")
    List<Notification> historyUpdate(@Field("username") String username);

}
