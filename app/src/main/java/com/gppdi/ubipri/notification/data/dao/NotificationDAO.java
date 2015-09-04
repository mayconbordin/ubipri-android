package com.gppdi.ubipri.notification.data.dao;

import com.activeandroid.query.Select;
import com.gppdi.ubipri.data.dao.AbstractDAO;
import com.gppdi.ubipri.notification.data.models.Notification;

import java.util.List;

public class NotificationDAO extends AbstractDAO<Notification> {

    public NotificationDAO() {
        super(Notification.class);
    }

    public List<Notification> newest() {
        return new Select()
                .from(Notification.class)
                .orderBy("timestamp DESC")
                .execute();
    }

    public Notification newestSingle() {
        return new Select()
                .from(Notification.class)
                .orderBy("timestamp DESC")
                .executeSingle();
    }

    public Notification newestByIdSingle() {
        return new Select()
                .from(Notification.class)
                .orderBy("id_event DESC")
                .executeSingle();
    }

    public List<Notification> newestUnread() {
        return new Select()
                .from(Notification.class)
                .where("state == ?", Notification.STATE_NEW)
                .orderBy("timestamp DESC")
                .execute();
    }

}
