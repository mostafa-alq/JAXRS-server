package com.smartcampus;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
  // room database
  public static Map<String, Room> rooms = new HashMap<>();
  
  // sensor database
  public static Map<String, Sensor> sensors = new HashMap<>();
}