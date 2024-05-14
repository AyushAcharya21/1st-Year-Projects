/* Ayush Acharya
 * April 7, 2024
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;

import javax.naming.InvalidNameException;

// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class UserInterface
{
  public static void main(String[] args) 
  {
    // Create the System Manager - the main system code is in here 

    SystemManager system = new SystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      try{
        if (action == null || action.equals("")) 
        {
          System.out.print("\n>");
          continue;
        }
        // Quit the App
        else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
          return;
        // Print all the registered drivers
        else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
        {
          system.listAllDrivers(); 
        }
        // Print all the registered users
        else if (action.equalsIgnoreCase("USERS"))  // List all users
        {
          system.listAllUsers(); 
        }
        // Print all current ride requests or delivery requests
        else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
        {
          system.listAllServiceRequests(); 
        }
        // Register a new driver
        else if (action.equalsIgnoreCase("REGDRIVER")) 
        {
          String name = "";
          System.out.print("Name: ");
          if (scanner.hasNextLine())
          {
            name = scanner.nextLine();
          }
          String carModel = "";
          System.out.print("Car Model: ");
          if (scanner.hasNextLine())
          {
            carModel = scanner.nextLine();
          }
          String license = "";
          System.out.print("Car License: ");
          if (scanner.hasNextLine())
          {
            license = scanner.nextLine();
          }
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine())
          {
            address = scanner.nextLine();
          }
          
          system.registerNewDriver(name, carModel, license, address);
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s", name, carModel, license);
          
        }
        // Register a new user
        else if (action.equalsIgnoreCase("REGUSER")) 
        {
          String name = "";
          System.out.print("Name: ");
          if (scanner.hasNextLine())
          {
            name = scanner.nextLine();
          }
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine())
          {
            address = scanner.nextLine();
          }
          double wallet = 0.0;
          System.out.print("Wallet: ");
          if (scanner.hasNextDouble())
          {
            wallet = scanner.nextDouble();
            scanner.nextLine(); // consume nl
          }
          system.registerNewUser(name, address, wallet);
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
        }
        // Request a ride
        else if (action.equalsIgnoreCase("REQRIDE")) 
        {
          String account = "";
          System.out.print("Passenger Account Id: ");
          if (scanner.hasNextLine())
          {
            account = scanner.nextLine();
          }
          String from = "";
          System.out.print("From Address: ");
          if (scanner.hasNextLine())
          {
            from = scanner.nextLine();
          }
          String to = "";
          System.out.print("To Address: ");
          if (scanner.hasNextLine())
          {
            to = scanner.nextLine();
          }
          system.requestRide(account, from, to);
          Passenger passenger = system.getUser(account);
          System.out.printf("\nRIDE for: %-15s From: %-15s To: %-15s", passenger.getName(), from, to);
        
        }
        // Request a food delivery
        else if (action.equalsIgnoreCase("REQDLVY")) 
        {
          String account = "";
          System.out.print("Passenger Account Id: ");
          if (scanner.hasNextLine())
          {
            account = scanner.nextLine();
          }
          String from = "";
          System.out.print("From Address: ");
          if (scanner.hasNextLine())
          {
            from = scanner.nextLine();
          }
          String to = "";
          System.out.print("To Address: ");
          if (scanner.hasNextLine())
          {
            to = scanner.nextLine();
          }
          String restaurant = "";
          System.out.print("Restaurant: ");
          if (scanner.hasNextLine())
          {
            restaurant = scanner.nextLine();
          }
          String foodOrder = "";
          System.out.print("Food Order #: ");
          if (scanner.hasNextLine())
          {
            foodOrder = scanner.nextLine();
          }
          system.requestDelivery(account, from, to, restaurant, foodOrder);
          Passenger passenger = system.getUser(account);
          System.out.printf("\nDELIVERY for: %-15s From: %-15s To: %-15s", passenger.getName(), from, to);  
        }
        // Sort users by name
        else if (action.equalsIgnoreCase("SORTBYNAME")) 
        {
          system.sortByUserName();
        }
        // Sort users by number of ride they have had
        else if (action.equalsIgnoreCase("SORTBYWALLET")) 
        {
          system.sortByWallet();
        }
        // Sort current service requests (ride or delivery) by distance
        // else if (action.equalsIgnoreCase("SORTBYDIST")) 
        // {
        //   system.sortByDistance();
        // }
        // Cancel a current service (ride or delivery) request
        else if (action.equalsIgnoreCase("CANCELREQ")) 
        {
          int zone = -1;
          System.out.print("Zone #: ");
          if (scanner.hasNextInt())
          {
          zone = scanner.nextInt();
          scanner.nextLine(); // consume nl character
          }
          int request = -1;
          System.out.print("Request #: ");
          if (scanner.hasNextLine())
          {
            request = scanner.nextInt();
            scanner.nextLine(); // consume nl character
          }
          system.cancelServiceRequest(zone, request);
          System.out.println("Service request #" + request + " cancelled");
        }
          
        // Drop-off the passenger or the food delivery to the destination address
        else if (action.equalsIgnoreCase("DROPOFF")) 
        {
          String driverID = "";
          System.out.print("Driver ID: ");
          if (scanner.hasNextLine())
          {
            driverID = scanner.nextLine();
          }
          system.dropOff(driverID);
          System.out.println("Driver "+driverID+" Dropping Off");
        }

        // Get the Current Total Revenues
        else if (action.equalsIgnoreCase("REVENUES")) 
        {
          System.out.println("Total Revenue: " + system.totalRevenue);
        }
        // Unit Test of Valid City Address 
        else if (action.equalsIgnoreCase("ADDR")) 
        {
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine())
          {
            address = scanner.nextLine();
          }
          System.out.print(address);
          if (MapModel.validAddress(address))
            System.out.println("\nValid Address"); 
          else
            System.out.println("\nBad Address"); 
        }
        // Unit Test of MapModel Distance Method
        else if (action.equalsIgnoreCase("DIST")) 
        {
          String from = "";
          System.out.print("From: ");
          if (scanner.hasNextLine())
          {
            from = scanner.nextLine();
          }
          String to = "";
          System.out.print("To: ");
          if (scanner.hasNextLine())
          {
            to = scanner.nextLine();
          }
          System.out.print("\nFrom: " + from + " To: " + to);
          System.out.println("\nDistance: " + MapModel.getDistance(from, to) + " City Blocks");
        }

        //Unit test of PICKUP Method
        else if (action.equalsIgnoreCase("PICKUP")){
          String driverId = "";
          System.out.print("Driver ID: ");
          //Getting an passenger input for driverID
          if (scanner.hasNextLine()){
            driverId = scanner.nextLine();
          }
          system.pickup(driverId); //Picking up the order
          System.out.println("Driver "+driverId+" Picking Up In Zone "+system.getDriver(driverId).getZone());
        }
        //Unit test of LOADUSERS Method
        else if (action.equalsIgnoreCase("LOADUSERS")){
          String userFile = "";
          System.out.print("passenger File: ");
          if (scanner.hasNextLine()){
            userFile = scanner.nextLine();
          }

          //Trying to load users, if there's a file not found exception, printing out a message
          //If there's any other IOException, ends the program with a return statement
          try{
            system.setUsers(Registered.loadPreregisteredPassengers(userFile));
            System.out.println("Users Loaded");
          }catch (FileNotFoundException e){
            System.out.println("Users File : "+userFile+" Not Found");
          }catch (IOException e){
            return;
          }

        }
        //Unit test of LOADDRIVERS Method
        else if (action.equalsIgnoreCase("LOADDRIVERS")){
          String driverFile = "";
          System.out.print("Driver File: ");
          if (scanner.hasNextLine()){
            driverFile = scanner.nextLine();
          }
          
          //Trying to load drivers, if there's a file not found exception, printing out a message
          //If there's any other IOException, ends the program with a return statement
          try{
            system.setDrivers(Registered.loadPreregisteredDrivers(driverFile));
            System.out.println("Drivers Loaded");
          }catch (FileNotFoundException e){
            System.out.println("Drivers File : "+driverFile+" Not Found");
          }catch (IOException e){
            return;
          }

        }
        //Unit test of DRIVETO Method
        else if (action.equalsIgnoreCase("DRIVETO")){
          String driverId = "";
          //Taking in the driverId input
          System.out.print("Driver ID: ");
          if (scanner.hasNextLine()){
            driverId = scanner.nextLine();
          }
          //Taking in address input
          String address = "";
          System.out.print("Address: ");
          if (scanner.hasNextLine()){
            address = scanner.nextLine();
          }
          system.driveTo(driverId, address); //Driving to the given address
          System.out.println("Driver "+driverId+" Now in Zone "+system.getDriver(driverId).getZone());
        }

      //Catches ny runtime exception among the custom ones. Gets the corresponding message
      }catch (RuntimeException e){
        System.out.println(e.getMessage());
      }
      System.out.print("\n>");
    }
  }
}

