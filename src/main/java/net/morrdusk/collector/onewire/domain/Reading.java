package net.morrdusk.collector.onewire.domain;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public abstract class Reading implements Serializable {
    @Key
    private DateTime dateTime;
    @Key
    private String deviceId;

    public Reading(String deviceId) {
        this.deviceId = deviceId;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "dateTime=" + dateTime +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
