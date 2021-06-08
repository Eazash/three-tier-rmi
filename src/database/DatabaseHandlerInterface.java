package database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface DatabaseHandlerInterface extends Remote {
    //Remote Database function declarations
    String[] getUser(String email) throws RemoteException, SQLException;

    int createUser(String username, String email, String password) throws RemoteException;

    void insertNote(int user_id, String noteContent) throws RemoteException;

    String[][] getAllNotes(int user_id) throws RemoteException;

    void delete(int user_id, int id) throws RemoteException, SQLException;

    void clearNotes(int user_id) throws RemoteException, SQLException;
}
