package org.routing.web.configuration;

import io.smallrye.config.ConfigMapping;

import java.nio.file.Path;

@ConfigMapping(prefix = "network")
public interface NetworkConfiguration {

    Path name();
}
