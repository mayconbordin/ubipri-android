package com.gppdi.ubipri.notification.data.dao;

import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.notification.Notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NotificationDAOTest {

    private NotificationDAO dao;

    public NotificationDAOTest() {
        dao = new NotificationDAO();
    }

    @Test
    public void testCreate() {
        Notification n = new Notification(1, 1000, "SMS", "SMS Test", Notification.State.READ);
        dao.createOrUpdate(n);
        assertTrue(dao.exists(n.getId()));
    }

    @Test
    public void testNewest() {
        populate();
        assertTrue(dao.count() == 3);
        Notification n = dao.newest().get(0);
        assertTrue(n.getEventId() == 3);
        assertTrue(n.getState() == Notification.State.NEW);
    }

    private void populate() {
        dao.createOrUpdate(new Notification(1, 1000, "SMS", "SMS Test", Notification.State.READ));
        dao.createOrUpdate(new Notification(2, 2000, "GCM", "GCM Test", Notification.State.NEW));
        dao.createOrUpdate(new Notification(3, 3000, "SMS", "SMS Test", Notification.State.NEW));
    }

}
