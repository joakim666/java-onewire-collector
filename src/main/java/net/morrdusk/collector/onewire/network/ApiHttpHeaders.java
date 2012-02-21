package net.morrdusk.collector.onewire.network;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Key;

public class ApiHttpHeaders extends HttpHeaders {

    public ApiHttpHeaders(String apiKey) {
        this.apiKey = apiKey;
    }

    @Key("X-Api-Key")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
