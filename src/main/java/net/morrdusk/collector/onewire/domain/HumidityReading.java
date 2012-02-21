package net.morrdusk.collector.onewire.domain;

import com.google.api.client.util.Key;

import java.math.BigDecimal;

public class HumidityReading extends Reading {
    @Key
    private BigDecimal humidity;

    public HumidityReading(String deviceId, BigDecimal humidity) {
        super(deviceId);
        this.humidity = humidity;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return super.toString() + " HumidityReading{" +
                "humidity=" + humidity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HumidityReading that = (HumidityReading) o;

        if (humidity != null ? !humidity.equals(that.humidity) : that.humidity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return humidity != null ? humidity.hashCode() : 0;
    }
}
