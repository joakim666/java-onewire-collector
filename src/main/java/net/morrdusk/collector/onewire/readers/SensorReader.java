package net.morrdusk.collector.onewire.readers;

import com.google.api.client.util.DateTime;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import net.morrdusk.collector.onewire.Sensor;
import net.morrdusk.collector.onewire.domain.CounterReading;
import net.morrdusk.collector.onewire.domain.HumidityReading;
import net.morrdusk.collector.onewire.domain.Reading;
import net.morrdusk.collector.onewire.domain.TemperatureReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SensorReader {
    private static final Logger LOG = LoggerFactory.getLogger(SensorReader.class);

    private final CounterReader counterReader;
    private final HumidityReader humidityReader;
    private final TemperatureReader temperatureReader;

    @Inject
    public SensorReader(CounterReader counterReader,
                        HumidityReader humidityReader,
                        TemperatureReader temperatureReader) {
        this.counterReader = counterReader;
        this.humidityReader = humidityReader;
        this.temperatureReader = temperatureReader;
    }

    public List<Reading> readAll(File rootDir, DateTime readingDate) {
        List<Reading> readings = new ArrayList<Reading>();

        File[] files = rootDir.listFiles();
        for (File deviceDirectory : files) {
            if (deviceDirectory.isDirectory()) {
                List<Reading> r = read(deviceDirectory);
                for (Reading reading : r) {
                    reading.setDateTime(readingDate);
                }
                readings.addAll(r);
            }
        }
        return readings;
    }

    protected List<Reading> read(File deviceDirectory) {
        List<Reading> readings = new ArrayList<Reading>();

        final Sensor sensor = new Sensor(deviceDirectory.getName());
        if (sensor.isSupportedDevice()) {
            switch (sensor.getFamilyCode()) {
                case COUNTER:
                    final Reading counter = readCounter(deviceDirectory, sensor);
                    if (counter != null) {
                        readings.add(counter);
                    }
                    break;
                case HUMIDITY:
                    final Reading humidity = readHumidity(deviceDirectory, sensor);
                    if (humidity != null) {
                        readings.add(humidity);
                    }
                    break;
                case THERMOMETER:
                    final Reading temperature = readTemperature(deviceDirectory, sensor);
                    if (temperature != null) {
                        readings.add(temperature);
                    }
                    break;
            }
        }
        else {
            LOG.error("Unknown device with id: {}", sensor.getUniqueId());
        }

        return readings;
    }

    private Reading readTemperature(File deviceDirectory, Sensor sensor) {
        Reading reading = null;

        try {
            final BigDecimal temperature = temperatureReader.readValue(deviceDirectory);
            reading = new TemperatureReading(sensor.getDeviceId(), temperature);

        } catch (ParseException e) {
            LOG.error("Failed to read temperature sensor", e);
        }

        return reading;
    }

    private Reading readHumidity(File deviceDirectory, Sensor sensor) {
        Reading reading = null;

        try {
            final BigDecimal humidity = humidityReader.readValue(deviceDirectory);
            reading = new HumidityReading(sensor.getDeviceId(), humidity);

        } catch (ParseException e) {
            LOG.error("Failed to read humidity sensor", e);
        }
        
        return reading;
    }

    private Reading readCounter(File deviceDirectory, Sensor sensor) {
        Reading reading = null;

        try {
            final ImmutableList<Long> values = counterReader.readValues(deviceDirectory);
            reading = new CounterReading(sensor.getDeviceId(), values.get(0), values.get(1));
        } catch (ParseException e) {
            LOG.error("Failed to read counter sensor", e);
        } catch (IndexOutOfBoundsException e) {
            LOG.error("Failed to read counter sensor", e);
        }

        return reading;
    }
}
