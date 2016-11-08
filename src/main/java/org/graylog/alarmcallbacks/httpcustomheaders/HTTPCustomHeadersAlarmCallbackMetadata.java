package org.graylog.alarmcallbacks.httpcustomheaders;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

public class HTTPCustomHeadersAlarmCallbackMetadata implements PluginMetaData {
    @Override
    public String getUniqueId() {
        return HTTPCustomHeadersAlarmCallback.class.getCanonicalName();
    }

    @Override
    public String getName() {
        return "HTTP Custom Headers Alarmcallback Plugin";
    }

    @Override
    public String getAuthor() {
        return "Alex&&er Campos I.";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/alcampos/graylog-plugin-http-custom-headers");
    }

    @Override
    public Version getVersion() {
        return new Version(0, 0, 1);
    }

    @Override
    public String getDescription() {
        return "Alarm callback plugin that send an HTTP/HTTPS request with custom headers on stream alert.";
    }

    @Override
    public Version getRequiredVersion() {
    	return new Version(2, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
