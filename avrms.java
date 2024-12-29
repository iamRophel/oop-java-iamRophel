package VehicleRentalSystem;

import java.util.*;

abstract class Vehicle {
    private final String vehicleId, model;
    private final double baseRentalRate;
    private boolean isAvailable = true;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        if (vehicleId == null || model == null || baseRentalRate <= 0) throw new IllegalArgumentException("Invalid vehicle details");
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
    }

    public String getVehicleId() { return vehicleId; }
    public String getModel() { return model; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailability(boolean available) { isAvailable = available; }
    public abstract double calculateRentalCost(int days);
}

class Car extends Vehicle {
    public Car(String vehicleId, String model, double baseRentalRate) { super(vehicleId, model, baseRentalRate); }
    @Override public double calculateRentalCost(int days) { return days * 50.0; }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String vehicleId, String model, double baseRentalRate) { super(vehicleId, model, baseRentalRate); }
    @Override public double calculateRentalCost(int days) { return days * 30.0; }
}

class Truck extends Vehicle {
    public Truck(String vehicleId, String model, double baseRentalRate) { super(vehicleId, model, baseRentalRate); }
    @Override public double calculateRentalCost(int days) { return days * 100.0; }
}

class Customer {
    private final String name;
    private final List<Vehicle> rentalHistory = new ArrayList<>();

    public Customer(String name) { this.name = name; }
    public void rentVehicle(Vehicle vehicle, int days) {
        if (!vehicle.isAvailable()) throw new IllegalStateException("Vehicle not available");
        rentalHistory.add(vehicle);
        vehicle.setAvailability(false);
        System.out.println(name + " rented " + vehicle.getModel() + " for " + days + " days. Cost: " + vehicle.calculateRentalCost(days));
    }
    public void returnVehicle(Vehicle vehicle) {
        if (!rentalHistory.remove(vehicle)) throw new IllegalStateException("Vehicle not rented by this customer");
        vehicle.setAvailability(true);
        System.out.println(name + " returned " + vehicle.getModel());
    }
}

class RentalAgency {
    private final List<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) { fleet.add(vehicle); }
    public void listAvailableVehicles() {
        fleet.stream().filter(Vehicle::isAvailable).forEach(vehicle ->
            System.out.println(vehicle.getModel() + " (ID: " + vehicle.getVehicleId() + ") is available for rental."));
    }
    public Vehicle findVehicleById(String vehicleId) {
        return fleet.stream().filter(v -> v.getVehicleId().equals(vehicleId)).findFirst().orElse(null);
    }
}

public class App {
    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();
        agency.addVehicle(new Car("C001", "Sedan", 50));
        agency.addVehicle(new Motorcycle("M001", "Sport Bike", 30));
        agency.addVehicle(new Truck("T001", "Freight Truck", 100));

        Customer customer = new Customer("Alice");

        System.out.println("Available Vehicles:");
        agency.listAvailableVehicles();

        Vehicle vehicle = agency.findVehicleById("C001");
        if (vehicle != null) customer.rentVehicle(vehicle, 5);

        System.out.println("\nAvailable Vehicles After Renting:");
        agency.listAvailableVehicles();

        customer.returnVehicle(vehicle);

        System.out.println("\nAvailable Vehicles After Returning:");
        agency.listAvailableVehicles();
    }
}
