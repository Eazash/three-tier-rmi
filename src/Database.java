import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn;
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
    public void main(){
        //initialize
    }
}