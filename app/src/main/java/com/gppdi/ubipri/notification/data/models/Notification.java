package com.gppdi.ubipri.notification.data.models;

import android.os.Bundle;

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
        init(0, 0, FORMAT_NONE, null, STATE_NEW);
    }

    public Notification(int eventId, long timestamp, int format, String message, int state) {
        super();
        init(eventId, timestamp, format, message, state);
    }

    private void init(int eventId, long timestamp, int format, String message, int state) {
        setEventId(eventId);
        setTimestamp(timestamp);
        setFormat(format);
        setMessage(message);
        setState(state);
    }

    public Notification(Bundle bundle) {
        init(
                bundle.getInt("id"),
                bundle.getLong("timestamp"),
                bundle.getInt("format"),
                bundle.getString("message"),
                bundle.getInt("state")
        );
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", getEventId());
        bundle.putLong("timestamp", getTimestamp());
        bundle.putInt("format", getFormat());
        bundle.putString("message", getMessage());
        bundle.putInt("state", getState());
        return bundle;
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
