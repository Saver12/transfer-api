package com.saver12.controller.mapper;

import com.owlike.genson.stream.JsonStreamException;
import com.saver12.model.AppResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.saver12.controller.mapper.AppExceptionMapper.ERROR;

@Provider
public class JsonStreamExceptionMapper implements ExceptionMapper<JsonStreamException> {

    @Override
    public Response toResponse(JsonStreamException exception) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(new AppResponse(Status.BAD_REQUEST.getStatusCode(), ERROR, exception.getMessage()))
                .build();
    }
}
