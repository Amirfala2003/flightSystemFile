import java.util.Random;

public class User {
    private String username, password;
    private int charge;
    private Flight[] flights;
    private int count;
    private int code;
    private String[] ticketID;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.charge = 0;
        this.flights = new Flight[50];
        this.ticketID = new String[50];
        this.count = 0;
        this.code = 0;
    }

    public Flight[] getFlights() {
        return flights;
    }

    public void setFlights(Flight[] flights) {
        this.flights = flights;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String addFlight(Flight newFlight) {
//        flights[count] = newFlight;
//        String ticketId = generateTicketId();
//        //flights[count].setTicketId(ticketId);
//        ticketID[count] = ticketId;
//        setCharge(getCharge()-newFlight.getPrice());
//        count++;
//        return ticketId;
//
//    }

    public String generateTicketId() {
        String[] strings = "4QAZ6XSW5EDC8VFR2TGB9NH1YU0JM3KIO7LP".split("");
        Random random = new Random();
        String id;

        id = "";
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(strings.length);
            id = id + strings[x];
        }
        id+= String.valueOf(code);
        code++;
        return id;


    }
    public String removeTicket(String ticketId){
        for (int i = 0; i < flights.length; i++) {
            if (flights[i]!= null && ticketId.equals(ticketID[i])){
                String flightId = flights[i].getFlightID();
                flights[i] = null;
                ticketID[i] = null;
                return flightId;
            }

        }
        return null;
    }
    public Flight findFlight(Flight flight){
        for (int i = 0; i < flights.length; i++) {
            if (flights[i]!=null&&flights[i]==flight){
                return flights[i];
            }

        }
        return null;
    }
    public void findTicketId(String flightId){
        boolean notFound = true;
        for (int i = 0; i < flights.length; i++) {
            if (flights[i] != null && flightId.equals(flights[i].getFlightID())) {
                System.out.printf("your ticket ID is: %s\n", ticketID[i]);
                notFound = false;
            }
        }
            if (notFound) {
                System.err.println("No ticket ID found with this flight ID");
            }

    }




    public void showTicket() {
        System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%-10s|%-5s|\n","FlightId", "Origin", "Destination", "Date",
                "Time", "Price", "seats");
        System.out.println("...........................................................................");
        for (int i = 0; i < flights.length; i++) {
            if (flights[i]!=null){
                System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%,-10d|%-5s|\n", flights[i].getFlightID(),
                        flights[i].getOrigin(), flights[i].getDestination(),flights[i].getDate(),flights[i].getTime(),
                        flights[i].getPrice(),flights[i].getSeats());
                System.out.println("...........................................................................");
            }
        }

        }

}


