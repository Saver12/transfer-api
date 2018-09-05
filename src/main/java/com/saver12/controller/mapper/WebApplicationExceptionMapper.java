package com.saver12.controller.mapper;

import com.saver12.model.AppResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.saver12.controller.mapper.AppExceptionMapper.ERROR;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        return Response
                .status(exception.getResponse().getStatus())
                .entity(new AppResponse(exception.getResponse().getStatus(), ERROR, exception.getMessage()))
                .build();
    }
}
