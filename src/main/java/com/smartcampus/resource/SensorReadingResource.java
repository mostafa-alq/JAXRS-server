package com.smartcampus.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.smartcampus.DataStore;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

public class SensorReadingResource {
    
    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        // fetch the history for the sensor
        List<SensorReading> history = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(history).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor parentSensor = DataStore.sensors.get(sensorId);
        
        // check to make sure the sensor actually exists first
        if (parentSensor == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Sensor not found\"}").build();
        }

        // check for maintenance status
        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
          throw new SensorUnavailableException("Sensor is in maintenance mode and cannot accept new readings.");
        }

        // if no ID, generate random ID
        if (reading.getId() == null || reading.getId().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        // if no timestamp, give it the current time as the timestamp
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        // save the reading to the sensor history database
        DataStore.readings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.readings.get(sensorId).add(reading);

        // update the parent sensor's current value
        parentSensor.setCurrentValue(reading.getValue());
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}