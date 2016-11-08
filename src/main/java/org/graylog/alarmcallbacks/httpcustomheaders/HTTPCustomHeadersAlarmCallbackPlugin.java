package org.graylog.alarmcallbacks.httpcustomheaders;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

public class HTTPCustomHeadersAlarmCallbackPlugin implements Plugin {
    @Override
    public Collection<PluginModule> modules() {
        return Collections.<PluginModule>singleton(new HTTPCustomHeadersAlarmCallbackModule());
    }

    @Override
    public PluginMetaData metadata() {
        return new HTTPCustomHeadersAlarmCallbackMetadata();
    }
}
