package database;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DatabaseHandlerImpl implements DatabaseHandlerInterface{
    //implementations for remote database functions
    private final Connection connection;
    public DatabaseHandlerImpl(Connection conn) {
        this.connection = conn;
    }

    @Override
    public int isUser(String email, String password) throws RemoteException {
        // TODO check password agianst hash (using salt) instead of direct string comp
        String queryUsersql = "SELECT `user_id`, `password` FROM `users` where email=?";
        try {
            PreparedStatement queryUserPS = connection.prepareStatement(queryUsersql);
            queryUserPS.setString(1, email);
            ResultSet rs = queryUserPS.executeQuery();
            if(!rs.next()){
                // user with provided email not found
                // TODO throw errors instead of 0
                return 0;
            }
            String storedPassword = rs.getString(2);
            // incorrect password
            if(storedPassword.equals(password)){
                return rs.getInt(1);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }
    public int createUser(String username, String email, String password) throws RemoteException{
        // TODO make password more secure with salting and sha2 encryption
        String createUsersql = "INSERT INTO `users` (`username`, `email`, `password`) VALUES (?, ? , ?)";
        try {
            PreparedStatement createUserStatement = connection.prepareStatement(createUsersql, new String[]{"user_id"});
            createUserStatement.setString(1, username);
            createUserStatement.setString(2, email);
            createUserStatement.setString(3, password);

            createUserStatement.executeUpdate();
            ResultSet rs = createUserStatement.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    @Override
    public void insertNote(int user_id, String noteContent) throws RemoteException {
        String sql = "INSERT INTO `notes` (`user_id`, `content`) VALUES (?, ?)";
        try {
            PreparedStatement insertNoteStatement = connection.prepareStatement(sql);
            insertNoteStatement.setInt(1, user_id);
            insertNoteStatement.setString(2, noteContent);
            insertNoteStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public String[] getAllNotes(int user_id) throws RemoteException {
        String sql = "SELECT `content` FROM `notes` WHERE `user_id`=?";
        try {
            PreparedStatement getAllNotesStatement = connection.prepareStatement(sql);
            getAllNotesStatement.setInt(1, user_id);
            ResultSet rs = getAllNotesStatement.executeQuery();
            Vector<String> notes = new Vector<>();
            while (rs.next()){
                notes.add(rs.getString(1));
            }
            return notes.toArray(new String[notes.size()]);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return new String[0];
    }

}
