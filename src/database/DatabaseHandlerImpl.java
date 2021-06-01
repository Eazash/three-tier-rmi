package database;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandlerImpl implements DatabaseHandlerInterface{
    //implementations for remote database functions
    private final Connection connection;
    public DatabaseHandlerImpl(Connection conn) {
        this.connection = conn;
    }

    @Override
    public int isUser(String email, String password) throws RemoteException {
        // TODO check password agianst hash (using salt) instead of direct string comp
        String queryUsersql = "SELECT `user_id` `password` FROM `users` where email=?";
        try {
            PreparedStatement queryUserPS = connection.prepareStatement(queryUsersql);
            queryUserPS.setString(1, email);
            ResultSet rs = queryUserPS.executeQuery();
            if(rs.next()){
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
}
