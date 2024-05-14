/* Ayush Acharya
 * April 7, 2024
 */

/*
 * 
 * General class that simulates a ride or a delivery in a simple Uber/Taxi app
 * 
 * This class is made abstract since we never create an object.
 */
abstract public class Service
{  
  private String from;
  private String to;
  private Passenger user;
  private String type;  // Currently Ride or Delivery but other services could be added      
  private int distance; // Units are City Blocks
  private double cost;  // Cost of the service
  
  public Service(String from, String to, Passenger user, int distance, double cost, String type)
  {
    //this.serviceNumber = serviceNum;
    this.from = from;
    this.to = to;
    this.user = user;
    this.distance = distance;
    this.cost = cost;
    this.type = type;
    this.distance = 0;
  }


  // Subclasses define their type (e.g. "RIDE" OR "DELIVERY") 
  abstract public String getServiceType();

  // Getters and Setters
  
  public String getFrom()
  {
    return from;
  }
  public void setFrom(String from)
  {
    this.from = from;
  }
  public String getTo()
  {
    return to;
  }
  public void setTo(String to)
  {
    this.to = to;
  }
  public Passenger getUser()
  {
    return user;
  }
  public void setUser(Passenger user)
  {
    this.user = user;
  }
  public int getDistance()
  {
    return distance;
  }
  public void setDistance(int distance)
  {
    this.distance = distance;
  }
  public double getCost()
  {
    return cost;
  }
  public void setCost(double cost)
  {
    this.cost = cost;
  }

  
  
  // Two service requests are equal if they have the same type and same user
  // Make sure type is checked first!
  public boolean equals(Object other)
  {
    Service otherService = (Service) other;
    return type.equals(otherService.type) && user.equals(otherService.user);
  }
  
  // Print Information 
  public void printInfo()
  {
    System.out.printf("\nType: %-9s From: %-15s To: %-15s", type, from, to);
    System.out.print("\nUser: ");
    user.printInfo();
  }
}
