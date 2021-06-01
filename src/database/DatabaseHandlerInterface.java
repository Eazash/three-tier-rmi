package database;

import java.rmi.Remote;

public interface DatabaseHandlerInterface extends Remote {
    //Remote Database function declarations
    boolean isUser(String email, String password);
    boolean createUser(String username, String email, String password);
}
