import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FlightFile {
    private RandomAccessFile flightFile;

    public RandomAccessFile getFlightFile() {
        return flightFile;
    }

    private final int FIX_SIZE = 20;

    public FlightFile() throws IOException  {
        this.flightFile = new RandomAccessFile("flightFile.dat","rw");
        if(flightFile.length()==0){
            flightFile.writeInt(0);
        }
    }

    public void writeFlightInFile(Flight flight) throws IOException {
        flightFile.writeChars(fixStringToWrite(flight.getFlightID()));
        flightFile.writeChars(fixStringToWrite(flight.getOrigin()));
        flightFile.writeChars(fixStringToWrite(flight.getDestination()));
        flightFile.writeChars(fixStringToWrite(flight.getDate()));
        flightFile.writeChars(fixStringToWrite(flight.getTime()));
        flightFile.writeInt(flight.getPrice());
        flightFile.writeInt(flight.getSeats());
    }
    public Flight readFlightFromFile() throws IOException {
        String flightId = readFixString();
        String origin = readFixString();
        String destination = readFixString();
        String date = readFixString();
        String time = readFixString();
        int price = flightFile.readInt();
        int seats = flightFile.readInt();
        return new Flight(flightId,origin,destination,date,time,price,seats);
    }
    public void removeFlight(String flightId) throws IOException {
        for (int i = 0; i < getCounter(); i++) {
            int seek = i*208+4;
            flightFile.seek(seek);
            if(flightId.equals(readFixString())){
                for (int j = i; j < getCounter()-1; j++) {
                    flightFile.seek((j+1)*208+4);
                    Flight flight = readFlightFromFile();
                    flightFile.seek(j*208+4);
                    writeFlightInFile(flight);
                }
                setCounter(getCounter()-1);
                return;
            }

        }

    }
    public int setSeats(String flightId,int count) throws IOException {
        for (int i = 0; i <getCounter(); i++) {
            int seek = i*208+4;
            flightFile.seek(seek);
            if(flightId.equals(readFixString())){
                flightFile.seek(seek);
                Flight flight = readFlightFromFile();
                flight.setSeats(flight.getSeats()+count);
                flightFile.seek(seek);
                writeFlightInFile(flight);
                return flight.getPrice();
            }
        }
        return 0;

    }
    public void updateFlightInFile(String flightId,Flight flight) throws IOException {
        for (int i = 0; i < getCounter(); i++) {
            int seek = i*208+4;
            flightFile.seek(seek);
            if(flightId.equals(readFixString())){
                flightFile.seek(seek);
                writeFlightInFile(flight);
            }
        }

    }
    public Flight findFlightFromFile(String flightId) throws IOException {
        for (int i = 0; i <getCounter(); i++) {
            int seek = i*208+4;
            flightFile.seek(seek);
            if(flightId.equals(readFixString())){
                flightFile.seek(seek);
                return readFlightFromFile();
            }
        }
        return null;

    }
    public void addFlight(Flight flight) throws IOException {
        flightFile.seek(getCounter()*208+4);
        writeFlightInFile(flight);
        setCounter(getCounter()+1);
    }
    public int getCounter() throws IOException {
        flightFile.seek(0);
        return flightFile.readInt();
    }
    public void setCounter(int count) throws IOException {
        flightFile.seek(0);
        flightFile.writeInt(count);
    }


    public String readFixString() throws IOException {
        String tpm = "";
        for (int i = 0; i < FIX_SIZE; i++) {
            tpm += flightFile.readChar();
        }
        return tpm.trim();
    }
    public String fixStringToWrite(String str) {
        while (str.length() < FIX_SIZE)
            str += " ";
        return str.substring(0, FIX_SIZE);
    }
}








