package net.morrdusk.collector.onewire.scheduling;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.morrdusk.collector.onewire.db.ReadingsTransferStorage;
import net.morrdusk.collector.onewire.domain.Reading;
import net.morrdusk.collector.onewire.network.ApiHttpHeaders;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UploadJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(UploadJob.class);

    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
    private String url;
    private String apiKey;
    private ReadingsTransferStorage transferStorage;

    @Inject
    public UploadJob(@Named("URL") String url, @Named("API_KEY") String apiKey, ReadingsTransferStorage transferStorage) {
        this.url = url;
        this.transferStorage = transferStorage;
        this.apiKey = apiKey;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        UploadResult result = null;
        try {
            result = upload(transferStorage.collectReadingsToSend());
        } catch (IOException e) {
            LOG.info("Failed to upload data", e);
        }

        if (result != null && result.isSuccessful()) {
            transferStorage.uploadedSuccessfully();
        }
    }

    protected UploadResult upload(List<Reading> readings) throws IOException {
        final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.addParser(new JsonHttpParser(JSON_FACTORY));
                request.setHeaders(new ApiHttpHeaders(apiKey));
            }
        });

        final UploadResult result;
        final HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(this.url),
                new JsonHttpContent(new JacksonFactory(), readings));
        result = request.execute().parseAs(UploadResult.class);
        return result;
    }

    public static class UploadResult extends GenericJson {
        @Key
        private String resultCode;

        public String getResultCode() {
            return resultCode;
        }

        public boolean isSuccessful() {
            return "OK".equals(resultCode);
        }
    }

}
