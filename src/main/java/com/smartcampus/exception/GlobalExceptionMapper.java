package com.smartcampus.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
  @Override
  public Response toResponse(Throwable exception) {
    // logging error in console for debugging
    exception.printStackTrace();

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"An unexpected internal server error occurred.\"}").type("application/json").build();
  }
}