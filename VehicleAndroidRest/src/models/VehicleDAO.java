package models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VehicleDAO {
	
	/*Connection to DB
	 */
	private static Connection getDBConnection() {
		Connection connection = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			String dbURL = "jdbc:sqlite:vehicles.sqlite";
			connection = DriverManager.getConnection(dbURL);
			return connection;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			
		}
		return connection;
	}
	
	/*Gets all vehicles in DB
	 * Returns array
	 */
	public ArrayList<Vehicle> getAllVehicles() throws SQLException
	{
		Connection dbConnection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = "SELECT * FROM vehicles";
		
		ArrayList<Vehicle> vehicleList = new ArrayList<>();
		
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			System.out.println("DBQuery = " + query);
			result = statement.executeQuery(query);
			
			//Loops through each row of table, stores each as vehicle object, adds to array
			while(result.next()) {
				int vehicle_id = result.getInt("vehicle_id");
				String make = result.getString("make");
				String model = result.getString("model");
				int year = result.getInt("year");
				int price = result.getInt("price");
				String license_number = result.getString("license_number");
				String colour = result.getString("colour");
				int number_doors = result.getInt("number_doors");
				String transmission = result.getString("transmission");
				int mileage = result.getInt("mileage");
				String fuel_type = result.getString("fuel_type");
				int engine_size = result.getInt("engine_size");
				String body_style = result.getString("body_style");
				String condition = result.getString("condition");
				String notes = result.getString("notes");
				
				vehicleList.add(new Vehicle(vehicle_id, make, model, year, price,
						   license_number, colour, number_doors, transmission,
						   mileage, fuel_type, engine_size, body_style,
						   condition, notes));
				
			}
		} finally {
			//Close all opened connections to DB
			if (result != null) { result.close(); }
			if (statement != null) { result.close(); }
			if (dbConnection != null) { dbConnection.close(); }
		}
		
		return vehicleList;
		
	}
	
	/*Outputs one vehicle given vehicle_id
	 * Returns Vehicle
	 */
	public Vehicle getVehicle(int vehicle_id_IN) throws SQLException 
	{
		Connection dbConnection = null;
		Statement statement = null;
		ResultSet result = null;
		Vehicle vehicleOut = null;
		String query = "SELECT * FROM vehicles WHERE vehicle_id =" + vehicle_id_IN + ";";
		
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			System.out.println("DBQuery = " + query);
			result = statement.executeQuery(query);
			
			while(result.next()) 
			{
				int vehicle_id = result.getInt("vehicle_id");
				String make = result.getString("make");
				String model = result.getString("model");
				int year = result.getInt("year");
				int price = result.getInt("price");
				String license_number = result.getString("license_number");
				String colour = result.getString("colour");
				int number_doors = result.getInt("number_doors");
				String transmission = result.getString("transmission");
				int mileage = result.getInt("mileage");
				String fuel_type = result.getString("fuel_type");
				int engine_size = result.getInt("engine_size");
				String body_style = result.getString("body_style");
				String condition = result.getString("condition");
				String notes = result.getString("notes");
				
				vehicleOut = new Vehicle(vehicle_id, make, model, year, price,
						   license_number, colour, number_doors, transmission,
						   mileage, fuel_type, engine_size, body_style,
						   condition, notes);
			}
			
		} finally {
			if (result != null) { result.close(); }
			if (statement != null) { result.close(); }
			if (dbConnection != null) { dbConnection.close(); }
		}
		
		return vehicleOut;
		
	}
	
	/*Deletes a vehicle given vehicle_id
	 *Returns true if deleted
	 */
	public Boolean deleteVehicle(int vehicle_id_IN) throws SQLException
	{
		Connection dbConnection = null;
		String query = "DELETE FROM vehicles WHERE vehicle_id = ?";
		Boolean bool = false;
		
		try {
			dbConnection = getDBConnection();
			PreparedStatement pstatement = dbConnection.prepareStatement(query);
			pstatement.setInt(1, vehicle_id_IN);
			pstatement.executeUpdate();
			System.out.println("Vehicle " + vehicle_id_IN + " has been deleted!");
			bool = true;
			
		} finally {
			if (dbConnection != null) { dbConnection.close(); }
		}
		
		return bool;
		
	}
	
	/* Inserts passed through vehicle into DB
	 * Returns true if inserted
	 */
	public Boolean insertVehicle(Vehicle v) throws SQLException
	{
		Connection dbConnection = null;
		String query = "INSERT INTO vehicles(vehicle_id, make, model, year, price," + 
				"				   license_number, colour, number_doors, transmission," + 
				"				   mileage, fuel_type, engine_size, body_style," + 
				"				   condition, notes) " +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Boolean bool = false;
		
		try {
			dbConnection = getDBConnection();
			PreparedStatement pstatement = dbConnection.prepareStatement(query);
			
			pstatement.setInt(1, v.get_vehicle_ID());
			pstatement.setString(2, v.get_make());
			pstatement.setString(3, v.get_model());
			pstatement.setInt(4, v.get_year());
			pstatement.setInt(5, v.get_price());
			pstatement.setString(6, v.get_license_number());
			pstatement.setString(7, v.get_colour());
			pstatement.setInt(8, v.get_number_doors());
			pstatement.setString(9, v.get_transmission());
			pstatement.setInt(10, v.get_mileage());
			pstatement.setString(11, v.get_fuel_type());
			pstatement.setInt(12, v.get_engine_size());
			pstatement.setString(13, v.get_body_style());
			pstatement.setString(14, v.get_condition());
			pstatement.setString(15, v.get_notes());
			pstatement.executeUpdate();
			System.out.println("Vehicle " + v.get_vehicle_ID() + " has been added to the database!");
			bool = true;
		
			
		} finally {
			if (dbConnection != null) { dbConnection.close(); }
		}
		
		return bool;
	}
	
	/* Updates passed through vehicle in DB given by vehicle_id
	 * Returns true if updated
	 */
	public boolean updateVehicle(Vehicle v, int vehicle_id) throws SQLException
	{
		Connection dbConnection = null;
		String query = "UPDATE vehicles SET make = ?, model = ?, year = ?, price = ?,license_number = ?, colour = ?"
				+ ", number_doors = ?, transmission = ?, mileage = ?, fuel_type = ?, engine_size = ?, body_style =?,"
				+ " condition = ?, notes = ? WHERE vehicle_ID = " + vehicle_id;
		Boolean bool = false;
		
		try {
			dbConnection = getDBConnection();
			PreparedStatement pstatement = dbConnection.prepareStatement(query);
			
			pstatement.setString(1, v.get_make());
			pstatement.setString(2, v.get_model());
			pstatement.setInt(3, v.get_year());
			pstatement.setInt(4, v.get_price());
			pstatement.setString(5, v.get_license_number());
			pstatement.setString(6, v.get_colour());
			pstatement.setInt(7, v.get_number_doors());
			pstatement.setString(8, v.get_transmission());
			pstatement.setInt(9, v.get_mileage());
			pstatement.setString(10, v.get_fuel_type());
			pstatement.setInt(11, v.get_engine_size());
			pstatement.setString(12, v.get_body_style());
			pstatement.setString(13, v.get_condition());
			pstatement.setString(14, v.get_notes());
			pstatement.executeUpdate();
			System.out.println("Vehicle " + v.get_vehicle_ID() + " has been updated!");
			bool = true;
			
		} catch (SQLException c) {
			System.out.print(c.getMessage());
		} finally {
			if (dbConnection != null) { dbConnection.close(); }
		}
		
		return bool;
	}

	/* Gets the value of vehicle_id of last row in DB
	 * Returns value of last vehicle_id in table
	 */
	public int getNextVehicle_ID() throws SQLException
	{
		Connection dbConnection = null;
		Statement statement = null;
		ResultSet result = null;
		String query = "SELECT * FROM vehicles ORDER BY vehicle_id DESC LIMIT 1";
		int numOfRows = 0;
		
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			result = statement.executeQuery(query);
			
			while(result.next()) {
				numOfRows = result.getInt(1);
			}
			
		} catch (SQLException c) {
			System.out.print(c.getMessage());
		} finally {
			if (result != null) { result.close(); }
			if (statement != null) { result.close(); }
			if (dbConnection != null) { dbConnection.close(); }
		}
		
		return numOfRows;
	}
}
