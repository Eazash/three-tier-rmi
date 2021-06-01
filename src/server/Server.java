package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static Registry registry;
    public static void main(String[] args) {
        try {
            registry = LocateRegistry.getRegistry(1099);
        } catch (RemoteException e) {
            System.err.println("Unable to connect to registry");
            e.printStackTrace();
            System.exit(1);
        }
        NotesImpl impl = new NotesImpl();
        try {
            NotesInterface stub = (NotesInterface) UnicastRemoteObject.exportObject(impl, 0);
            registry.rebind("notes", stub);
        } catch (RemoteException e) {
            System.err.println("Unable to bind stub. Make sure Database class and subsequent rmiregistry are running");
            e.printStackTrace();
        }
    }
}
