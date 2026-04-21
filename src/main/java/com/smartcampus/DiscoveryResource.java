package com.smartcampus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo() {
        Map<String, Object> discoveryData = new HashMap<>();
        
        discoveryData.put("version", "v1.0");
        discoveryData.put("description", "Smart Campus Sensor & Room Management API");
        
        Map<String, String> contact = new HashMap<>();
        contact.put("name", "Mostafa Alqadi");
        contact.put("email", "w2116049@westminster.ac.uk");
        discoveryData.put("contact", contact);
        
        // map of primary resource collections
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        discoveryData.put("_links", links);

        return Response.ok(discoveryData).build();
    }
}