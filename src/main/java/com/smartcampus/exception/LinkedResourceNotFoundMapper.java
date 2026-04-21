package com.smartcampus.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
  @Override
  public Response toResponse(LinkedResourceNotFoundException exception) {
    return Response.status(422).entity("{\"error\":\"" + exception.getMessage() + "\"}").type("application/json").build();
  }
}