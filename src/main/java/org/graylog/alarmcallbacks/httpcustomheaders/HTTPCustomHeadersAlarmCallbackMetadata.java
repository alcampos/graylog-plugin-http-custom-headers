package org.graylog.alarmcallbacks.httpcustomheaders;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

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
        return "Graylog, Inc.";
    }

    @Override
    public URI getURL() {
        return URI.create("https://www.graylog.org");
    }

    @Override
    public Version getVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public String getDescription() {
        return "Alarm callback plugin that send an HTTP/HTTPS request with custom headers on stream alert.";
    }

    @Override
    public Version getRequiredVersion() {
    	return new Version(2, 1, 1);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
