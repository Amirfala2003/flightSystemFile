import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class TicketFile {
    public RandomAccessFile getTicketFile() {
        return ticketFile;
    }

    private RandomAccessFile ticketFile;
    private final int FIX_SIZE = 20;
    private RandomAccessFile userFile;

    private final int FIX_SIZE2 = 6244;

    public TicketFile() throws FileNotFoundException {
        this.ticketFile = new RandomAccessFile("ticketFile.dat", "rw");
        this.userFile = new RandomAccessFile("userFile.dat","rw");
    }

    public void writeFlightInFile(Flight flight) throws IOException {
        ticketFile.writeChars(fixStringToWrite(flight.getFlightID()));
        ticketFile.writeChars(fixStringToWrite(flight.getOrigin()));
        ticketFile.writeChars(fixStringToWrite(flight.getDestination()));
        ticketFile.writeChars(fixStringToWrite(flight.getDate()));
        ticketFile.writeChars(fixStringToWrite(flight.getTime()));
        ticketFile.writeInt(flight.getPrice());
        ticketFile.writeInt(flight.getSeats());
    }

    public Flight readFlightFromFile() throws IOException {
        String ticketId = readFixString();
        String flightId = readFixString();
        String origin = readFixString();
        String destination = readFixString();
        String date = readFixString();
        String time = readFixString();
        int price = ticketFile.readInt();
        int seats = ticketFile.readInt();
        return new Flight(flightId, origin, destination, date, time, price, seats);
    }

    public String addTicket(String username, Flight flight) throws IOException {
        for (int i = 0; i < userFile.length()/84; i++) {
            int seek = i * FIX_SIZE2;
            ticketFile.seek(seek);
            String str = readFixString();
            int count = ticketFile.readInt();
            if (username.equals(str) && count < 25) {
                ticketFile.seek(seek + 44 + count * 248);
                String generate = generateTicketId(count);
                ticketFile.writeChars(fixStringToWrite(generate));
                writeFlightInFile(flight);
                ticketFile.seek(seek + 40);
                ticketFile.writeInt(count + 1);
                return generate;
            }

        }
        return null;

    }
    public String removeTicket(String username,String ticketId) throws IOException {
        for (int i = 0; i < userFile.length()/84; i++) {
            int seek = i * FIX_SIZE2;
            ticketFile.seek(seek);
            String str = readFixString();
            int count = ticketFile.readInt();
            if (username.equals(str)){
                for (int j = 0; j < count; j++) {
                    ticketFile.seek(seek+44+j*248);
                    String ticket = readFixString();
                    if (ticketId.equals(ticket)){
                        String flightId = readFixString();
                        for (int k = j; k < count-1; k++) {
                            ticketFile.seek(seek+44+(k+1)*248);
                            String newTicket = readFixString();
                            ticketFile.seek(seek+44+(k+1)*248);
                            Flight flight = readFlightFromFile();
                            ticketFile.seek(seek+44+k*248);
                            ticketFile.writeChars(fixStringToWrite(newTicket));
                            writeFlightInFile(flight);

                        }
                        ticketFile.seek(seek+40);
                        ticketFile.writeInt(count-1);
                        return flightId;
                    }
                }
            }

        }
        return null;
    }
    public void showTicket(User user) throws IOException {
        System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%-10s|%-5s|\n","FlightId", "Origin", "Destination", "Date",
                "Time", "Price", "seats");
        System.out.println("...........................................................................");
        for (int i = 0; i < userFile.length()/84; i++) {
            int seek = i *FIX_SIZE2;
            ticketFile.seek(seek);
            String str = readFixString();
            int count = ticketFile.readInt();
            if(str.equals(user.getUsername())){
                for (int j = 0; j < count; j++) {
                    ticketFile.seek(seek+44+j*248);
                    printTicket(readFlightFromFile());
                }
                return;
            }

        }
    }
    public void printTicket(Flight flight){
        System.out.printf("|%-10s|%-10s|%-13s|%-13s|%-6s|%,-10d|%-5s|\n", flight.getFlightID(),
                flight.getOrigin(), flight.getDestination(),flight.getDate(),flight.getTime(),
                flight.getPrice(),flight.getSeats());
        System.out.println("...........................................................................");
    }
    public boolean findUserTicket(Flight flight) throws IOException {
        for (int i = 0; i < userFile.length()/84; i++) {
            ticketFile.seek(i*FIX_SIZE2);
            String username = readFixString();
            int count = ticketFile.readInt();
            for (int j = 0; j < count; j++) {
                if(flight.getFlightID().equals(readFlightFromFile().getFlightID()))
                    return true;
            }
        }
        return false;
    }

    public String generateTicketId(int code) {
        String[] strings = "4QAZ6XSW5EDC8VFR2TGB9NH1YU0JM3KIO7LP".split("");
        Random random = new Random();
        String id;

        id = "";
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(strings.length);
            id = id + strings[x];
        }
        id+= String.valueOf(code);
        return id;


    }

    public void addTicket(String userName) throws IOException {
        ticketFile.seek((userFile.length()/84 - 1) * FIX_SIZE2);
        ticketFile.writeChars(fixStringToWrite(userName));
        ticketFile.writeInt(0);
    }


    public String readFixString() throws IOException {
        String tpm = "";
        for (int i = 0; i < FIX_SIZE; i++) {
            tpm += ticketFile.readChar();
        }
        return tpm.trim();
    }

    public String fixStringToWrite(String str) {
        while (str.length() < FIX_SIZE)
            str += " ";
        return str.substring(0, FIX_SIZE);
    }
}
