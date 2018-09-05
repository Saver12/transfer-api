package com.saver12.controller;

import com.alibaba.fastjson.JSON;
import com.saver12.config.ApplicationConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.net.URI;

public class AbstractControllerTest extends JerseyTest {

    private static final String BASE_URI = "http://localhost:8080/api";

    @Override
    protected Application configure() {
        return new ApplicationConfig();
    }

    @Override
    public URI getBaseUri() {
        return URI.create(BASE_URI);
    }

    // had to use fastxml json library because of java.util.Currency private constructors
    <T> T parseJSON(Response output, Class<T> clazz) {
        return JSON.parseObject(output.readEntity(String.class), clazz);
    }
}
