package com.smartcampus.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.smartcampus.DataStore;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

  @POST
  public Response createSensor(Sensor sensor) {

    String roomId = sensor.getRoomId();

    // make sure that the roomId specified in the request actually exists
    if (roomId == null || !DataStore.rooms.containsKey(roomId)) {
      throw new LinkedResourceNotFoundException("Cannot link sensor: Room ID " + roomId + " does not exist.");
    }

    // if it passes the integrity check, generate a random ID if it doesn't have one
    if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
      sensor.setId(UUID.randomUUID().toString());
    }

    // save the sensor to my database
    DataStore.sensors.put(sensor.getId(), sensor);

    // link the new sensor back to the rooms sensorIds list
    Room parentRoom = DataStore.rooms.get(roomId);
    parentRoom.getSensorIds().add(sensor.getId());

    // return created response
    return Response.status(Response.Status.CREATED).entity(sensor).build();
  }

  @GET
  public Response getSensors(@QueryParam("type") String type) {

    // if no type is provided, just return all sensors
    if (type == null || type.trim().isEmpty()) {
      return Response.ok(DataStore.sensors.values()).build();
    }

    // if a type is provided, filter the list
    List<Sensor> filteredSensors = new ArrayList<>();
    for (Sensor sensor : DataStore.sensors.values()) {
      if (type.equalsIgnoreCase(sensor.getType())) {
        filteredSensors.add(sensor);
      }
    }

    return Response.ok(filteredSensors).build();
  }

  @Path("/{sensorId}/readings")
  public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
    // instantiate the sub-resource from JAX and pass the ID from the URL into it
    return new SensorReadingResource(sensorId);
  }
}