package net.morrdusk.collector.onewire.domain;

import com.google.api.client.util.Key;

import java.math.BigDecimal;

public class TemperatureReading extends Reading {
    @Key
    private BigDecimal temperature;

    public TemperatureReading(String deviceId, BigDecimal temperature) {
        super(deviceId);
        this.temperature = temperature;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return super.toString() + " TemperatureReading{" +
                "temperature=" + temperature +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemperatureReading that = (TemperatureReading) o;

        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return temperature != null ? temperature.hashCode() : 0;
    }
}
