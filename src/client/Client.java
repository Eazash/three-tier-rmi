package client;
import database.DatabaseHandlerImpl;
import database.DatabaseHandlerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        String name= "database";
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            System.out.println(Arrays.stream(registry.list()).reduce("", (acc, el)-> acc + el + "\n"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
