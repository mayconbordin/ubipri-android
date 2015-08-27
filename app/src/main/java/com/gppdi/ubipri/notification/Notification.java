package com.gppdi.ubipri.notification;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "Notifications")
public class Notification extends Model {

    // Possible state values
    public static final int STATE_NEW = 0;
    public static final int STATE_READ = 1;

    // Possible format values
    public static final int FORMAT_NONE = 0;
    public static final int FORMAT_GCM = 1;
    public static final int FORMAT_SMS = 2;
    public static final int FORMAT_EMAIL = 3;
    public static final int FORMAT_HISTORY = 4;

    @SerializedName("id")
    @Column(name = "id_event")
    private int eventId;

    @SerializedName("timestamp_in")
    @Column(name = "timestamp")
    private long timestamp;

    @SerializedName("send_format")
    @Column(name = "format")
    private int format;

    @SerializedName("send_message")
    @Column(name = "message")
    private String message;

    @Column(name = "state")
    private int state;

    public Notification() {
        super();
    }

    public Notification(int eventId, long timestamp, int format, String message, int state) {
        super();
        setEventId(eventId);
        setTimestamp(timestamp);
        setFormat(format);
        setMessage(message);
        setState(state);
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
