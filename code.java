/*
Design a parking lot using object-oriented principles

Goals:
- Your solution should be in Java - if you would like to use another language, please let the interviewer know.
- Boilerplate is provided. Feel free to change the code as you see fit

Assumptions:
- The parking lot can hold motorcycles, cars and vans
- The parking lot has motorcycle spots, car spots and large spots
- A motorcycle can park in any spot
- A car can park in a single compact spot, or a regular spot
- A van can park, but it will take up 3 regular spots
- These are just a few assumptions. Feel free to ask your interviewer about more assumptions as needed

Here are a few methods that you should be able to run:
- Tell us how many spots are remaining
- Tell us how many total spots are in the parking lot
- Tell us when the parking lot is full
- Tell us when the parking lot is empty
- Tell us when certain spots are full e.g. when all motorcycle spots are taken
- Tell us how many spots vans are taking up

Hey candidate! Welcome to your interview. I'll start off by giving you a Solution class. To run the code at any time, please hit the run button located in the top left corner.
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Solution {
  public static void main(String[] args) {
    ArrayList<String> strings = new ArrayList<String>();
    strings.add("Hello, World!");
    strings.add("Please put code below");
    for (String string : strings) {
      System.out.println(string);
    }

    List<Spot> spots = Arrays.asList(new Spot(1, SpotType.MEDIUM));
    Parking parking = new Parking(spots);
    Vehicle car = new Vehicle(VehicleType.CAR, spots.stream().map(s -> s.getType()).collect(Collectors.toList()));
    parking.parkVehicle(car);

    System.out.println(String.format("Parking full = %s", parking.isFull()));
    System.out.println(String.format("Parking empty = %s", parking.isEmpty()));
  }
}


class Parking {
  private List<Spot> spots;

  public Parking(List<Spot> spots) {
    this.spots = spots;
  }

  public Integer getRemainingSpot() {
    return spots.stream()
        .filter(spot -> spot.isEmpty())
        .collect(Collectors.toList())
        .size();
  }

  public Integer getTotalSpots() {
    return spots.size();
  }

  public boolean isFull() {
    return !isEmpty();
  }

  public boolean isEmpty() {
    return spots.stream().allMatch(spot -> spot.isEmpty());
  }

  public void parkVehicle(Vehicle vehicle) {
    spots.stream()
      .filter(spot -> spot.isEmpty())
      .filter(spot -> vehicle.allowParking(spot))
      .findFirst()
      .ifPresent(spot -> spot.setVehicle(vehicle));

    assessState();
  }

  public void assessState() {
    Stream.of(SpotType.values())
    //.map(spotType -> String.format("%s is full = %s", spotType.name(), spotsFullByType(spotType)))
    .filter(spotType -> spotsFullByType(spotType))
    .forEach(spotType -> System.out.println(String.format("####### Spots %s are full!!!", spotType.name())));
    //.forEach(message -> System.out.println(message));
  }

  private boolean spotsFullByType(SpotType spotType) {
    List<Spot> spotsType =  spots.stream()
      .filter(spot -> spotType.equals(spot.getType()))
      .collect(Collectors.toList());

    return !spotsType.isEmpty() && spotsType.stream().allMatch(spot -> !spot.isEmpty());
  }
}

class Spot {
  private Integer id;
  private SpotType type;
  private boolean empty;
  private Vehicle vehicle;

  public Spot(Integer id, SpotType type) {
    this.id = id;
    this.type = type;
    this.empty = true;
  }

  public Integer getId() {
    return this.id;
  }

  public SpotType getType() {
    return this.type;
  }

  public boolean isEmpty() {
    return this.empty;
  }

  public void setVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
    this.empty = false;
  }
}

enum SpotType {
  SMALL,
  MEDIUM,
  LARGE;
}

enum VehicleType {
  CAR,
  MOTORCYCLES,
  VAN;
}

class Vehicle {
  
  private VehicleType type;
  private List<SpotType> spotTypes;

  public Vehicle(VehicleType type, List<SpotType> spotTypes) {
    this.type = type;
    this.spotTypes = spotTypes;
  }

  public VehicleType getType() {
    return type;
  }

  public boolean allowParking(Spot spot) {
    return spotTypes.contains(spot.getType());
  }
}
