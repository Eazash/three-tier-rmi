package database;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatabaseHandlerInterface extends Remote {
    //Remote Database function declarations
    int isUser(String email, String password) throws RemoteException;
    int createUser(String username, String email, String password) throws RemoteException;

    void insertNote(int user_id, String noteContent) throws RemoteException;

    String[] getAllNotes(int user_id) throws RemoteException;
}
