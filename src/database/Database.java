package database;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn;
    private static Registry registry;
    public Database(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to load Driver Class");
            System.exit(1);
        }
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/notes_rmi", "rmidbserver" , "password");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }

    public Connection getConn() {
        return conn;
    }
    public static void main(String []args){
        //initialize

        DatabaseHandlerImpl impl = new DatabaseHandlerImpl();
        try {
            DatabaseHandlerInterface stub = (DatabaseHandlerInterface) UnicastRemoteObject.exportObject(impl, 0);
            registry = LocateRegistry.createRegistry(2000);
            registry.rebind("database", stub);
        } catch (Exception e) {
            System.err.println("Unable to bind impl to rmi registry");
            e.printStackTrace();
        }
    }
}