import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Flights {
    private int count1, count2;
    private User[] users;
    private Flight[] flights1;
    private RandomAccessFile urFile;
    private RandomAccessFile frFile;
    private RandomAccessFile trFile;
    private UserFile userFile;
    private FlightFile flightFile;
    private TicketFile ticketFile;

    public Flights() throws IOException {
        this.count1 = 0;
        this.count2 = 0;
        this.users = new User[100];
        this.flights1 = new Flight[100];
        this.userFile = new UserFile();
        this.flightFile = new FlightFile();
        this.ticketFile = new TicketFile();
        this.urFile = userFile.getUserFile();
        this.frFile = flightFile.getFlightFile();
        this.trFile = ticketFile.getTicketFile();
    }

    public User signUp(String userName, String password) throws IOException {
        if (userFile.findUserFromFile(userName) != null) {
            return null;
        }
//        users[count1] = new User(userName, password);
//        count1++;
        User user = new User(userName , password);
        urFile.seek(urFile.length());
        userFile.writeUserInFile(user);
        ticketFile.addTicket(userName);
        return user;

    }

    public User findUser(String userName) {
        for (User user : users) {
            if (user != null && user.getUsername().equals(userName)) {
                return user;
            }

        }
        return null;

    }


    public User signIn(String userName, String password) throws IOException {
        if (userName.equals("Admin") && password.equals("Admin")){
            return new User("Admin","Admin");
        }
        User user = userFile.findUserFromFile(userName);
        if ((user) != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }


    public void addFlight(Flight flight) {
        flights1[count2] = flight;
        count2++;

    }

    public void filterFlight(String flightID, String origin, String destination, String date, String time, int price1, int price2) throws IOException {
        showTicket("FlightId", "Origin", "Destination", "Date", "Time", "Price", "seats");
        for (int i = 0; i < flightFile.getCounter(); i++) {
            frFile.seek(i*208+4);
            Flight flight = flightFile.readFlightFromFile();
            if (flight != null) {
                boolean bFlightId = flightID.equals("") || flightID.equals(flight.getFlightID());
                boolean bOrigin = origin.equals("") || origin.equals(flight.getOrigin());
                boolean bDestination = destination.equals("") || destination.equals(flight.getDestination());
                boolean bDate = date.equals("") || date.equals(flight.getDate());
                boolean bTime = time.equals("") || time.equals(flight.getTime());
                boolean bPrice = price1 == -1 || (price1 < flight.getPrice() && price2 > flight.getPrice());
                if (bFlightId && bOrigin && bDestination && bDate && bTime && bPrice) {
                    showTicket(flight);
                }
            }
        }


    }

    public void showTicket(Flight flight) {
        System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%,-10d|%-5s|\n", flight.getFlightID(), flight.getOrigin(),
                flight.getDestination(), flight.getDate(), flight.getTime(), flight.getPrice(), flight.getSeats());
        System.out.println("...........................................................................");

    }

    public void showTicket(String column1, String column2, String column3, String column4, String column5,
                           String column6, String column7) {
        System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%-10s|%-5s|\n", column1, column2, column3, column4, column5,
                column6, column7);
        System.out.println("...........................................................................");
    }

    public Flight findTicket(String flightId) {
        for (int i = 0; i < flights1.length; i++) {
            if (flights1[i] != null && flightId.equals(flights1[i].getFlightID())) {
                return flights1[i];

            }

        }
        return null;

    }

    public boolean findUserTicket(Flight flight) throws IOException {
        for (int i = 0; i < urFile.length()/84; i++) {

            if (users[i] != null && users[i].findFlight(flight) != null) {
                return true;
            }
        }
        return false;
    }

    public void removeFlight(Flight flight) {
        for (int i = 0; i < flights1.length; i++) {
            if (flights1[i] != null && flights1[i]==flight) {
                flights1[i] = null;
            }
        }
    }
    public void showSchedules() throws IOException {
        System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%-10s|%-5s|\n","FlightId", "Origin", "Destination", "Date",
                "Time", "Price", "seats");
        System.out.println("...........................................................................");
        for (int i = 0; i < flightFile.getCounter(); i++) {
            frFile.seek(i*208+4);
            Flight flight = flightFile.readFlightFromFile();
            System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%,-10d|%-5s|\n", flight.getFlightID(),
                    flight.getOrigin(), flight.getDestination(),flight.getDate(),flight.getTime(),
                    flight.getPrice(),flight.getSeats());
            System.out.println("...........................................................................");


        }
    }
    public String checkFlightId(String flightId) throws IOException {
        Scanner input = new Scanner(System.in);
        while (true) {
            if (flightFile.findFlightFromFile(flightId) != null) {
                System.err.println("This flight ID already exists. please enter another flight ID");
                flightId = input.next();
                continue;

            }
            break;
        }
        return flightId;
    }

}
