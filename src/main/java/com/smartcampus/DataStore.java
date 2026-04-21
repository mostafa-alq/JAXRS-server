package com.smartcampus;

import java.util.HashMap;
import java.util.Map;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

public class DataStore {
  // room database
  public static Map<String, Room> rooms = new HashMap<>();
  
  // sensor database
  public static Map<String, Sensor> sensors = new HashMap<>();

  // database to store history of reading per sensor id
  public static java.util.Map<String, java.util.List<SensorReading>> readings = new java.util.HashMap<>();
}