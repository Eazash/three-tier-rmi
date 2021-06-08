package database;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.Vector;

public class DatabaseHandlerImpl implements DatabaseHandlerInterface {
    //implementations for remote database functions
    private final Connection connection;

    public DatabaseHandlerImpl(Connection conn) {
        this.connection = conn;
    }

    @Override
    public String[] getUser(String email) throws RemoteException, SQLException {
        // TODO check password agianst hash (using salt) instead of direct string comp
        String queryUsersql = "SELECT * FROM `users` where email=?";
            PreparedStatement queryUserPS = connection.prepareStatement(queryUsersql);
            queryUserPS.setString(1, email);
            ResultSet rs = queryUserPS.executeQuery();
            if (!rs.next()) {
                // user with provided email not found
                // TODO throw errors instead of 0
                return new String[]{};
            }
            return new String[]{rs.getString(1), rs.getString(2), rs.getString(4)};
    }

    public int createUser(String username, String email, String password) throws RemoteException {
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
    public String[][] getAllNotes(int user_id) throws RemoteException {
        String sql = "SELECT * FROM `notes` WHERE `user_id`=?";
        try {
            PreparedStatement getAllNotesStatement = connection.prepareStatement(sql);
            getAllNotesStatement.setInt(1, user_id);
            ResultSet rs = getAllNotesStatement.executeQuery();
            Vector<String[]> notes = new Vector<>();
            while (rs.next()) {
                notes.add(new String[]{rs.getString(1), rs.getString(2)});
            }
            return notes.toArray(new String[notes.size()][]);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return new String[][]{};
    }

    @Override
    public void delete(int user_id, int note_id) throws RemoteException, SQLException {
        String sql = "DELETE FROM `notes` where `user_id`=? and `note_id`=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, user_id);
        ps.setInt(2, note_id);
        ps.executeUpdate();
    }

    @Override
    public void clearNotes(int user_id) throws RemoteException, SQLException {
        String sql = "DELETE FROM `notes`";
        Statement st = connection.createStatement();
        st.executeUpdate(sql);
    }


}
