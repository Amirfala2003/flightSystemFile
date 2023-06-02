import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class UserFile {
    private RandomAccessFile userFile;
    private final int FIX_SIZE = 20;

    public UserFile() throws FileNotFoundException {
        this.userFile = new RandomAccessFile("userFile.dat","rw");
    }

    public void writeUserInFile(User user) throws IOException {
        userFile.writeChars(fixStringToWrite(user.getUsername()));
        userFile.writeChars(fixStringToWrite(user.getPassword()));
        userFile.writeInt(user.getCharge());

    }

    public RandomAccessFile getUserFile() {
        return userFile;
    }

    public int getCounter() throws IOException {
        userFile.seek(0);
        return userFile.readInt();
    }
    public void setCounter(int count) throws IOException {
        userFile.seek(0);
        userFile.writeInt(count);
    }
    public User readUserFromFile() throws IOException {
        String username = readFixString();
        String password = readFixString();
        int charge = userFile.readInt();
        return new User(username,password);
    }
    public User findUserFromFile(String username) throws IOException {
        for (int i = 0; i < userFile.length()/84; i++) {
            int seek = i*84;
            userFile.seek(seek);
            if (username.equals(readFixString())){
                userFile.seek(seek);
                return readUserFromFile();
            }
        }
        return null;
    }
//    public User signUp(String username , String password) throws IOException {
//        if (findUserFromFile(username)!=null){
//            return null;
//        }
//        User user = new User(username , password);
//        userFile.seek(userFile.length());
//        writeUserInFile(user);
//
//
//    }
    public void setCharge(User user,int flightCharge) throws IOException {
        for (int i = 0; i < userFile.length()/84; i++) {
            int seek = i*84;
            userFile.seek(seek);
            if (user.getUsername().equals(readFixString())){
                userFile.seek(seek);
                user.setCharge(user.getCharge()+flightCharge);
                writeUserInFile(user);
            }
        }

    }
    public void changePassword(User user,String password) throws IOException {
        for (int i = 0; i < userFile.length()/84; i++) {
            int seek = i*84;
            userFile.seek(seek);
            if (user.getUsername().equals(readFixString())){
                userFile.seek(seek);
                user.setPassword(password);
                writeUserInFile(user);
            }
        }
    }



    public String readFixString() throws IOException {
        String tpm = "";
        for (int i = 0; i < FIX_SIZE; i++) {
            tpm += userFile.readChar();
        }
        return tpm.trim();
    }

    public String fixStringToWrite(String str) {
        while (str.length() < FIX_SIZE)
            str += " ";
        return str.substring(0, FIX_SIZE);
    }
}
