package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotesInterface extends Remote {
    //function declaration for remote note manipulating functions
    int login(String email, String password) throws RemoteException;
    int signup(String username, String email, String password) throws RemoteException;
}
