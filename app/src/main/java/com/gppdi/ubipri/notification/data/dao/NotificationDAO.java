package com.gppdi.ubipri.notification.data.dao;

import com.activeandroid.query.Select;
import com.gppdi.ubipri.data.dao.AbstractDAO;
import com.gppdi.ubipri.notification.Notification;

import java.util.List;

public class NotificationDAO extends AbstractDAO<Notification> {

    public NotificationDAO() {
        super(Notification.class);
    }

    public List<Notification> newest() {
        return new Select().from(Notification.class).orderBy("timestamp_in DESC").execute();
    }

}
