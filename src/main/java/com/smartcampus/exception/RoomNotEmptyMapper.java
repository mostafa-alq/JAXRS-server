package com.smartcampus.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {
  @Override
  public Response toResponse(RoomNotEmptyException exception) {
    return Response.status(Response.Status.CONFLICT).entity("{\"error\":\"" + exception.getMessage() + "\"}").type("application/json").build();
  }
}