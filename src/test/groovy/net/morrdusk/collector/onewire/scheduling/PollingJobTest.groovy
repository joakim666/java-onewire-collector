package net.morrdusk.collector.onewire.scheduling

import static org.mockito.Mockito.*
import net.morrdusk.collector.onewire.readers.SensorReader
import org.quartz.JobExecutionContext
import org.quartz.JobKey
import org.quartz.JobDetail
import net.morrdusk.collector.onewire.db.ReadingsView
import net.morrdusk.collector.onewire.domain.CounterReading
import net.morrdusk.collector.onewire.domain.TemperatureReading
import net.morrdusk.collector.onewire.domain.Reading
import net.morrdusk.collector.onewire.db.ReadingKey
import org.quartz.Scheduler
import com.google.api.client.util.DateTime

class PollingJobTest extends GroovyTestCase {

    PollingJob pollingJob
    SensorReader mockedSensorReader
    ReadingsView mockedReadingsView

    void setUp() {
        mockedSensorReader = mock(SensorReader.class)
        mockedReadingsView = mock(ReadingsView.class)
        pollingJob = new PollingJob(mockedSensorReader, mockedReadingsView)
    }

    void testExecute() {
        def context = mock(JobExecutionContext.class)
        def jobDetail = mock(JobDetail.class)
        def jobKey = new JobKey("test")
        def scheduler = mock(Scheduler.class)
        when(context.getJobDetail()).thenReturn(jobDetail)
        when(context.getScheduler()).thenReturn(scheduler)
        when(jobDetail.getKey()).thenReturn(jobKey)


        def date = new DateTime(new Date(), TimeZone.getDefault())
        
        def reading1 = new CounterReading("C1", 1, 2)
        reading1.setDateTime(date)
        def reading2 = new TemperatureReading("T1", new BigDecimal(12.5))
        reading2.setDateTime(date)

        def list = new ArrayList<Reading>()
        list.add(reading1)
        list.add(reading2)
        when(mockedSensorReader.readAll(anyObject(), anyObject())).thenReturn(list)
        
        pollingJob.execute(context)
        
        verify(mockedSensorReader).readAll(anyObject(), anyObject())
        verify(mockedReadingsView).save(new ReadingKey(reading1.getDeviceId() + date.getValue()), reading1)
        verify(mockedReadingsView).save(new ReadingKey(reading2.getDeviceId() + date.getValue()), reading2)

        verify(scheduler).triggerJob(new JobKey("uploadJob"))
    }

}
