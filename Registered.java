/* Ayush Acharya
 * April 7, 2024
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Registered
{
    // These variables are used to generate Passenger account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new Passengeraccount id
    public static String generateUserAccountId(ArrayList<Passenger> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

        /**
     * Database of Pre-Registered passengers. Reads the given file and loads the passengers in an ArrayList.
     *
     * @param filename Takes in the name of the file
     * @return Returns an ArrayList of Passengerobjects
     * @throws IOException The compiler throws the IOException
     */
    public static ArrayList<Passenger> loadPreregisteredPassengers(String filename) throws IOException
    {
        ArrayList<Passenger> passengers = new ArrayList<>(); //Creating an arraylist to store the Passenger objects
        Scanner in = new Scanner(new File(filename)); //Creating a scanner to read the File associated with the given filename
       
        //Using a while loop to iterate through all the lines in the file
        while (in.hasNextLine()){
            String name = in.nextLine(); //First line is the name of the user
            String address = in.nextLine(); //Second line is the address of the user
            double wallet = Double.parseDouble(in.nextLine()); //Third line is the wallet amount of the user. Taking in a string and converting to int to avoid errors causes by spaces
            passengers.add(new Passenger(generateUserAccountId(passengers), name, address, wallet)); //Creating a Passenger object
        }
        in.close();
        return passengers;
        
        
    }

     /**
     * Database of Pre-Registered Drivers. Reads the given file and loads the drivers in an ArrayList.
     *
     * @param filename Takes in the name of the file
     * @return Returns an ArrayList of Driver objects
     * @throws IOException The compiler throws the IOException
     */
    public static ArrayList<Driver> loadPreregisteredDrivers(String filename) throws IOException 
    {
        //Uses the same logic as loadPreregisteredpassengers to load in the drivers
        
        ArrayList<Driver> drivers = new ArrayList<>();
        Scanner in = new Scanner(new File(filename));
        while (in.hasNextLine()){
            String name = in.nextLine();
            String carModel = in.nextLine();
            String licensePlate = in.nextLine();
            String address = in.nextLine();
            drivers.add(new Driver(generateDriverId(drivers), name, carModel, licensePlate, address));
        }
        in.close();
        return drivers;
    }
}

