package com.gppdi.ubipri.notification;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "Notifications")
public class Notification extends Model {

    @SerializedName("id")
    @Column(name = "id_event")
    private int eventId;

    @SerializedName("timestamp_in")
    @Column(name = "timestamp")
    private long timestamp;

    @SerializedName("send_format")
    @Column(name = "type")
    private String type;

    @SerializedName("send_message")
    @Column(name = "message")
    private String message;

    public enum State { NEW, READ }
    @Column(name = "state")
    private State state;

    public Notification() {
        super();
    }

    public Notification(int eventId, long timestamp, String type, String message, State state) {
        super();
        setEventId(eventId);
        setTimestamp(timestamp);
        setType(type);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
