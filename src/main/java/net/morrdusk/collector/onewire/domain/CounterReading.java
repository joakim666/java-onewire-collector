package net.morrdusk.collector.onewire.domain;

import com.google.api.client.util.Key;

public class CounterReading extends Reading {
    @Key
    private long counterA;
    @Key
    private long counterB;

    public CounterReading(String deviceId, long counterA, long counterB) {
        super(deviceId);
        this.counterA = counterA;
        this.counterB = counterB;
    }

    public long getCounterA() {
        return counterA;
    }

    public void setCounterA(long counterA) {
        this.counterA = counterA;
    }

    public long getCounterB() {
        return counterB;
    }

    public void setCounterB(long counterB) {
        this.counterB = counterB;
    }

    @Override
    public String toString() {
        return super.toString() + " CounterReading{" +
                "counterA=" + counterA +
                ", counterB=" + counterB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterReading that = (CounterReading) o;

        if (counterA != that.counterA) return false;
        if (counterB != that.counterB) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (counterA ^ (counterA >>> 32));
        result = 31 * result + (int) (counterB ^ (counterB >>> 32));
        return result;
    }
}
