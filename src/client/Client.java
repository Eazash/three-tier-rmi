package client;

import server.NotesInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private static NotesInterface notes;
    private static int user_id;

    public static void main(String[] args) {
        String name = "database";
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            notes = (NotesInterface) registry.lookup("notes");
//            System.out.println(Arrays.stream(registry.list()).reduce("", (acc, el)-> acc + el + "\n"));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.err.println("Notes class not bound with name: 'notes'");
            e.printStackTrace();
            System.exit(1);
        }
        //Read Username and password
        System.out.println("Enter email and password");
        Scanner s = new Scanner(System.in);
        System.out.print("email ");
        String email = s.nextLine();
        System.out.print("Password ");
        String password = s.nextLine();

        try {
            user_id = notes.login(email, password);
            System.out.println(user_id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        int choice;
        do {
            System.out.println("-------------------------------------------------");
            System.out.println("1: Add note\n2: View Notes\n0: Exit program");
            choice = Integer.parseInt(s.nextLine());
            switch (choice) {
                case 1: {
                    // Add a new note
                    System.out.println("Enter note content");
                    String note = s.nextLine();
                    System.out.println(note);
                    try {
                        notes.addNote(user_id, note);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    // Fetch all notes and display
                    try {
                        System.out.println(notes.getAllNotes(user_id));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 0: {
                    System.out.println("Goodbye!");
                    break;
                }
                default: {
                    choice = -1;
                    break;
                }
            }
        } while (choice != 0);

    }
}
