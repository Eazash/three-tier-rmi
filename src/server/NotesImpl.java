package server;

import database.DatabaseHandlerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

public class NotesImpl implements NotesInterface{
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
    public int login(String email, String password) {

        int userID = 0;
        try {
            userID = db.isUser(email, password);
        } catch (RemoteException e) {
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
    public void addNote(int user_id, String noteContent) {
        try {
            db.insertNote(user_id, noteContent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public String getAllNotes(int user_id){
        try {
            String [] notes = db.getAllNotes(user_id);
            return Arrays.stream(notes).reduce("", (acc, note)-> acc + "- " + note + "\n" );
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return "";
    }
}
