package net.morrdusk.collector.onewire.scheduling

import static org.mockito.Mockito.mock
import net.morrdusk.collector.onewire.db.ReadingsView

import static org.mockito.Mockito.when
import net.morrdusk.collector.onewire.db.ReadingKey
import net.morrdusk.collector.onewire.domain.Reading

import net.morrdusk.collector.onewire.domain.CounterReading
import net.morrdusk.collector.onewire.domain.TemperatureReading
import com.google.api.client.testing.http.MockHttpTransport

import com.google.mockwebserver.MockResponse
import com.google.mockwebserver.MockWebServer
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.util.DateTime
import net.morrdusk.collector.onewire.db.ReadingsTransferStorage
import org.quartz.JobExecutionContext
import org.quartz.JobDetail
import org.quartz.JobKey
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.never

class UploadJobTest extends GroovyTestCase {

    ReadingsTransferStorage mockedTransferStorage
    UploadJob uploadJob
    MockHttpTransport httpTransport

    void testExecuteSuccessful() {
        def context = mock(JobExecutionContext.class)
        mockedTransferStorage = mock(ReadingsTransferStorage.class)
        def dateTime = new DateTime(new Date(), TimeZone.getDefault())
        def readings = makeReadings(dateTime)
        when(mockedTransferStorage.collectReadingsToSend()).thenReturn(readings)

        // Enqueue response
        MockWebServer server = new MockWebServer();
        def mockResponse = new MockResponse()
        mockResponse.addHeader("Content-Type: application/json")
        mockResponse.setBody("{\"resultCode\":\"OK\"}")
        server.enqueue(mockResponse);
        server.play();

        // Make request
        def up = new UploadJob(server.getUrl("/upload").toString(), "foo_key", mockedTransferStorage)

        def result = up.execute(context)

        // Validate request
        def factory = new JacksonFactory()
        def expectedDate = factory.toString(dateTime)

        def request = server.takeRequest()
        assertEquals("POST /upload HTTP/1.1", request.getRequestLine())
        assertTrue(request.getHeaders().contains("Content-Type: application/json"))
        assertTrue(request.getHeaders().contains("X-Api-Key: foo_key"))
        assertEquals("[{\"counterA\":1,\"counterB\":2,\"dateTime\":" + expectedDate + ",\"deviceId\":\"C1\"},{\"dateTime\":" + expectedDate + ",\"deviceId\":\"T1\",\"temperature\":12.5}]", new String(request.getBody()))

        verify(mockedTransferStorage).uploadedSuccessfully();
    }

    void testExecuteFail() {
        def context = mock(JobExecutionContext.class)
        mockedTransferStorage = mock(ReadingsTransferStorage.class)
        def dateTime = new DateTime(new Date(), TimeZone.getDefault())
        def readings = makeReadings(dateTime)
        when(mockedTransferStorage.collectReadingsToSend()).thenReturn(readings)

        // Enqueue response
        MockWebServer server = new MockWebServer();
        def mockResponse = new MockResponse()
        mockResponse.addHeader("Content-Type: application/json")
        mockResponse.setBody("{\"resultCode\":\"Error\"}")
        server.enqueue(mockResponse);
        server.play();

        // Make request
        def up = new UploadJob(server.getUrl("/upload").toString(), "foo_key", mockedTransferStorage)

        def result = up.execute(context)

        // Validate request
        def factory = new JacksonFactory()
        def expectedDate = factory.toString(dateTime)

        def request = server.takeRequest()
        assertEquals("POST /upload HTTP/1.1", request.getRequestLine())
        assertTrue(request.getHeaders().contains("Content-Type: application/json"))
        assertTrue(request.getHeaders().contains("X-Api-Key: foo_key"))
        assertEquals("[{\"counterA\":1,\"counterB\":2,\"dateTime\":" + expectedDate + ",\"deviceId\":\"C1\"},{\"dateTime\":" + expectedDate + ",\"deviceId\":\"T1\",\"temperature\":12.5}]", new String(request.getBody()))

        verify(mockedTransferStorage, never()).uploadedSuccessfully();
    }

    void testUpload() {
        mockedTransferStorage = mock(ReadingsTransferStorage.class)

        // Enqueue response
        MockWebServer server = new MockWebServer();
        def mockResponse = new MockResponse()
        mockResponse.addHeader("Content-Type: application/json")
        mockResponse.setBody("{\"resultCode\":\"OK\"}")
        server.enqueue(mockResponse);
        server.play();

        // Make request
        def up = new UploadJob(server.getUrl("/upload").toString(), "foo_key", mockedTransferStorage)

        def dateTime = new DateTime(new Date(), TimeZone.getDefault())
        def readings = makeReadings(dateTime)
        
        def result = up.upload(readings)
        assertEquals("OK", result.getResultCode())

        // Validate request
        def factory = new JacksonFactory()
        def expectedDate = factory.toString(dateTime)

        def request = server.takeRequest()
        assertEquals("POST /upload HTTP/1.1", request.getRequestLine())
        assertTrue(request.getHeaders().contains("Content-Type: application/json"))
        assertTrue(request.getHeaders().contains("X-Api-Key: foo_key"))
        assertEquals("[{\"counterA\":1,\"counterB\":2,\"dateTime\":" + expectedDate + ",\"deviceId\":\"C1\"},{\"dateTime\":" + expectedDate + ",\"deviceId\":\"T1\",\"temperature\":12.5}]", new String(request.getBody()))
    }

    private ArrayList<Reading> makeReadings(DateTime dateTime) {
        def counterReading = new CounterReading("C1", 1, 2)
        counterReading.setDateTime(dateTime)
        def temperatureReading = new TemperatureReading("T1", new BigDecimal(12.5))
        temperatureReading.setDateTime(dateTime)
        def readings = [counterReading,
                temperatureReading]
        return readings
    }

}
