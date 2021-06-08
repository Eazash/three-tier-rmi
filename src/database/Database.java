package database;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection conn;

    public Database(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to load Driver Class");
            System.exit(1);
        }
    }

    public static Connection getConn() {
        return conn;
    }
    public static void main(String []args){
        //initialize connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/notes_rmi", "rmidbserver" , "password");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }

        DatabaseHandlerImpl impl = new DatabaseHandlerImpl(getConn());
        try {
            DatabaseHandlerInterface stub = (DatabaseHandlerInterface) UnicastRemoteObject.exportObject(impl, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Registry Created");
            registry.rebind("database", stub);
            System.out.println("Database Stub bound");
        } catch (Exception e) {
            System.err.println("Unable to bind impl to rmi registry");
            e.printStackTrace();
        }
    }
}