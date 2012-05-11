package net.morrdusk.collector.onewire.readers

import com.google.inject.Injector
import com.google.inject.Guice
import net.morrdusk.collector.onewire.CollectorModule
import net.morrdusk.collector.onewire.domain.CounterReading
import net.morrdusk.collector.onewire.domain.HumidityReading
import net.morrdusk.collector.onewire.domain.TemperatureReading
import com.google.api.client.util.DateTime


class SensorReaderTest extends GroovyTestCase {

    Injector injector

    void setUp() {
        injector = Guice.createInjector(new CollectorModule())
    }

    void testReadCounter() {
        def sensorReader = injector.getInstance(SensorReader.class)

        def readings = sensorReader.read(new File("src/test/resources/test-sensors/1D.91B80D000000"))
        assertEquals(1, readings.size())
        def counter = readings.get(0)
        assertEquals("91B80D000000", counter.getDeviceName())
        assertEquals(21191234, ((CounterReading) counter).getCounterA())
        assertEquals(1, ((CounterReading) counter).getCounterB())
    }

    void testReadHumidity() {
        def sensorReader = injector.getInstance(SensorReader.class)

        def readings = sensorReader.read(new File("src/test/resources/test-sensors/26.2D3011010000"))
        assertEquals(1, readings.size())
        def firstReading = readings.get(0)
        assertEquals("2D3011010000", firstReading.getDeviceName())
        assertEquals(new BigDecimal("24.7563"), ((HumidityReading) firstReading).getHumidity())
    }

    void testReadTemperature() {
        def sensorReader = injector.getInstance(SensorReader.class)

        def readings = sensorReader.read(new File("src/test/resources/test-sensors/28.3E5D70020000"))
        assertEquals(1, readings.size())
        def firstReading = readings.get(0)
        assertEquals("3E5D70020000", firstReading.getDeviceName())
        assertEquals(new BigDecimal("-2.25"), ((TemperatureReading) firstReading).getTemperature())
    }
    
    void testReadAll() {
        def sensorReader = injector.getInstance(SensorReader.class)

        def date = new DateTime(new Date(), TimeZone.getDefault())
        
        def readings = sensorReader.readAll(new File("src/test/resources/test-sensors"), date)
        assertEquals(3, readings.size())

        def readings0 = readings.find { it.getDeviceName().equals("91B80D000000") }
        assertEquals("91B80D000000", readings0.getDeviceName())
        assertEquals(21191234, ((CounterReading) readings0).getCounterA())
        assertEquals(1, ((CounterReading) readings0).getCounterB())

        def readings1 = readings.find { it.getDeviceName().equals("2D3011010000") }
        assertEquals("2D3011010000", readings1.getDeviceName())
        assertEquals(new BigDecimal("24.7563"), ((HumidityReading) readings1).getHumidity())

        def readings2 = readings.find { it.getDeviceName().equals("3E5D70020000") }
        assertEquals("3E5D70020000", readings2.getDeviceName())
        assertEquals(new BigDecimal("-2.25"), ((TemperatureReading) readings2).getTemperature())
    }
}
