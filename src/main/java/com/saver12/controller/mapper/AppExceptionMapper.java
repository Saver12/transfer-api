package com.saver12.controller.mapper;

import com.saver12.exception.AppException;
import com.saver12.model.AppResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    public static final String ERROR = "error";

    public Response toResponse(AppException exception) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(new AppResponse(Status.BAD_REQUEST.getStatusCode(), ERROR, exception.getMessage()))
                .build();
    }
}
