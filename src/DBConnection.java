

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 *
 * @author mattr
 */
public class DBConnection {
    private static Connection connection;
    private static final String user = "";
    private static final String password = "";
    private static final String database = "jdbc:derby:C:courseSchedulerDB;create=true;";
    static boolean databaseAlreadyExists = true;

    public static Connection getConnection()
    {
        if (connection == null)
        {
            try{
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            
            try
            {
                connection = DriverManager.getConnection(database, user, password);
            } catch (SQLException e)
            {
                e.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);

            }
            
            try{
                DatabaseMetaData dbm = connection.getMetaData();
                // check if "employee" table is there
                ResultSet tables = dbm.getTables(null, null, "COURSE", null);
                if (tables.next()) {
                  // Table exists
                  databaseAlreadyExists = true;
                  System.out.println("Database already exists");
                }
                else {
                    databaseAlreadyExists = false;
                  // Table does not exist
                } 
            } catch(SQLException e){
                e.printStackTrace();
                System.out.println("Could not access database to check for existing tables.");
                System.exit(1);
                    
            }
            
            if(databaseAlreadyExists==false){
                try(Connection connection = DriverManager.getConnection(database, user, password);
                    Statement stmt = connection.createStatement();)
                {
                    String sql = "CREATE TABLE APP.COURSE " +
                       "(SEMESTER VARCHAR(30) not NULL, " +
                       " COURSECODE VARCHAR(20) not NULL, " + 
                       " DESCRIPTION VARCHAR(20), " + 
                       " SEATS INTEGER, " + 
                       " PRIMARY KEY ( SEMESTER, COURSECODE ))"; 
                    stmt.executeUpdate(sql);
                    sql = "CREATE TABLE APP.SCHEDULE " +
                       "(SEMESTER VARCHAR(30) not NULL, " +
                       " STUDENTID VARCHAR(20) not NULL, " +
                       " COURSECODE VARCHAR(20) not NULL, " + 
                       " STATUS VARCHAR(1), " + 
                       " TIMESTAMP TIMESTAMP, " + 
                       " PRIMARY KEY ( SEMESTER, STUDENTID, COURSECODE ))"; 
                    stmt.executeUpdate(sql);
                    sql = "CREATE TABLE APP.SEMESTER " +
                       "(SEMESTER VARCHAR(30) not NULL PRIMARY KEY )"; 
                    stmt.executeUpdate(sql);
                    sql = "CREATE TABLE APP.STUDENT " +
                       "(STUDENTID VARCHAR(20) not NULL PRIMARY KEY, " +
                       " FIRSTNAME VARCHAR(20), " + 
                       " LASTNAME VARCHAR(20))"; 
                    stmt.executeUpdate(sql);

                } catch (SQLException e)
                {
                    e.printStackTrace();
                    System.out.println("Could not correctly configure database.");
                    System.exit(1);

                }
            }
        }
        return connection;
    }

    
}
