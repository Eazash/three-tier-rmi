package server;

import database.DatabaseHandlerImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class NotesImpl implements NotesInterface{
    //function definitions for remote functions for working with notes
    private static DatabaseHandlerImpl db;
    @Override
    public int login(String email, String password) {

        int userID = 0;
        try {
            DatabaseHandlerImpl db = (DatabaseHandlerImpl) Server.registry.lookup("database");
            userID = db.isUser(email, password);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return userID;
    }

    @Override
    public int signup(String username, String email, String password) {
        int userId = 0;
        try {
            userId = db.createUser(username, email, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
