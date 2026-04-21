package com.smartcampus;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger logger = Logger.getLogger(ApiLoggingFilter.class.getName());

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String method = requestContext.getMethod();
    String uri = requestContext.getUriInfo().getRequestUri().toString();
    logger.info(">>> INCOMING REQUEST: " + method + " " + uri);
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
  throws IOException {
    int status = responseContext.getStatus();
    logger.info("<<< OUTGOING RESPONSE: Status Code " + status);
  }
}