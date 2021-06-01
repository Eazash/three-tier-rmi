package database;

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
    public boolean isUser(String email, String password) {
        // TODO check password agianst hash (using salt) instead of direct string comp
        String queryUsersql = "SELECT `user_id` `password` FROM `users` where email=?";
        try {
            PreparedStatement queryUserPS = connection.prepareStatement(queryUsersql);
            queryUserPS.setString(1, email);
            ResultSet rs = queryUserPS.executeQuery();
            if(rs.next()){
                // user with provided email not found
                return false;
            }
            String storedPassword = rs.getString(2);
            // incorrect password
            return storedPassword.equals(password);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
    public boolean createUser(String username, String email, String password){
        // TODO make password more secure with salting and sha2 encryption
        String createUsersql = "INSERT INTO `users` (`username`, `email`, `password`) VALUES (?, ? , ?)";
        try {
            PreparedStatement createUserStatement = connection.prepareStatement(createUsersql);
            createUserStatement.setString(1, username);
            createUserStatement.setString(2, email);
            createUserStatement.setString(3, password);

            createUserStatement.executeUpdate();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
}
