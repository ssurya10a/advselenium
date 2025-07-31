package generateUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver; // MySQL JDBC driver

public class DatabaseUtility {

	public Connection con; // Global connection object

	// Method 1: Fetch data from DB using dynamic connection details
	public ResultSet FetchDataFromDataBase(String url, String id, String pass, String queary) throws SQLException {

		Driver diver = new Driver(); // Create MySQL driver object
		DriverManager.registerDriver(diver); // Register the driver

		con = DriverManager.getConnection(url, id, pass); // Establish connection to database

		Statement stat = con.createStatement(); // Create statement for executing queries

		ResultSet result = stat.executeQuery(queary); // Execute SELECT query and store result
		return result; // Return ResultSet
	}

	// Method 2: Fetch data using default DB connection credentials
	public ResultSet FetchDataFromDataBase(String queary) throws SQLException {

		Driver diver = new Driver(); // Registering the MySQL driver
		DriverManager.registerDriver(diver);

		// Connect using default URL, username, and password
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/selproject", "root", "tiger");

		Statement stat = con.createStatement(); // Create SQL statement
		ResultSet result = stat.executeQuery(queary); // Execute SELECT query
		return result; // Return result set
	}

	// Method 3: Write/update data into DB (INSERT, UPDATE, DELETE)
	public int WriteBackDataToDataBase(String queary) throws SQLException {

		Driver diver = new Driver(); // Create and register driver
		DriverManager.registerDriver(diver);

		// Establish default connection
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/selproject", "root", "tiger");

		Statement stat = con.createStatement(); // Create statement
		int result = stat.executeUpdate(queary); // Execute update/insert/delete query
		return result; // Return number of affected rows
	}

	// Method 4: Close the database connection
	public void CloseDataBase() throws SQLException {
		con.close(); // Close connection to release DB resources
	}

	// Method 4: Connect to the database connection
	public Connection openDataBase() throws SQLException {
		// Get the connection
		Driver driver = new Driver();
		DriverManager.registerDriver(driver);
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/selproject", "root", "tiger");
		return con;
	}

}
