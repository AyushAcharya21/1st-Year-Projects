/* Ayush Acharya
 * April 7, 2024
 */

/*
 * 
 * This class simulates a ride service for a simple Uber/Taxi app
 * 
 * A Ride is-a Service with some extra functionality
 */
public class Ride extends Service
{
  private int numPassengers;
  private boolean requestedXL;
  
  public static final String TYPENAME = "RIDE";
  
  public Ride(String from, String to, Passenger user, int distance, double cost)
  {
    super(from, to, user, distance, cost, Ride.TYPENAME);
    requestedXL = false;
    numPassengers = 1;
  }
  
  public String getServiceType()
  {
    return TYPENAME;
  }

  public int getNumPassengers()
  {
    return numPassengers;
  }

  public void setNumPassengers(int numPassengers)
  {
    this.numPassengers = numPassengers;
  }

  public boolean isRequestedXL()
  {
    return requestedXL;
  }

  public void setRequestedXL(boolean requestedXL)
  {
    this.requestedXL = requestedXL;
  }
}
