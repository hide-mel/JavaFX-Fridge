import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class FridgeDSC {

	// the date format we will be using across the application
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	/*
		FREEZER, // freezing cold
		MEAT, // MEAT cold
		COOLING, // general fridge area
		CRISPER // veg and fruits section

		note: Enums are implicitly public static final
	*/
	public enum SECTION {
		FREEZER,
		MEAT,
		COOLING,
		CRISPER
	};

	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement preparedStatement;

	public static void connect() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");


			/* TODO 1-01 - TO COMPLETE ****************************************
			 * change the value of the string for the following 3 lines:
			 * - url
			 * - user
			 * - password
			 */
			//String url = "jdbc:mysql://localhost:3306/fridgedb";
			//String user = "root";
			//String password = "1234";
			String url = "jdbc:mysql://latcs7.cs.latrobe.edu.au:3306/18352018";
			String user = "18352018";
			String password = "pJKZ6n6dkaP3XtWVXHG4";

			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
  		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public static void disconnect() throws SQLException {
		if(preparedStatement != null) preparedStatement.close();
		if(statement != null) statement.close();
		if(connection != null) connection.close();
	}



	public Item searchItem(String name) throws Exception {
		String queryString = "SELECT * FROM item WHERE name = ?";


		/* TODO 1-02 - TO COMPLETE ****************************************
		 * - preparedStatement to add argument name to the queryString
		 * - resultSet to execute the preparedStatement query
		 * - iterate through the resultSet result
		 */
		//connect();

		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setString(1, name);
		ResultSet rs = preparedStatement.executeQuery();

		Item item = null;

		if (rs.next()) { // i.e. the item exists

			/* TODO 1-03 - TO COMPLETE ****************************************
			 * - if resultSet has result, get data and create an Item instance
			 */
			item = new Item(
				rs.getString("name"),
				rs.getBoolean("expires")
				);

		}

		//disconnect();

		return item;
	}

	public Grocery searchGrocery(int id) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String queryString = "SELECT * FROM grocery WHERE id = ?";

		/* TODO 1-04 - TO COMPLETE ****************************************
		 * - preparedStatement to add argument name to the queryString
		 * - resultSet to execute the preparedStatement query
		 * - iterate through the resultSet result
		 */
		//connect();

		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();

		Grocery grocery = null;
		SECTION section = null;

		if (rs.next()) { // i.e. the grocery exists

			/* TODO 1-05 - TO COMPLETE ****************************************
			 * - if resultSet has result, get data and create a Grocery instance
			 * - making sure that the item name from grocery exists in
			 *   item table (use searchItem method)
			 * - pay attention about parsing the date string to LocalDate
			 */
			Item tmp = searchItem(rs.getString("itemName"));
			boolean pre = (tmp != null);

			if (!pre) {
				String msg = "Item name " + rs.getString("itemName") + " does not exist!";
            	System.out.println("\nERROR: " + msg);
            	throw new Exception(msg);
			}

			switch(rs.getString("section")){
				case "FREEZER": section = SECTION.FREEZER; break;
  				case "MEAT": section =  SECTION.MEAT; break;
      			case "COOLING": section = SECTION.COOLING; break;
      			case "CRISPER": section = SECTION.CRISPER; break;
			}
			grocery = new Grocery(
				rs.getInt("id"),
				tmp,
				LocalDate.parse(rs.getString("date"), (DateTimeFormatter)DateTimeFormatter.ofPattern(DATE_FORMAT)),
				rs.getInt("quantity"),
				section
				);
		}
		//disconnect();

		return grocery;
	}

	public List<Item> getAllItems() throws Exception {
		String queryString = "SELECT * FROM item";

		/* TODO 1-06 - TO COMPLETE ****************************************
		 * - resultSet to execute the statement query
		 */
		//connect();

		preparedStatement = connection.prepareStatement(queryString);
        ResultSet rs = preparedStatement.executeQuery();

		List<Item> items = new ArrayList<Item>();

		/* TODO 1-07 - TO COMPLETE ****************************************
		 * - iterate through the resultSet result, create intance of Item
		 *   and add to list items
		 */
		Item tmp;

		while (rs.next())  {
			tmp = new Item(
				rs.getString("name"),
				rs.getBoolean("expires")
				);

			items.add(tmp);
		}

		//disconnect();

		return items;
	}

	public List<Grocery> getAllGroceries() throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String queryString = "SELECT * FROM grocery";

		/* TODO 1-08 - TO COMPLETE ****************************************
		 * - resultSet to execute the statement query
		 */
		//connect();
		preparedStatement = connection.prepareStatement(queryString);
        ResultSet rs = preparedStatement.executeQuery();

		List<Grocery> groceries = new ArrayList<Grocery>();

		/* TODO 1-09 - TO COMPLETE ****************************************
		 * - iterate through the resultSet result, create intance of Item
		 *   and add to list items
		 * - making sure that the item name from each grocery exists in
		 *   item table (use searchItem method)
		 * - pay attention about parsing the date string to LocalDate
		 */
		Grocery tmp;
		SECTION section = null;
		while (rs.next()) {
			Item temp = searchItem(rs.getString("itemName"));
			boolean pre = (temp != null);

			if (!pre) {
				String msg = "Item name " + rs.getString("itemName") + " does not exist!";
            	System.out.println("\nERROR: " + msg);
            	throw new Exception(msg);
			}

			switch(rs.getString("section")){
				case "FREEZER": section = SECTION.FREEZER; break;
      			case "MEAT": section = SECTION.MEAT; break;
      			case "COOLING": section = SECTION.COOLING; break;
      			case "CRISPER": section = SECTION.CRISPER; break;
			}
			tmp = new Grocery(
				rs.getInt("id"),
				temp,
				LocalDate.parse(rs.getString("date"), (DateTimeFormatter)DateTimeFormatter.ofPattern(DATE_FORMAT)),
				rs.getInt("quantity"),
				section
				);

			groceries.add(tmp);
		}

		//disconnect();

		return groceries;
	}


	public int addGrocery(String name, int quantity, SECTION section) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate date = LocalDate.now();
		String dateStr = date.format(dtf);

		// NOTE: should we check if itemName (argument name) exists in item table?
		//		--> adding a groceries with a non-existing item name should through an exception

		//not sure need to be added...............................................................
		Item tmp = searchItem(name);
		boolean pre = (tmp != null);
		String sectionString = null;

		if (!pre) {
			String msg = "Item name " + name + " does not exist!";
            System.out.println("\nERROR: " + msg);
            throw new Exception(msg);
		}

		//String command = "INSERT INTO grocery VALUES(?, ?, ?, ?, ?)";
		String command = "INSERT INTO grocery (itemName, date, quantity, section) VALUES(?, ?, ?, ?)";

		/* TODO 1-10 - TO COMPLETE ****************************************
		 * - preparedStatement to add arguments to the queryString
		 * - resultSet to executeUpdate the preparedStatement query
		 */
		//connect();

		switch(section){
					case FREEZER: sectionString = "FREEZER"; break;
      				case MEAT: sectionString = "MEAT"; break;
      				case COOLING: sectionString = "COOLING"; break;
      				case CRISPER: sectionString = "CRISPER"; break;
				}

		preparedStatement = connection.prepareStatement(command);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, dateStr);
        preparedStatement.setInt(3, quantity);
        preparedStatement.setString(4, sectionString);
        preparedStatement.executeUpdate();

        //disconnect();


		// retrieving & returning last inserted record id
		ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
		rs.next();
		int newId = rs.getInt(1);

		return newId;
	}

	public Grocery useGrocery(int id) throws Exception {

		/* TODO 1-11 - TO COMPLETE ****************************************
		 * - search grocery by id
		 * - check if has quantity is greater one; if not throw exception
		 *   with adequate error message
		 */
		Grocery tmp = searchGrocery(id);
		boolean pre = (tmp.getQuantity() > 1);

		if (!pre) {
            throw new Exception("java.lang.Exception");
		}


		String queryString =
			"UPDATE grocery " +
			"SET quantity = quantity - 1 " +
			"WHERE quantity > 1 " +
			"AND id = " + id + ";";


		/* TODO 1-12 - TO COMPLETE ****************************************
		 * - statement execute update on queryString
		 * - should the update affect a row search grocery by id and
		 *   return it; else throw exception with adequate error message
		 *
		 * NOTE: method should return instance of grocery
		 */
		Integer rs = statement.executeUpdate(queryString);
		Grocery grocery = searchGrocery(id);

		return grocery;
	}

	public int removeGrocery(int id) throws Exception {
		//String queryString = "DELETE FROM grocery WHERE id = " + id + ";";
		String queryString = "DELETE FROM grocery WHERE id = ?";

		/* TODO 1-13 - TO COMPLETE ****************************************
		 * - search grocery by id
		 * - if grocery exists, statement execute update on queryString
		 *   return the value value of that statement execute update
		 * - if grocery does not exist, throw exception with adequate
		 *   error message
		 *
		 * NOTE: method should return int: the return value of a
		 *		 stetement.executeUpdate(...) on a DELETE query
		 */
		Grocery tmp = searchGrocery(id);
		boolean pre = (tmp != null);

		if (!pre) {
			String msg = "Grocery id " + id + " does not exist!";
            System.out.println("\nERROR: " + msg);
            throw new Exception(msg);
		}

		//ResultSet rs = statement.executeQuery(queryString);
		//rs.next();
		preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setInt(1, id);
        Integer rs = preparedStatement.executeUpdate();

		return id;
	}

	// STATIC HELPERS -------------------------------------------------------

	public static long calcDaysAgo(LocalDate date) {
    	return Math.abs(Duration.between(LocalDate.now().atStartOfDay(), date.atStartOfDay()).toDays());
	}

	public static String calcDaysAgoStr(LocalDate date) {
    	String formattedDaysAgo;
    	long diff = calcDaysAgo(date);

    	if (diff == 0)
    		formattedDaysAgo = "today";
    	else if (diff == 1)
    		formattedDaysAgo = "yesterday";
    	else formattedDaysAgo = diff + " days ago";

    	return formattedDaysAgo;
	}

	// To perform some quick tests
	public static void main(String[] args) throws Exception {
		FridgeDSC myFridgeDSC = new FridgeDSC();

		myFridgeDSC.connect();

		System.out.println("\nSYSTEM:\n");

		System.out.println("\n\nshowing all of each:");
		System.out.println(myFridgeDSC.getAllItems());
		System.out.println(myFridgeDSC.getAllGroceries());

		int addedId = myFridgeDSC.addGrocery("Milk", 40, SECTION.COOLING);
		System.out.println("added: " + addedId);
		System.out.println("deleting " + (addedId - 1) + ": " + (myFridgeDSC.removeGrocery(addedId - 1) > 0 ? "DONE" : "FAILED"));
		System.out.println("using " + (addedId) + ": " + myFridgeDSC.useGrocery(addedId));
		System.out.println(myFridgeDSC.searchGrocery(addedId));

		myFridgeDSC.disconnect();
	}
}

//error FridgeDSC section  = switch... (enum..)
//addGrocery check if already exist name?
//LocalDate.parse? correct format?
//Class.forName("com.mysql.cj.jdbc.Driver");?
//not initiated?