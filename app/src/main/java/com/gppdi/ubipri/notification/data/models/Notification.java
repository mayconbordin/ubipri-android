package com.gppdi.ubipri.notification.data.models;

import android.os.Bundle;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Table(name = "Notifications")
public class Notification extends Model {

    // Local database column names
    public static final String FIELD_ID = "EventId";
    public static final String FIELD_TIMESTAMP = "Timestamp";
    public static final String FIELD_FORMAT = "Format";
    public static final String FIELD_MESSAGE = "Message";
    public static final String FIELD_STATE = "State";

    // Possible state values
    public static final int STATE_NEW = 0;
    public static final int STATE_READ = 1;

    // Possible format values
    public static final int FORMAT_NONE = 0;
    public static final int FORMAT_GCM = 1;
    public static final int FORMAT_SMS = 2;
    public static final int FORMAT_EMAIL = 3;
    public static final int FORMAT_HISTORY = 4;

    @Expose @SerializedName("id")
    @Column(name = FIELD_ID, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int eventId;

    @Expose @SerializedName("timestamp_in")
    @Column(name = FIELD_TIMESTAMP)
    private long timestamp;

    @Expose @SerializedName("send_format")
    @Column(name = FIELD_FORMAT)
    private int format;

    @Expose @SerializedName("send_message")
    @Column(name = FIELD_MESSAGE)
    private String message;

    @Column(name = FIELD_STATE)
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
                bundle.getInt(FIELD_ID),
                bundle.getLong(FIELD_TIMESTAMP),
                bundle.getInt(FIELD_FORMAT),
                bundle.getString(FIELD_MESSAGE),
                bundle.getInt(FIELD_STATE)
        );
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(FIELD_ID, getEventId());
        bundle.putLong(FIELD_TIMESTAMP, getTimestamp());
        bundle.putInt(FIELD_FORMAT, getFormat());
        bundle.putString(FIELD_MESSAGE, getMessage());
        bundle.putInt(FIELD_STATE, getState());
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
