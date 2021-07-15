package src.vehicles;

public class Truck extends abstractVehicleInventoryManager {

    public Truck(int numWheels) {
        wheels = numWheels;

        // If the Truck has 4 wheels, set according parameters
        if (wheels == 4) {
            carryingCapacity = 2500;
            humanCapacity = 5;
        }

        // If the Truck has 8 wheels, set according parameters
        if (wheels == 8) {
            carryingCapacity = 4000;
            humanCapacity = 3;
        }

        // If the Truck has 16 wheels, set according parameters
        if (wheels == 16) {
            carryingCapacity = 7000;
            humanCapacity = 3;
        }
    }

    @Override
    // Set return statements for abstract functions
    double priceOfVehicle() {
        return (carryingCapacity / 100.00) + wheels; // Return price
    }

    @Override
    String getType() {
        return "Truck"; // Return type as "Truck"
    }

    @Override
    int getWheels() {
        return wheels; // Return number of wheels
    }
}