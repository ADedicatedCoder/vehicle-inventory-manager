package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * @author Mahin Kukreja Vehicle Inventory Manager is a simple program to help
 *         manage the stock of vehicles of, for example, a company that rents
 *         cars to customers. It has the option to give away a vehicle for rent,
 *         check-in a rented vehicle, show current vehicles available to rent,
 *         export and import customer information and includes a page for Help
 *         and Support.
 */

public class VehicleInventoryManager extends JFrame implements ActionListener {
    Container contentPanel; // Declare main container to hold JPanel components
    JPanel userInfo, userIDPanel; // JPanel components, for the main forms of the program (rental and check-in)
    JLabel firstNameLabel, lastNameLabel, lengthOfRentLabel, userCodeLabel, totalPriceLabel, typeLabel; // Different
                                                                                                        // labels for
                                                                                                        // side text
    JButton submitButton, checkInPageButton; // The 2 main buttons in the program
    JTextField firstNameField, lastNameField, lengthOfRentField, userCodeFieldOut, userCodeFieldIn, firstNameFieldOut,
            lastNameFieldOut, lengthOfRentFieldOut, typeField, totalPriceFieldRentalPage, totalPriceFieldCheckInPage; // All
                                                                                                                      // the
                                                                                                                      // JTextfields
                                                                                                                      // for
                                                                                                                      // input
                                                                                                                      // and
                                                                                                                      // output
    JMenuItem vehicleTruck, vehicleSedan, checkInVehicle, display, helpInstructions; // Menu items to appear on the menu
                                                                                     // bar
    String vehicleEvent = ""; // Saves which vehicle was selected
    int numSedans, numTrucks; // Tracks the number of vehicles for each type

    // Arraylists for customer information
    static ArrayList<String> firstName = new ArrayList<>();
    static ArrayList<String> lastName = new ArrayList<>();
    static ArrayList<Long> userCodes = new ArrayList<>();
    static ArrayList<Integer> lengthOfRent = new ArrayList<>();
    static ArrayList<Truck> truckArray = new ArrayList<>();
    static ArrayList<Sedan> sedanArray = new ArrayList<>();
    static ArrayList<String> vehicleChoice = new ArrayList<>();

    static int customers = 0;// Counter variable
    static int sedanIndex, truckIndex; // Integers to track different vehicles for customers

    // Queues to store vehicle object
    static Queue<Sedan> sedanQueue;
    static Stack<Truck> truckStack;

    /*
     * fillVehicleStock() is meant to fill the stock of vehicles of the rental
     * company. It takes no input and pushes Objects into the queues and stacks
     */
    public static void fillVehicleStock() {
        // Initialize Stack and Queue
        sedanQueue = new LinkedList<>();
        truckStack = new Stack<>();

        // Push vehicle type Sedan to the queue
        sedanQueue.add(new Sedan(950));
        sedanQueue.add(new Sedan(850));
        sedanQueue.add(new Sedan(750));
        sedanQueue.add(new Sedan(650));

        // Push vehicle type Truck to the Stack
        truckStack.push(new Truck(4));
        truckStack.push(new Truck(4));
        truckStack.push(new Truck(8));
        truckStack.push(new Truck(16));
    }

    /*
     * getVehicles() is a function that is meant to convert the items in the Stacks
     * and Queues into 1 comprehensive 2D array so that it can be used in the
     * JTable. It takes no direct input but uses the Stacks and Queues and outputs a
     * 2D array with vehicle information such as Type, Price and Number of Wheels
     */
    public String[][] getVehicles() {
        String[][] vehicles = new String[150][3]; // 2D array to be returned
        Sedan[] sedanType = new Sedan[sedanQueue.size()]; // Array of type Sedan
        Truck[] truckType = new Truck[truckStack.size()]; // Array of type Truck

        // Save the size of the arrays
        numSedans = sedanType.length;
        numTrucks = truckType.length;

        // Declare and initialize different counters
        int k = 0;
        int x = 0;

        // Append each Sedan to the Sedan array by removing the elements
        while (sedanQueue.peek() != null) {
            sedanType[k] = sedanQueue.remove();
            k++;
        }

        // Add the elements of the Queue back once the array has all the Sedans
        for (k = 0; k < sedanType.length; k++) {
            sedanQueue.add(sedanType[k]);
        }

        k = 0; // Reset counter

        // Append each Truck to the Truck array by removing the elements
        while (!truckStack.empty()) {
            truckType[k] = truckStack.pop();
            k++;
        }

        // Add the elements of the Stack back once the array has all the Trucks
        for (k = truckType.length - 1; k >= 0; k--) {
            truckStack.push(truckType[k]);
        }

        // Get the parameters of each Sedan and add it to a different column of the 2D
        // Array
        for (k = 0; k < numSedans; k++) {
            vehicles[k][0] = sedanType[k].getType();
            vehicles[k][1] = String.valueOf(sedanType[k].priceOfVehicle());
            vehicles[k][2] = String.valueOf(sedanType[k].getWheels());
        }

        // Get the parameters of each Truck and add it to a different column of the 2D
        // Array
        for (k = numSedans; k < (numTrucks + numSedans); k++) {
            if (x < numTrucks) {
                vehicles[k][0] = truckType[x].getType();
                vehicles[k][1] = String.valueOf(truckType[x].priceOfVehicle());
                vehicles[k][2] = String.valueOf(truckType[x].getWheels());
                x++;
            }
        }
        return vehicles; // Return the 2D Array
    }

    public VehicleInventoryManager() {
        JMenuBar menuBar = new JMenuBar(); // Declare and initialize the menu bar to hold the different menu items
        setJMenuBar(menuBar); // Set the menu bar in place

        // Declare, initialize and add the renting drop down menu to the menu bar
        JMenu menu1 = new JMenu("Rent Vehicle");
        menuBar.add(menu1);

        // Declare, initialize and add the check-in drop down menu to the menu bar
        JMenu menu2 = new JMenu("Check-In Vehicle");
        menuBar.add(menu2);

        // Declare, initialize and add the display vehicles drop down menu to the menu
        // bar
        JMenu menu3 = new JMenu("Display");
        menuBar.add(menu3);

        // Declare, initialize and add the renting drop down menu to the menu bar
        JMenu menu4 = new JMenu("Help");
        menuBar.add(menu4);

        vehicleTruck = new JMenuItem("Truck"); // Initialize the JMenuItem to rent a Truck
        vehicleTruck.addActionListener(this); // Add an ActionListener to the menu item

        vehicleSedan = new JMenuItem("Sedan"); // Initialize the JMenuItem to rent a Sedan
        vehicleSedan.addActionListener(this); // Add an ActionListener to the menu item

        checkInVehicle = new JMenuItem("Check-In"); // Initialize the JMenuItem to check-in a vehicle
        checkInVehicle.addActionListener(this); // Add an ActionListener to the menu item

        display = new JMenuItem("Display Vehicles"); // Initialize the JMenuItem to display all rent-able vehicles
        display.addActionListener(this); // Add an ActionListener to the menu item

        helpInstructions = new JMenuItem("Instructions"); // Initialize the JMenuItem to display tutorials of the
                                                          // program
        helpInstructions.addActionListener(this); // Add an ActionListener to the menu item

        // Add the menu items to their respective drop down menus
        menu1.add(vehicleTruck);
        menu1.add(vehicleSedan);
        menu2.add(checkInVehicle);
        menu3.add(display);
        menu4.add(helpInstructions);
    }

    /*
     * checkInPage() generates the page where the user can check-in their vehicle by
     * entering their User ID Code It takes no direct input and outputs the Check-In
     * form
     */
    public void checkInPage() {
        contentPanel = getContentPane();

        // Declare and initialize the JPanels
        JPanel userIDPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel orderInfoPanel = new JPanel();

        contentPanel.removeAll(); // Clear the contentPanel

        userIDPanel.setLayout(new GridLayout(1, 2)); // Set a Grid-like layout for neater output
        userCodeFieldIn = new JTextField(15); // JTextfield to receive user input for User ID Code
        JLabel userIDLabel = new JLabel("Enter User ID"); // Add the JLabel to clarify what the user should input

        // Add JLabel and JTextfield to the JPanel
        userIDPanel.add(userIDLabel);
        userIDPanel.add(userCodeFieldIn);

        contentPanel.add(userIDPanel, BorderLayout.NORTH); // Add the JPanel to the container

        checkInPageButton = new JButton("Submit User ID and Pay"); // Initialize the button to submit the User ID Code
        checkInPageButton.addActionListener(this); // Implement ActionListener to the button
        buttonPanel.add(checkInPageButton, BorderLayout.CENTER); // Add the button to the buttonPanel
        contentPanel.add(buttonPanel); // Add the buttonPanel to the contentPanel

        orderInfoPanel.setLayout(new GridLayout(5, 2)); // Set a Grid-like layout for neater output

        // Declare and initialize JLabels for output JTextFields
        JLabel firstNameLabelOutput = new JLabel("First Name");
        JLabel lastNameLabelOutput = new JLabel("Last Name");
        JLabel durationOfRentLabelOutput = new JLabel("Length of Rent (days)");
        JLabel type = new JLabel("Vehicle Type");
        JLabel finalPrice = new JLabel("Final Price");

        // Initialize output JTextFields
        firstNameFieldOut = new JTextField(15);
        lastNameFieldOut = new JTextField(15);
        lengthOfRentFieldOut = new JTextField(15);
        typeField = new JTextField(15);
        totalPriceFieldCheckInPage = new JTextField(15);

        // Set all JTextFields to be uneditable
        firstNameFieldOut.setEditable(false);
        lastNameFieldOut.setEditable(false);
        lengthOfRentFieldOut.setEditable(false);
        typeField.setEditable(false);
        totalPriceFieldCheckInPage.setEditable(false);

        // Add the Labels and JTextFields to the orderInfoPanel
        orderInfoPanel.add(firstNameLabelOutput);
        orderInfoPanel.add(firstNameFieldOut);
        orderInfoPanel.add(lastNameLabelOutput);
        orderInfoPanel.add(lastNameFieldOut);
        orderInfoPanel.add(durationOfRentLabelOutput);
        orderInfoPanel.add(lengthOfRentFieldOut);
        orderInfoPanel.add(type);
        orderInfoPanel.add(typeField);
        orderInfoPanel.add(finalPrice);
        orderInfoPanel.add(totalPriceFieldCheckInPage);

        // Add the orderInfoPanel to the contentPanel
        contentPanel.add(orderInfoPanel, BorderLayout.SOUTH);

        validate(); // Refresh and update the GUI
    }

    /*
     * getUserInfo() generates the page where the user can rent a vehicle by
     * entering their first name, last name and how long they want to rent the
     * vehicle for It takes no direct input and outputs the Rental form
     */
    public void getUserInfo() {
        contentPanel = getContentPane();
        if (userInfo != null)
            contentPanel.remove(userInfo);

        // Declare and initialize the JPanels
        userInfo = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel bookingInfo = new JPanel();

        contentPanel.removeAll(); // Clear the contentPanel

        // Initialize input JTextFields
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        lengthOfRentField = new JTextField(15);

        userInfo.setLayout(new GridLayout(3, 2)); // Set a Grid-like layout for neater output

        // Declare and initialize JLabels for input JTextFields
        firstNameLabel = new JLabel("First Name");
        lastNameLabel = new JLabel("Last Name");
        lengthOfRentLabel = new JLabel("Length of Rent (days)");

        // Add the Labels, JTextFields to the userInfo JPanel and implement
        // ActionListeners for the JTextFields
        userInfo.add(firstNameLabel);
        userInfo.add(firstNameField);
        firstNameField.addActionListener(this);
        userInfo.add(lastNameLabel);
        userInfo.add(lastNameField);
        lastNameField.addActionListener(this);
        userInfo.add(lengthOfRentLabel);
        userInfo.add(lengthOfRentField);
        lengthOfRentField.addActionListener(this);

        contentPanel.add(userInfo, BorderLayout.NORTH); // Add the userInfo JPanel to the contentPanel

        submitButton = new JButton("Submit Booking"); // Initialize the button to submit the rental form
        submitButton.addActionListener(this); // Implement ActionListener for the button
        buttonPanel.add(submitButton, BorderLayout.CENTER); // Add the button to the buttonPanel
        contentPanel.add(buttonPanel); // Add the buttonPanel to contentPanel

        bookingInfo.setLayout(new GridLayout(3, 2)); // Set a Grid-like layout for neater output

        // JLabel for output JTextFields
        typeLabel = new JLabel("Type");
        totalPriceLabel = new JLabel("Total Price");
        userCodeLabel = new JLabel("User ID Code");

        // Output JTextFields
        typeField = new JTextField(15);
        totalPriceFieldRentalPage = new JTextField(15);
        userCodeFieldOut = new JTextField(15);

        // Make output JTextFields uneditable
        typeField.setEditable(false);
        totalPriceFieldRentalPage.setEditable(false);
        userCodeFieldOut.setEditable(false);

        // Add the JLabels and JTextFields to the bookingInfo Panel
        bookingInfo.add(typeLabel);
        bookingInfo.add(typeField);
        bookingInfo.add(totalPriceLabel);
        bookingInfo.add(totalPriceFieldRentalPage);
        bookingInfo.add(userCodeLabel);
        bookingInfo.add(userCodeFieldOut);

        contentPanel.add(bookingInfo, BorderLayout.SOUTH); // Add the bookingInfo panel to contentPanel

        validate(); // Refresh and update the GUI
    }

    /*
     * saveInformation() saves all customer information in a neat manner in case the
     * user does not check-in the vehicle within the same session as it was rented.
     * The method takes no direct input but does use the elements of the ArrayLists
     * that save customer information and the outputs them in a text file in an
     * orderly manner
     */
    public static void saveInformation() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("information.txt")); // Declare and initialize the PrintWriter

        // Integer for tracking which Sedans and Trucks have been rented out by the
        // customers
        int truckCount = -1;
        int sedanCount = -1;

        // If the number of customers is greater than 0
        if (customers > 0) {
            out.println(customers); // Print the number of customers

            for (int i = 0; i < customers; i++) {
                // Print customer first name, last name, duration of rent and their USer ID Code
                out.println(firstName.get(i));
                out.println(lastName.get(i));
                out.println(lengthOfRent.get(i));
                out.println(userCodes.get(i));

                // Spit the vehicle element with the vehicle type and the index it is located at
                String[] vehicleType = vehicleChoice.get(i).split(",");

                // If the first element is a Truck, then print out the Truck information
                if (vehicleType[0].equals("Truck")) {
                    truckCount++;
                    out.println(truckArray.get(truckCount).getType());
                    out.println(truckArray.get(truckCount).priceOfVehicle());
                    out.println(truckArray.get(truckCount).getWheels());
                }

                // If the first element is a Sedan, then print out the Sedan information
                else if (vehicleType[0].equals("Sedan")) {
                    sedanCount++;
                    out.println(sedanArray.get(sedanCount).getType());
                    out.println(sedanArray.get(sedanCount).priceOfVehicle());
                    out.println(sedanArray.get(sedanCount).getWheels());
                }
            }
        } else {
            out.println("0"); // Print 0 if there are no customers
        }
        out.close(); // Close the file
    }

    /*
     * loadInformation() loads the saved information from the text file back into
     * the program. This method takes no direct input but requires a text file to be
     * able to import the customers into the program.
     */
    public static void loadInformation() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("information.txt")); // Declare and initialize the
                                                                                   // FileReader

        customers = Integer.parseInt(in.readLine()); // Read the first line of the file and store it.

        // For each customer
        for (int i = 0; i < customers; i++) {

            // Read and save the customer information (first name, last name, duration of
            // rent, and User ID Code)
            firstName.add(in.readLine());
            lastName.add(in.readLine());
            lengthOfRent.add(Integer.parseInt(in.readLine()));
            userCodes.add(Long.parseLong(in.readLine()));

            // Save the vehicle information in temporary variables
            String tempType = in.readLine();
            double tempPrice = Double.parseDouble(in.readLine());
            int tempWheels = Integer.parseInt(in.readLine());

            // If the vehicle is a truck, create a Truck object with the parameters from the
            // text file and add the index
            if (tempType.equals("Truck")) {
                Truck truck = new Truck(tempWheels);
                truckArray.add(truck);
                vehicleChoice.add(tempType + "," + truckIndex);
                truckIndex++;
            }
            // Else create a Sedan object with the parameters from the text file and add the
            // index
            else {
                Sedan sedan = new Sedan((int) ((tempPrice - tempWheels) * 100));
                sedanArray.add(sedan);
                vehicleChoice.add(tempType + "," + sedanIndex);
                sedanIndex++;
            }
        }
        in.close(); // Close the file
    }

    /*
     * generateUserCode() is a method that generates a random Long value to serve as
     * the User ID Code
     */
    public void generateUserCode() {
        // Add the randomly generated long to the userCodes array
        userCodes.add(Math
                .abs(Float.valueOf(new Random().nextFloat() * (9999999999999999L - 1000000000000000L)).longValue()));
    }

    /*
     * checkInVehicle() removes the customer information and adds the rented vehicle
     * back into its list. It takes the input of the User ID Code JTextField in the
     * check-in page and outputs order information
     */
    public void checkInVehicle() {
        try {
            boolean found = false; // boolean for whether search was successful or not

            // Does a sequential search on all elements of the userCodes array, comparing
            // the user input to each element
            for (int i = 0; i < customers; i++) {
                // If the element is found
                if (Long.parseLong(userCodeFieldIn.getText()) == userCodes.get(i)) {

                    // Set the uneditable JTextFields with the order information
                    firstNameFieldOut.setText(firstName.get(i));
                    lastNameFieldOut.setText(lastName.get(i));
                    lengthOfRentFieldOut.setText(String.valueOf(lengthOfRent.get(i)));

                    String[] vehicleType = vehicleChoice.get(i).split(","); // Split the vehicle element
                    int index = Integer.parseInt(vehicleType[1]); // Save the index which is the 2nd element of
                                                                  // vehicleType

                    // If the vehicle is a truck
                    if (vehicleType[0].equals("Truck")) {
                        // Set the uneditable JTextFields with the order information
                        typeField.setText(truckArray.get(index).getType());
                        totalPriceFieldCheckInPage
                                .setText("$ " + (truckArray.get(index).priceOfVehicle() * 24) * lengthOfRent.get(i));

                        truckStack.push(truckArray.get(index)); // Push the element back to the Stack
                        truckArray.remove(index); // Remove the object at the index
                        truckIndex--; // Decrement the counter
                    }
                    // Else if the vehicle is a sedan
                    else {
                        // Set the uneditable JTextFields with the order information
                        typeField.setText(sedanArray.get(index).getType());
                        totalPriceFieldCheckInPage
                                .setText("$ " + (sedanArray.get(index).priceOfVehicle() * 24) * lengthOfRent.get(i));

                        sedanQueue.add(sedanArray.get(index)); // Push the element back to the Queue
                        sedanArray.remove(index); // Remove the object at the index
                        sedanIndex--; // Decrement the counter
                    }

                    userCodeFieldIn.setText(""); // Set the input field to be blank

                    // Remove the customer information at the index
                    firstName.remove(i);
                    lastName.remove(i);
                    lengthOfRent.remove(i);
                    vehicleChoice.remove(i);
                    userCodes.remove(i);
                    customers--;

                    // Iterate through the vehicle array and fix the index (in case the order of
                    // check-in is not the same as rental)
                    for (int x = i; x < vehicleChoice.size(); x++) {
                        String[] arrangeVehicles = vehicleChoice.get(x).split(","); // Split the element
                        vehicleChoice.remove(x); // Remove the element
                        vehicleChoice.add(arrangeVehicles[0] + "," + x); // Add it back in with the updated index
                    }

                    // Set boolean to true and break loop
                    found = true;
                    break;
                }
            }
            // If found boolean remains false, inform user that their ID wasn't found
            if (!found) {
                JOptionPane.showMessageDialog(userIDPanel, "No match! Please make sure you have entered it correctly!");
            }
        }

        // Catch NumberFormatException in case the user accidentally puts a string
        // character inside their input
        catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(userIDPanel,
                    "Please make sure you have only entered numbers into the text box!");
        }
    }

    /*
     * appendCustomerInfo() saves customer information for when they rent a vehicle.
     * It takes the input from the input fields of the rental form and updates the
     * arrays to hold customer information
     */
    public void appendCustomerInfo() {
        boolean append = true; // Boolean condition for whether to proceed with saving the information

        // Check for blank JTextFields
        if (firstNameField.getText().equals("") || lastNameField.getText().equals("")
                || lengthOfRentField.getText().equals("")) {
            JOptionPane.showMessageDialog(userInfo, "All fields must be filled appropriately!");
            append = false;
        }
        // Try to add the length of rent field
        try {
            lengthOfRent.add(Integer.parseInt(lengthOfRentField.getText()));

            // Check if user has entered a valid number of days to rent
            if (Integer.parseInt(lengthOfRentField.getText()) == 0) {
                JOptionPane.showMessageDialog(userInfo, "You cannot rent for 0 days!"); // Warn user that length of rent
                                                                                        // must be greater than 0 days
                append = false;
            }
        }

        // Catch NumberFormatException in case user tries to enter a string
        catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(userInfo, "You must enter a number as your renting period length!"); // Warn
                                                                                                               // user
                                                                                                               // that
                                                                                                               // length
                                                                                                               // of
                                                                                                               // rent
                                                                                                               // must
                                                                                                               // be a
                                                                                                               // number
            append = false;
        }

        // If user input meets requirements
        if (append) {
            // Save inputted information
            firstName.add(firstNameField.getText());
            lastName.add(lastNameField.getText());
            customers++; // Increment counter

            // If the vehicle rented is a truck, then add a truck and the index
            if (vehicleEvent.equals("Truck")) {
                vehicleChoice.add(vehicleEvent + "," + truckIndex);
                truckIndex++;
            }
            // Else if the vehicle rented is a sedan, then add a sedan and the index
            else {
                vehicleChoice.add(vehicleEvent + "," + sedanIndex);
                sedanIndex++;
            }

            String[] vehicleType = vehicleChoice.get(customers - 1).split(","); // Split the vehicle element

            // If the vehicle is a truck
            if (vehicleType[0].equals("Truck")) {
                Truck temp = truckStack.pop(); // Remove element from Stack
                typeField.setText(temp.getType()); // Set the uneditable JTextField with the type of vehicle
                totalPriceFieldRentalPage
                        .setText("$ " + (temp.priceOfVehicle() * 24) * lengthOfRent.get(customers - 1)); // Set the
                                                                                                         // uneditable
                                                                                                         // JTextField
                                                                                                         // with the
                                                                                                         // price of the
                                                                                                         // vehicle
                truckArray.add(temp); // Add the object to the array
            }

            // Else if the vehicle is a Sedan
            else {
                Sedan temp = sedanQueue.remove();// Remove element from Queue
                typeField.setText(temp.getType()); // Set the uneditable JTextField with the type of vehicle
                totalPriceFieldRentalPage
                        .setText("$ " + (temp.priceOfVehicle() * 24) * lengthOfRent.get(customers - 1)); // Set the
                                                                                                         // uneditable
                                                                                                         // JTextField
                                                                                                         // with the
                                                                                                         // price of the
                                                                                                         // vehicle
                sedanArray.add(temp); // Add the object to the array
            }
            generateUserCode();// Generate the User ID Code
            userCodeFieldOut.setText(String.valueOf(userCodes.get(customers - 1))); // Set the latest element of
                                                                                    // userCodes to the uneditable
                                                                                    // JTextField
        }
        firstNameField.setText(""); // Make the JTextField for customer first name blank
        lastNameField.setText(""); // Make the JTextField for customer last name blank
        lengthOfRentField.setText(""); // Make the JTextField for customer renting duration blank
    }

    /*
     * instructionsPage() loads the tutorial page. It takes no input and displays
     * several tutorials on the program's main functions
     */
    public void instructionsPage() {
        contentPanel = getContentPane();
        JPanel instructionsPanel = new JPanel();

        instructionsPanel.setLayout(new BorderLayout()); // Set the layout for the JPanel

        // Declare and initialize main JLabel
        JLabel helpLabel1 = new JLabel("<html>To rent a vehicle: "
                + "<br> 1. Simply navigate to the Rent Vehicle tab on the menu bar.</br>"
                + "<br> 2. Select your vehicle of choice from the drop down menu.</br>"
                + "<br> 3. Fill out the form appropriately and save the User ID Code by copying it.</br>"
                + "<br> 4. You have successfully rented a vehicle.</br>" + "<br> </br>"
                + "<br>To check in a previously rented vehicle: </br>"
                + "<br> 1. Simply navigate to the Check-In Vehicle tab on the menu bar.</br>"
                + "<br> 2. Select Check-In from the drop down menu.</br>"
                + "<br> 3. Enter the User ID Code received when renting your vehicle.</br>"
                + "<br> 4. You have successfully checked in your vehicle.</br>" + "<br> </br>"
                + "<br>To check for availability for a certain vehicle: </br>"
                + "<br> 1. Simply navigate to the Display tab on the menu bar.</br>"
                + "<br> 2. Select Display Vehicles from the drop down menu.</br>"
                + "<br> 3. Under the Type column check if your vehicle of choice has any entries.</br>"
                + "<br> 4. If any entries exist, then there is availability for your vehicle of choice.</br>"
                + "<br> </br>" + "<br>Please report any and all bugs to <u>Mahin.Kukreja@vhs.ucourses.com<u></br>"
                + "</html>");

        instructionsPanel.add(helpLabel1, BorderLayout.NORTH); // Add JLabel to instructionsPanel

        contentPanel.removeAll(); // Clear contentPanel

        contentPanel.add(instructionsPanel); // Add instructionsPanel to contentPanel
        validate(); // Refresh the GUI
    }

    /*
     * displayVehicles() displays all the available vehicles in a JTable. It takes
     * the getVehicles function to save the information as a 2D array and outputs a
     * neat JTable with a header
     */
    public void displayVehicles() {
        contentPanel = getContentPane();

        JPanel tablePanel = new JPanel(); // Declare and initialize tablePanel
        contentPanel.removeAll(); // Clear contentPanel

        // Set table header
        JLabel tableHeader = new JLabel("Available Vehicles to Rent", JLabel.CENTER);
        tableHeader.setFont(new Font("Times New Roman", Font.BOLD + Font.ITALIC, 27));

        // Set table parameters
        String[] columnNames = { "Type", "Price Per Hour", "Wheels" };
        String[][] vehicles2D = getVehicles();

        JTable table = new JTable(vehicles2D, columnNames); // Create JTable
        table.setDefaultEditor(Object.class, null); // Makes table uneditable
        JScrollPane scrollPane = new JScrollPane(table); // Add JTable to scrollPane

        // Add the tableHeader and scrollPane to the tablePanel
        tablePanel.add(tableHeader, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tablePanel); // Add the tablePanel to the contentPanel

        validate(); // Refresh the GUI
    }

    /*
     * exitProcess() is a method that exits the program. It takes the main program
     * window as a parameter and exits the program safely as output
     */
    public static void exitProcess(VehicleInventoryManager screen) {
        screen.dispose();
        System.exit(0);
    }

    /*
     * actionPerformed() executes methods based on where the user clicks their
     * mouse. It takes ActionEvent e and has many outputs based on what type of
     * ActionEvent has occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vehicleTruck) {
            vehicleEvent = "Truck"; // Set vehicle type
            getUserInfo(); // Run method
        } else if (e.getSource() == vehicleSedan) {
            vehicleEvent = "Sedan";// Set vehicle type
            getUserInfo(); // Run method
        } else if (e.getSource() == checkInVehicle) {
            checkInPage(); // Load check-in page
        } else if (e.getSource() == checkInPageButton) {
            checkInVehicle(); // Run check-in vehicle function
        } else if (e.getSource() == submitButton) {
            try {
                appendCustomerInfo(); // Append customer information
            } catch (NullPointerException nullPointerException) {
                JOptionPane.showMessageDialog(userInfo, "This current vehicle is out of stock! Please return later!"); // Tell
                                                                                                                       // user
                                                                                                                       // that
                                                                                                                       // vehicle
                                                                                                                       // is
                                                                                                                       // out
                                                                                                                       // of
                                                                                                                       // stock
                customers--; // Decrement the counter
                firstNameField.setText(""); // Make the JTextField for customer first name blank
                lastNameField.setText(""); // Make the JTextField for customer last name blank
                lengthOfRentField.setText(""); // Make the JTextField for customer renting duration blank
            }
        } else if (e.getSource() == display) {
            try {
                displayVehicles(); // Display vehicles in tabulated format
            } catch (NullPointerException nullPointerException) {
                JOptionPane.showMessageDialog(userInfo, "All vehicles are out of stock! Please return later!"); // Tell
                                                                                                                // user
                                                                                                                // that
                                                                                                                // vehicles
                                                                                                                // are
                                                                                                                // out
                                                                                                                // of
                                                                                                                // stock
            }
        } else if (e.getSource() == helpInstructions) {
            instructionsPage(); // Load instructions page
        }
    }

    public static void main(String[] args) throws IOException {
        VehicleInventoryManager screen = new VehicleInventoryManager();

        fillVehicleStock(); // Add vehicles to Stack and Queue
        try {
            loadInformation(); // Load information
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not created yet. If this is your first time running the program, do not panic!"); // Warning
                                                                                                                       // message
                                                                                                                       // for
                                                                                                                       // FileNotFoundException
        }

        // Function to export information and safely close when user closes the window
        screen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        screen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    saveInformation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exitProcess(screen);
            }
        });

        // Set window information
        screen.setTitle("Vehicle Inventory Manager v1.0.1");
        screen.setSize(500, 550);
        screen.setVisible(true);
        screen.setResizable(false);
    }
}