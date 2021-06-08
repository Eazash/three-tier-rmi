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
    private static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        String name = "database";
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            notes = (NotesInterface) registry.lookup("notes");
//            System.out.println(Arrays.stream(registry.list()).reduce("", (acc, el)-> acc + el + "\n"));
        } catch (RemoteException e) {
            System.err.println("Unable to Connect to Registry");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.err.println("Server class not bound with name: 'notes'");
            e.printStackTrace();
            System.exit(1);
        }
        //Read Username and password
        int sel;
        System.out.println("-------------------------------------------------");
        boolean signin = false;
        boolean signup = false;
        do {
            System.out.println("1: Login\n2: Signup\n0: Exit Program");
            sel = Integer.parseInt(s.nextLine());
            switch (sel) {
                case 1: {
                    signin = login();
                    break;
                }
                case 2: {
                    signup = signup();
                    break;
                }
                case 0:{
                    System.out.println("Exiting!");
                    System.exit(0);
                }
                default: {
                    System.out.println("Invalid input");
                    break;
                }
            }
        } while (!signin && !signup);
        int choice;
        do {
            System.out.println("-------------------------------------------------");
            System.out.println("1: Add note\n2: View Notes\n3: Delete Note\n4: Delete all Notes\n0: Exit program");
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
                        System.out.println("-------------------------------------------------");
                        System.out.println(notes.getAllNotes(user_id));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter id of note to delete");
                    int id = Integer.parseInt(s.nextLine());
                    try {
                        notes.deleteNote(user_id, id);
                    } catch (RemoteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4: {
                    System.out.println("Deleting all notes");
                    try {
                        notes.clearNotes(user_id);
                    }catch (RemoteException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 0: {
                    System.out.println("Exiting!");
                    choice = 0;
                    break;
                }
                default: {
                    choice = -1;
                    break;
                }
            }
        } while (choice != 0);

    }

    private static boolean signup() {
        System.out.println("Enter username, email and password");
        System.out.print("username ");
        String username = s.nextLine();
        System.out.print("email ");
        String email = s.nextLine();
        System.out.print("Password ");
        String password = s.nextLine();

        try {
            user_id = notes.signup(username, email, password);
            System.out.println(user_id);
            return true;
        } catch (RemoteException e) {
            System.out.println("Unable to Signup");
            e.printStackTrace();
        }
        return false;
    }

    private static boolean login() {
        System.out.println("Enter email and password");
        Scanner s = new Scanner(System.in);
        System.out.print("email ");
        String email = s.nextLine();
        System.out.print("Password ");
        String password = s.nextLine();

        try {
            user_id = notes.login(email, password);
            System.out.println(user_id);
            return true;
        } catch (RemoteException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return false;
    }
}
