package src;

public abstract class abstractVehicleInventoryManager {
    // Integer properties of the vehicle object
    int wheels;
    int humanCapacity;
    int carryingCapacity;

    // Abstract functions to get properties of each vehicle
    abstract double priceOfVehicle();

    abstract String getType();

    abstract int getWheels();
}