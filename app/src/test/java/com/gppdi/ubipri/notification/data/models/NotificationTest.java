package com.gppdi.ubipri.notification.data.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.notification.data.dao.NotificationDAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NotificationTest {

    @Test
    public void testNotificationFromJSON() {
        final String json = "{ \"id\": 456, \"timestamp_in\": 3000, \"send_format\": 2, \"send_message\": \"SMS TEST\" }";
        Gson gson = new GsonBuilder().create();
        Notification notification = gson.fromJson(json, Notification.class);
        assertTrue(notification.getEventId() == 456);
        assertTrue(notification.getTimestamp() == 3000);
        assertTrue(notification.getFormat() == Notification.FORMAT_SMS);
        assertTrue(notification.getMessage().equals("SMS TEST"));
    }

    @Test
    public void testUniqueId() {
        NotificationDAO dao = new NotificationDAO();
        Notification n1 = new Notification();
        Notification n2 = new Notification();
        n1.setEventId(1);
        n2.setEventId(1);
        n1.save();
        n2.save();
        assertTrue(dao.count() == 1);
    }

}
