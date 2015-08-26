package com.gppdi.ubipri.notification;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table(name = "Notifications")
public class Notification extends Model {

    @SerializedName("id")
    @Column(name = "id")
    private int eventId;

    @SerializedName("timestamp_in")
    private long timestamp;

    @SerializedName("send_format")
    private String type;

    @SerializedName("send_message")
    private String message;

    public enum State { NEW, READ }
    private State state;

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
