package src.vehicles;

public class Sedan extends abstractVehicleInventoryManager {

    // Set the parameters of the Sedan
    public Sedan(int capacity) {
        wheels = 4;
        humanCapacity = 5;
        carryingCapacity = capacity;
    }

    @Override
    // Set return statements for abstract functions
    double priceOfVehicle() {
        return (carryingCapacity / 100.00) + wheels; // Return price
    }

    @Override
    String getType() {
        return "Sedan"; // Return type as "Truck"
    }

    @Override
    int getWheels() {
        return wheels; // Return number of wheels
    }
}