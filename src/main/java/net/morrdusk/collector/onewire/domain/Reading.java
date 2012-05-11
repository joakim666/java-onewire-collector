package net.morrdusk.collector.onewire.domain;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.io.Serializable;

public abstract class Reading implements Serializable {
    @Key
    private DateTime dateTime;
    @Key
    private String deviceName;

    public Reading(String deviceName) {
        this.deviceName = deviceName;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "dateTime=" + dateTime +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
