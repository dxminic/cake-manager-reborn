package com.dom.config;

// This pulls in the cake.url property from application.yml and uses Micronaut's configuration system.

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

@ConfigurationProperties("cake")
@Requires(property = "cake")
public class CakeConfig {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
