package org.graylog.alarmcallbacks.httpcustomheaders;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import org.graylog2.alarmcallbacks.HTTPAlarmCallback;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.shared.bindings.providers.ObjectMapperProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import okhttp3.OkHttpClient;


public class HTTPCustomHeadersAlarmCallbackTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
	
    private OkHttpClient httpClient;
    private ObjectMapper objectMapper;
    private HTTPAlarmCallback alarmCallback;

    private MockWebServer server;
    
    @Before
    public void setUp() {
        httpClient = new OkHttpClient();
        objectMapper = new ObjectMapperProvider().get();
        alarmCallback = new HTTPAlarmCallback(httpClient, objectMapper);
        
        server = new MockWebServer();
    }
    
    @After
    public void shutDown() throws IOException {
        if (server != null) {
            server.shutdown();
        }
    }
    
    @Test
    public void initializeStoresConfiguration() throws Exception {
        final Map<String, Object> configMap = ImmutableMap.of("url", "http://example.com/");
        final Configuration configuration = new Configuration(configMap);
        alarmCallback.initialize(configuration);

        assertThat(alarmCallback.getAttributes()).isEqualTo(configMap);
    }
}
