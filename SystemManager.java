/* Ayush Acharya
 * April 7, 2024
 */


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;




/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all passengers, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class SystemManager
{
  private Map<String, Passenger> users;
  private ArrayList<Passenger> userList;
  private ArrayList<Driver> drivers;

  private Queue<Service> serviceRequests[];

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  // These variables are used to generate passenger account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public SystemManager()
  {
    users = new HashMap<String, Passenger>();
    userList = new ArrayList<>();
    drivers = new ArrayList<>();

    serviceRequests = (Queue<Service>[])new Queue[4]; 
    for (int i = 0; i<serviceRequests.length; i++){
      serviceRequests[i] = new LinkedList<Service>();
    }

    totalRevenue = 0;
  }

  
  // Generate a new passenger account id
  private String generateUserAccountId()
  {
    return "" + userAccountId + users.size();
  }
  
  // Generate a new driver id
  private String generateDriverId()
  {
    return "" + driverId + drivers.size();
  }

  /**
   * Method to setup the Users map as well as the userList instance with pre-registered users
   *
   * @param userList Takes in a list of Users (from loadPreRegisteredUsers method in Registered)
   */
  public void setUsers(ArrayList<Passenger> userList){
    //Iterating through the given userList 
    for (Passenger u : userList){
      this.userList.add(u); //Saving the given userList in the current instance of userList
      users.put(u.getAccountId(), u); //Mapping the userList with an unique account id as the key and passenger object as the value
    }
  }
  

  /**
   * Method to setup the drivers instance with pre-registered drivers
   *
   * @param userList Takes in a list of Drivers (from loadPreRegisteredDrivers method in Registered)
   */
  public void setDrivers(ArrayList<Driver> drivers){
    //Iterating through the given drivers and saving it in the current instance of drivers
    for (Driver d : drivers){
      this.drivers.add(d);
    }
  }
 
  /**
   * Method to get the passenger object associated with the given accountId
   *
   * @param accountId Takes in an accountId to look for inside users map
   */
  public Passenger getUser(String accountId)
  {
    //Iterates through the set of keys in the users map
    for (String s : users.keySet())
    {
      //If the id from parameter matches with any of the keys, returns the corresponding value i.e passenger object
      if (s.equals(accountId)){
        return users.get(s);
      }
    }
    return null;
  }
  
  // Check for duplicate user
  private void userExists(Passenger user)
  {
    // Simple way
    // return users.contains(user);

    //Iterates through the set of keys in the users map
    for (String s : users.keySet())
    {
      //If the param passenger matches any passenger in the users map, that means it's a duplicate
      if (users.get(s).equals(user)){
        throw new UserAlreadyExistsException(); //Hence returning UserAlreadyExists Exception
      }
    }
  }
  
 // Check for duplicate driver
 private void driverExists(Driver driver)
 {
   // simple way
   // return drivers.contains(driver);
   
   for (int i = 0; i < drivers.size(); i++)
   {

    //Throwing DriverAlreadyExistsException if the given driver object already exists
     if (drivers.get(i).equals(driver)){
      throw new DriverAlreadyExistsException();
     }
   }
 }
  
 
 // Given a user, check if passenger ride/delivery request already exists in service requests
 private void existingRequest(Service req)
 {
   // Simple way
   // return serviceRequests.contains(req);
   
   //Iterating through all the queues in serviceRequests
   for (Queue<Service> q : serviceRequests)
   {
    //If one of the queues already contains the param Service object and the service type is a ride, 
    //throws a UserAlreadyHasRideRequestException and if it's a delivery,
    //throws a UserAlreadyHasDeliveryRequestException
     if (q.contains(req) && req.getServiceType().equals("RIDE")){
      throw new UserAlreadyHasRideRequestException();
     }else if (q.contains(req) && req.getServiceType().equals("DELIVERY")){
      throw new UserAlreadyHasDeliveryRequestException();
     }
   }
 } 
 
  
  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  private Driver getAvailableDriver()
  {
    for (int i = 0; i < drivers.size(); i++)
    {
      Driver driver = drivers.get(i);
      if (driver.getStatus() == Driver.Status.AVAILABLE)
        return driver;
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    int index = 0;
    for (Passenger u : userList)
    {
      index++;
      System.out.printf("%-2s. ", index);
      u.printInfo();
      System.out.println(); 
    }
    
    
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    System.out.println();
    for (int i = 0; i < drivers.size(); i++)
    {
      System.out.println(); 
      int index = i + 1;
      System.out.printf("%-2s. ", index);

      //If they are driving, it should print the from and to address of the corresponding service
      if (drivers.get(i).getStatus().equals(Driver.Status.DRIVING)){
        drivers.get(i).printInfo();
        System.out.println(); 
        Service service = drivers.get(i).getService();
        System.out.printf("From: %-15s To: %-15s", service.getFrom(), service.getTo());
      }else{
        drivers.get(i).printInfo();
      }
      System.out.println(); 
    }
    
   
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    for (int i = 0; i<serviceRequests.length; i++)
    {
      int index = 0;
      System.out.println();
      System.out.println("Zone "+i+"\n======");
      for (Service s : serviceRequests[i]){
        index++;
        System.out.println();
        System.out.print(index + ". ");
        for (int j = 0; j < 60; j++) System.out.print("-");
        s.printInfo();
        System.out.println();
      }
       
    }
  }

  // Add a new passenger to the system
  public void registerNewUser(String name, String address, double wallet)
  {
    // Check to ensure name is valid
    if (name == null || name.equals(""))
    {
      throw new InvalidUserNameException(name);
      
    }
    // Check to ensure address is valid
    if (!MapModel.validAddress(address))
    {
      throw new InvalidUserAddressException(address);
    }
    // Check to ensure wallet amount is valid
    if (wallet < 0)
    {
      throw new InvalidMoneyInWalletException();
    }
    // Check for duplicate user
    Passenger passenger = new Passenger(generateUserAccountId(), name, address, wallet);
    userExists(passenger); 
    userList.add(passenger);
    users.put(generateUserAccountId(), passenger);
    
  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address)
  {
    // Check to ensure name is valid
    if (name == null || name.equals(""))
    {
      throw new InvalidDriverNameException(name);
    }
    // Check to ensure car models is valid
    if (carModel == null || carModel.equals(""))
    {
      throw new InvalidCarModelException(carModel);
    }
    // Check to ensure car licence plate is valid
    // i.e. not null or empty string
    if (carLicencePlate == null || carLicencePlate.equals(""))
    {
      throw new InvalidCarLicensePlateException(carLicencePlate);
    }
    if (!MapModel.validAddress(address)){
      throw new InvalidAddressException(address);
    }
    // Check for duplicate driver. If not a duplicate, add the driver to the drivers list
    Driver driver = new Driver(generateDriverId(), name, carModel, carLicencePlate, address);
    driverExists(driver);
    drivers.add(driver); 
  }

  // Request a ride. passenger wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
    // Check valid passenger account
    Passenger passenger = getUser(accountId);
    if (passenger == null)
    {
      throw new UserAccountNotFoundException(accountId);
    }
    // Check for a valid from and to addresses
    if (!MapModel.validAddress(from))
    {
      throw new InvalidAddressException(from);
    }
    if (!MapModel.validAddress(to))
    {
      throw new InvalidAddressException(to);
    }
    // Get the distance for this ride
    int distance = MapModel.getDistance(from, to);         // city blocks
    // Distance == 0 or == 1 is not accepted - walk!
    if (!(distance > 1))
    {
      throw new InsufficientTravelDistanceException();
    }
    // Check if passenger has enough money in wallet for this trip
    double cost = getRideCost(distance);
    if (passenger.getWallet() < cost)
    {
      throw new InsufficientFundsException();
    }
    // Get an available driver
    Driver driver = getAvailableDriver();
    if (driver == null) 
    {
      throw new NoDriversAvailableException();
    }
    // Create the request
    Ride req = new Ride(from, to, passenger, distance, cost);
    int zone = MapModel.getCityZone(from);

    // Check if existing ride request for this passenger - only one ride request per passenger at a time
    existingRequest(req);
    serviceRequests[zone].add(req); //Adding the request to the corresponding zone
    passenger.addRide(); 
  }

  // Request a food delivery. passenger wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // Check for valid passenger account
    Passenger passenger = getUser(accountId);
    if (passenger == null)
    {
     throw new UserAccountNotFoundException(accountId); 
    }
    // Check for valid from and to address
    if (!MapModel.validAddress(from))
    {
      throw new InvalidAddressException(from);
    }
    if (!MapModel.validAddress(to))
    {
      throw new InvalidAddressException(to);
    }
    // Get the distance to travel
    int distance = MapModel.getDistance(from, to);         // city blocks
    // Distance must be at least 1 city block
    if (distance == 0)
    {
      throw new InsufficientTravelDistanceException();
    }
    // Check if passenger has enough money in wallet for this delivery
    double cost = getDeliveryCost(distance);
    if (passenger.getWallet() < cost)
    {
      throw new InsufficientFundsException();
    }
    // Find an available driver, if any
    Driver driver = getAvailableDriver();
    if (driver == null) 
    {
      throw new NoDriversAvailableException();
    }
    Delivery delivery = new Delivery(from, to, passenger, distance, cost, restaurant, foodOrderId); 
    int zone = MapModel.getCityZone(from);

    // Check if existing delivery request for this passenger for this restaurant and food order #
    existingRequest(delivery);
    
    
    serviceRequests[zone].add(delivery); //Adding the delivery to the corresponding zone
    passenger.addDelivery();
  }


  // Cancel an existing service request. 
  // parameter request is the index in the serviceRequests array list
  public void cancelServiceRequest(int zone, int request)
  {
    // Check if zone
    if (zone < 0 || zone > 3){
      throw new InvalidZoneException(zone);
    }
    //Check if the zone has any request
    if (serviceRequests[zone]==null){
      throw new NoServiceRequestedException(zone);
    }
    //Chcek if valid request #
    if (request < 1 || request>serviceRequests[zone].size())
    {
      throw new InvalidRequestNumberException(request);
    }
    Service cancelledService = getRequest(zone, request-1);
    serviceRequests[zone].remove(cancelledService); //Removing the Service object associated with the request number
  }
  
 /**
 * Helper method for cancelServiceRequest
 * Finds the Service object associated with the request number
 *
 * @param zone Zone number
 * @param request Request number
 * @return Returns a Service object
 */
  private Service getRequest (int zone, int request){
    int index = 0; 
    //Looping through the Queue of given zone
    for (Service s : serviceRequests[zone]){
      //If the index is equal to request-1, returns the service
      if (index==request){
        return s;
      }
      index++;
    }
    return null;
  }
  

  // Drop off a ride or a delivery. This completes a service.
  // parameter driverId gives the driver that is supposed to dropoff a request in their zone
  public void dropOff(String driverId)
  {
    Driver driver = getDriver(driverId); //Using the getDriver helper method to get the driver object
    //Checking if the driver exists
    if (driver==null ){
      throw new DriverNotFoundException();
    }
    //checking if the driver is driving 
    if (!driver.getStatus().equals(Driver.Status.DRIVING)){
      throw new DriverNotDrivingException();
    }
    int driverZone = driver.getZone();
    Service service = driver.getService(); // Getting the service that was picked up by the driver
    //If the Service object is null, that means there are no service requests in the queue
    if (service==null){
      throw new NoServiceRequestedException(driverZone);
    }
    totalRevenue += service.getCost();          // add service cost to revenues
    driver.pay(service.getCost()*PAYRATE);      // pay the driver
    totalRevenue -= service.getCost()*PAYRATE;  // deduct driver fee from total revenues
    Passenger passenger = service.getUser();
    passenger.payForService(service.getCost());      // passenger pays for ride or delivery
    
    driver.setAddress(service.getTo()); //Setting the new driver address to drop off location
    driver.setZone(MapModel.getCityZone(service.getTo()));
    driver.setStatus(Driver.Status.AVAILABLE);  // driver is now available again
    driver.setService(null); //Setting the service back to null after dropoff
    
  }

  /**
 * Method to pickup a service request in the zone that the driver is located at
 *
 * @param driveId Driver ID
 */
  public void pickup(String driverId){
    System.out.println();
    Driver driver = getDriver(driverId); //Getting the driver object
    //Checking if the driver is found
    if (driver==null ){
      throw new DriverNotFoundException();
    }
    int zone = driver.getZone();
    //Checking if there's any service requested, the driver has nothing to pickup
    if (serviceRequests[zone].size()==0){
      throw new NoServiceRequestedException(zone);
    }
    Service first = serviceRequests[zone].remove(); //Service in front of the queue (to be removed)
    //If the Service object is null, that means there are no service requests in the queue

    driver.setService(first); //Setting the service to first, so that it can be accessed during dropoff
    driver.setStatus(Driver.Status.DRIVING); //The driver is driving
    driver.setAddress(first.getFrom()); //The new address of driver is the from address
  }

 /**
 * Helper method to find the Driver object associated with the given driverId
 *
 * @param driverId Driver ID
 * @return Returns a Driver object
 */
  public Driver getDriver(String driverId){
    for (Driver d : drivers){
      if (d.getId().equals(driverId)){
        return d;
      }
    }
    return null;
  }

  /**
 * Method to make a driver drive to a certain address
 *
 * @param driverId Driver ID
 * @param address Address to driveTo
 * @return Returns a Service object
 */
  public void driveTo(String driverId, String address){
    Driver driver = getDriver(driverId); //Getting the driver object
    //Check if driver is found
    if (driver==null ){
      throw new DriverNotFoundException();
    }
    //Checking if the driver's status is available
    if (!driver.getStatus().equals(Driver.Status.AVAILABLE)){
      throw new DriverNotAvailableException();
    }
    //Checking if valid address
    if (!MapModel.validAddress(address)){
      throw new InvalidAddressException(address);
    }
    driver.setAddress(address); //Setting the driver address to the address they are driving to
    
    //Note that this new address may trigger a zone change for the driver. Do the usual error checking.
    //...........
  }
  
  // Sort users by name
  public void sortByUserName()
  {
    Collections.sort(userList, new NameComparator());
    listAllUsers();
  }

  private class NameComparator implements Comparator<Passenger>
  {
    public int compare(Passenger a, Passenger b)
    {
      return a.getName().compareTo(b.getName());
    }
  }

  // Sort users by number amount in wallet
  public void sortByWallet()
  {
    Collections.sort(userList, new UserWalletComparator());
    listAllUsers();
  }

  private class UserWalletComparator implements Comparator<Passenger>
  {
    public int compare(Passenger a, Passenger b)
    {
      if (a.getWallet() > b.getWallet()) return 1;
      if (a.getWallet() < b.getWallet()) return -1; 
      return 0;
    }
  }

  // Sort trips (rides or deliveries) by distance
  // class Service must implement Comparable
  // public void sortByDistance()
  // {
  //   Collections.sort(serviceRequests);
  //   listAllServiceRequests();
  // }
}


/**
 * Custom exception classes
 *
 * @throws RuntimeException throws custom exceptions that extend RuntimeException
 */
//passenger exceptions: 
class UserAccountNotFoundException extends RuntimeException{
  public UserAccountNotFoundException (String accountId){
   super("passenger Account Not Found "+accountId );
  }
}
class InvalidUserNameException extends RuntimeException{
  public InvalidUserNameException (String name){
   super("Invalid passenger Name " + name);
  }
}
class InvalidUserAddressException extends RuntimeException{
  public InvalidUserAddressException (String address){
   super("Invalid passenger Address " + address);
  }
}
class UserAlreadyExistsException extends RuntimeException{
  public UserAlreadyExistsException (){
   super("passenger Already Exists in System");
  }
}
class UserAlreadyHasRideRequestException extends RuntimeException{
  public UserAlreadyHasRideRequestException (){
   super("passenger Already Has Ride Request");
  }
}
class UserAlreadyHasDeliveryRequestException extends RuntimeException{
  public UserAlreadyHasDeliveryRequestException (){
   super("passenger Already Has Delivery Request at Restaurant with this Food Order");
  }
}


//Driver exceptions:
class DriverNotFoundException extends RuntimeException{
  public DriverNotFoundException (){
   super("Driver Not Found");
  }
}
class InvalidDriverNameException extends RuntimeException{
  public InvalidDriverNameException (String name){
   super( "Invalid Driver Name " + name);
  }
}
class InvalidCarModelException extends RuntimeException{
  public InvalidCarModelException (String carModel){
   super("Invalid Car Model " + carModel);
  }
}
class InvalidCarLicensePlateException extends RuntimeException{
  public InvalidCarLicensePlateException (String carLicensePlate){
   super("Invalid Car Licence Plate " + carLicensePlate);
  }
}
class NoDriversAvailableException extends RuntimeException{
  public NoDriversAvailableException (){
   super("No Drivers Available");
  }
}
class DriverAlreadyExistsException extends RuntimeException{
  public DriverAlreadyExistsException (){
   super("Driver Already Exists in System");
  }
}
class DriverNotAvailableException extends RuntimeException{
  public DriverNotAvailableException (){
   super("Driver Is Already Driving");
  }
}
class DriverNotDrivingException extends RuntimeException{
  public DriverNotDrivingException (){
   super("Driver is Not Driving");
  }
}


//Misc exceptions:
class InvalidAddressException extends RuntimeException{
  public InvalidAddressException (String address){
   super("Invalid Address "+address);
  }
}
class InvalidMoneyInWalletException extends RuntimeException{
  public InvalidMoneyInWalletException (){
   super("Invalid Money in Wallet");
  }
}
class InsufficientTravelDistanceException extends RuntimeException{
  public InsufficientTravelDistanceException (){
   super("Insufficient Travel Distance");
  }
}
class InsufficientFundsException extends RuntimeException{
  public InsufficientFundsException (){
   super("Insufficient Funds");
  }
}
class InvalidRequestNumberException extends RuntimeException{
  public InvalidRequestNumberException (int request){
   super("Invalid Request # " + request);
  }
}
class InvalidZoneException extends RuntimeException{
  public InvalidZoneException (int zone){
   super("Invalid Zone " + zone);
  }
}
class NoServiceRequestedException extends RuntimeException{
  public NoServiceRequestedException(int zone){
    super(" No Service Request in Zone "+zone);
  }
}

