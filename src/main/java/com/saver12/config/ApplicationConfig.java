package com.saver12.config;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {

    private static final String PACKAGES = "com.saver12.controller";

    public ApplicationConfig() {
    	register(new ApplicationBinder());
        packages(true, PACKAGES);
    }
}
