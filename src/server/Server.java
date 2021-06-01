package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        NotesImpl impl = new NotesImpl();
        try {
            NotesInterface stub = (NotesInterface) UnicastRemoteObject.exportObject(impl, 0);
            Registry registry = LocateRegistry.getRegistry(2000);
            registry.rebind("notes", stub);
        } catch (RemoteException e) {
            System.err.println("Unable to bind stub. Make sure Database class and subsequent rmiregistry are running");
            e.printStackTrace();
        }
    }
}
