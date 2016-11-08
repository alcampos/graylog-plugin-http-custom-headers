package org.graylog.alarmcallbacks.httpcustomheaders;

import static com.google.common.base.Strings.isNullOrEmpty;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.streams.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HTTPCustomHeadersAlarmCallback implements AlarmCallback{
    private static final String CK_URL = "url";
    private static final String CK_HEADER_NAME = "header name";
    private static final String CK_HEADER_VALUE = "header value";
    private static final MediaType CONTENT_TYPE = MediaType.parse(APPLICATION_JSON);

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private Configuration configuration;

    @Inject
    public HTTPCustomHeadersAlarmCallback(final OkHttpClient httpClient, final ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }


    @Override
    public void initialize(Configuration config) throws AlarmCallbackConfigurationException{
        this.configuration = config;
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult result) throws AlarmCallbackException {
    	final Map<String, Object> event = Maps.newHashMap();
        event.put("stream", stream);
        event.put("check_result", result);

        final byte[] body;
        try {
            body = objectMapper.writeValueAsBytes(event);
        } catch (JsonProcessingException e) {
            throw new AlarmCallbackException("Unable to serialize alarm", e);
        }

        final String url = configuration.getString(CK_URL);
        final String headerName = configuration.getString(CK_HEADER_NAME);
        final String headerValue = configuration.getString(CK_HEADER_VALUE);
        final HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new AlarmCallbackException("Malformed URL: " + url);
        }

        final Request request = new Request.Builder()
                .url(httpUrl)
                .post(RequestBody.create(CONTENT_TYPE, body))
                .addHeader(headerName, headerValue)
                .build();
        try (final Response r = httpClient.newCall(request).execute()) {
            if (!r.isSuccessful()) {
                throw new AlarmCallbackException("Expected successful HTTP response [2xx] but got [" + r.code() + "].");
            }
        } catch (IOException e) {
            throw new AlarmCallbackException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getAttributes() {
        return configuration.getSource();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
        final String url = configuration.getString(CK_URL);
        if (isNullOrEmpty(url)) {
            throw new ConfigurationException("URL parameter is missing!");
        }

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new ConfigurationException("Malformed URL", e);
        }
        
        final String headerName = configuration.getString(CK_HEADER_NAME);
        if (isNullOrEmpty(headerName)) {
            throw new ConfigurationException("Header name is missing!");
        }
        
        final String headerValue = configuration.getString(CK_HEADER_VALUE);
        if (isNullOrEmpty(headerValue)) {
            throw new ConfigurationException("Header value is missing!");
        }
    }

    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        final ConfigurationRequest configurationRequest = new ConfigurationRequest();
        configurationRequest.addField(new TextField(CK_URL,
                "URL",
                "https://example.org/alerts",
                "The URL to POST to when an alert is triggered",
                ConfigurationField.Optional.NOT_OPTIONAL));
        configurationRequest.addField(new TextField(CK_HEADER_NAME,
                "Header Name",
                "X-Custom-Header",
                "The custom header name",
                ConfigurationField.Optional.NOT_OPTIONAL));
        configurationRequest.addField(new TextField(CK_HEADER_VALUE,
                "Header Value",
                "value",
                "The value of the header value",
                ConfigurationField.Optional.NOT_OPTIONAL));
        return configurationRequest;
    }

    @Override
    public String getName() {
        return "HTTP Custom Headers alarm callback";
    }
}
