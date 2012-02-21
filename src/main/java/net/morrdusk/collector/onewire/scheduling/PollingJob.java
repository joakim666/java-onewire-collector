package net.morrdusk.collector.onewire.scheduling;

import com.google.api.client.util.DateTime;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.inject.Inject;
import net.morrdusk.collector.onewire.db.ReadingKey;
import net.morrdusk.collector.onewire.db.ReadingsView;
import net.morrdusk.collector.onewire.domain.CounterReading;
import net.morrdusk.collector.onewire.domain.Reading;
import net.morrdusk.collector.onewire.readers.SensorReader;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.rmic.iiop.DirectoryLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PollingJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(PollingJob.class);


    private final SensorReader sensorReader;
    private final ReadingsView dbView;

    @Inject
    public PollingJob(SensorReader sensorReader, ReadingsView dbView) {
        this.sensorReader = sensorReader;
        this.dbView = dbView;
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();

        Calendar calendar = Calendar.getInstance();
        final DateTime readingDate = new DateTime(calendar.getTime(), calendar.getTimeZone());

        LOG.debug(jobKey + " executing at " + readingDate);

        List<Reading> readings = sensorReader.readAll(new File("/var/1-wire/uncached"), readingDate);

        save(readings);

        try {
            context.getScheduler().triggerJob(new JobKey("uploadJob"));
        } catch (SchedulerException e) {
            LOG.error("Failed to trigger uploadJob", e);
        }
    }

    protected void save(List<Reading> readings) {
        for (Reading reading : readings) {
            dbView.save(makeKey(reading.getDeviceId(), reading.getDateTime()), reading);
        }
    }

    protected ReadingKey makeKey(String deviceId, DateTime dateTime) {
        return new ReadingKey(deviceId + dateTime.getValue());
    }

}
