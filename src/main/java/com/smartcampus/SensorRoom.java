package com.smartcampus;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoom {

    @GET
    public Response getAllRooms() {
      return Response.ok(DataStore.rooms.values()).build();
    }

    @POST
    public Response createRoom(Room room) {
        // if JSON has no ID, generate random ID
        if (room.getId() == null || room.getId().trim().isEmpty()) {
          room.setId(UUID.randomUUID().toString());
        }
        
        DataStore.rooms.put(room.getId(), room);
        
        // if successful, return created response
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        
        // return not found if room doesn't exist in my array
        if (room == null) {
          return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Room not found\"}").build();
        }
        
        // return OK if room exists
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        
        // make sure the room actually exists
        if (room == null) {
          return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Room not found\"}").build();
        }
        
        // check if sensors are assigned; if assigned, cannot delete.
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
          // return conflict response if sensors are found
          return Response.status(Response.Status.CONFLICT).entity("{\"error\":\"Room cannot be deleted: Active sensors are still assigned to this room.\"}").build();
        }
        
        DataStore.rooms.remove(roomId);
        
        // return no content response
        return Response.noContent().build(); 
  }
}