package server;

import database.DatabaseHandlerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Arrays;

public class NotesImpl implements NotesInterface {
    //function definitions for remote functions for working with notes
    private DatabaseHandlerInterface db;

    public NotesImpl() {
        try {
            db = (DatabaseHandlerInterface) Server.registry.lookup("database");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("Database class not bound with name 'database'");
            e.printStackTrace();
        }
    }

    @Override
    public int login(String email, String password) throws RemoteException {

        int userID ;
        try {
            String[] user = db.getUser(email);
            if (user.length == 0 || !user[2].equals(password)) {
                throw new RemoteException("Invalid Credentials");
            } else {
                userID = Integer.parseInt(user[0]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Unable to login");
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

    public void addNote(int user_id, String noteContent) {
        try {
            db.insertNote(user_id, noteContent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getAllNotes(int user_id) {
        String output = "";
        try {
            String[][] notes = db.getAllNotes(user_id);
            for (String[] note : notes) {
                output += String.format("%d - %s\n", Integer.parseInt(note[0]), note[1]);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public void deleteNote(int user_id, int id) throws RemoteException {
        try {
            db.delete(user_id, id);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void clearNotes(int user_id) throws RemoteException {
        try {
            db.clearNotes(user_id);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
