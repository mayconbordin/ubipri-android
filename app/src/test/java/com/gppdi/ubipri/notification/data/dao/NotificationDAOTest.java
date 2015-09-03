package com.gppdi.ubipri.notification.data.dao;

import com.gppdi.ubipri.BuildConfig;
import com.gppdi.ubipri.notification.data.models.Notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

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
        Notification n = new Notification(1, 1000, Notification.FORMAT_SMS, "SMS Test", Notification.STATE_READ);
        dao.createOrUpdate(n);
        assertTrue(dao.exists(n.getId()));
    }

    @Test
    public void testNewest() {
        populate();
        assertTrue(dao.count() == 3);
        Notification n = dao.newest().get(0);
        assertTrue(n.getEventId() == 3);
    }

    @Test
    public void testNewestByIdSingle() {
        populate();
        assertTrue(dao.count() == 3);
        Notification n = dao.newestByIdSingle();
        assertTrue(n.getEventId() == 3);
    }

    @Test
    public void testNewestUnread() {
        populate();
        assertTrue(dao.count() == 3);
        List<Notification> notifications = dao.newestUnread();
        assertTrue(notifications.size() == 2);
        for(Notification n : notifications) {
            assertTrue(n.getState() == Notification.STATE_NEW);
        }
    }

    private void populate() {
        dao.createOrUpdate(new Notification(1, 1000, Notification.FORMAT_SMS, "SMS Test", Notification.STATE_READ));
        dao.createOrUpdate(new Notification(2, 2000, Notification.FORMAT_GCM, "GCM Test", Notification.STATE_NEW));
        dao.createOrUpdate(new Notification(3, 3000, Notification.FORMAT_SMS, "SMS Test", Notification.STATE_NEW));
    }

}
