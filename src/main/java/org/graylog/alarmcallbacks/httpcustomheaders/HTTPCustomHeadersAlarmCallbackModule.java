package org.graylog.alarmcallbacks.httpcustomheaders;

import org.graylog2.plugin.PluginModule;

public class HTTPCustomHeadersAlarmCallbackModule extends PluginModule {
    @Override
    protected void configure() {
        addAlarmCallback(HTTPCustomHeadersAlarmCallback.class);
    }
}
