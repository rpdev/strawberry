package froapp3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

class Database {
	private static Database instance;
	private final Connection connection;
	private PreparedStatement insertBerries, insertPrices, getAllBerries, getAllPrices, deleteBerries, deletePrices;
	
	private interface DatabaseKeys{
		String getDatabaseType();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		new Database();
	}
	
	private enum Berries implements DatabaseKeys{
		ID("id INTEGER PRIMARY KEY"),
		NAME("name TEXT"),
		NUMBER("number INTEGER"),
		SOLD("sold INTEGER"),
		NON_SOLD("non_sold INTEGER"),
		PRICE("price INTEGER");
		
		private final String databaseType;
		
		private Berries(String databaseType){
			this.databaseType = databaseType;
		}

		@Override
		public String getDatabaseType() {
			return databaseType;
		}
	}
	
	private enum Prices implements DatabaseKeys{
		ID("id INTEGER PRIMARY KEY"),
		PRICE("price INTEGER"),
		BERRY_ID("berry_id INTEGER, FOREIGN KEY(berry_id) REFERENCES Berries(id) ON DELETE CASCADE");
		
		private final String databaseType;
		
		private Prices(String databaseType){
			this.databaseType = databaseType;
		}

		@Override
		public String getDatabaseType() {
			return databaseType;
		}
	}

	private Database() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:BerriesDatabase.db");
		
		Statement statement = connection.createStatement();
		statement.executeUpdate("DROP TABLE IF EXISTS "+Berries.class.getSimpleName());
		statement.executeUpdate("DROP TABLE IF EXISTS "+Prices.class.getSimpleName());		
		statement.executeUpdate(generateTable(Berries.class));
		statement.executeUpdate(generateTable(Prices.class));
		
		insertBerries = connection.prepareStatement(generateInsert(Berries.class));
		insertPrices = connection.prepareStatement(generateInsert(Prices.class));

		getAllBerries = connection.prepareStatement("SELECT * FROM "+Berries.class.getSimpleName());
		getAllPrices = connection.prepareStatement("SELECT * FROM "+Prices.class.getSimpleName());
		
		deleteBerries = connection.prepareStatement("DELETE FROM "+Berries.class.getSimpleName()+" WHERE "+Berries.ID.toString().toLowerCase()+" = ?");
		deleteBerries = connection.prepareStatement("DELETE FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.ID.toString().toLowerCase()+" = ?");
	}
	
	private String generateTable(Class<? extends Enum<? extends DatabaseKeys>> enums){
		StringBuilder sb = new StringBuilder("CREATE TABLE " + enums.getSimpleName() + "(");
		for (Object l : enums.getEnumConstants()){
			DatabaseKeys e =  (DatabaseKeys) l;
			sb.append(e.getDatabaseType() + ",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")");
		String returnValue = sb.toString();
		System.out.println(returnValue);
		return returnValue;
	}
	
	private String generateInsert(Class<? extends Enum<?>> enums){
		StringBuilder sb = new StringBuilder("INSERT INTO " + enums.getSimpleName() + "(");
		for (Object l : enums.getEnumConstants())
			sb.append(l.toString().toLowerCase() + ",");
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(") VALUES(");
		for (@SuppressWarnings("unused") Object l : enums.getEnumConstants())
			sb.append("?,");
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")");
		String returnValue = sb.toString();
		System.out.println(returnValue);
		return returnValue;
	}

	static Database getInstance() {
		if (instance == null) {
			try {
				instance = new Database();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	void addBerry(String name, int number, int sold, int nonSold, int price) {
		try {
			insertBerries.setString(Berries.NAME.ordinal(), name);
			insertBerries.setInt(Berries.NUMBER.ordinal(), number);
			insertBerries.setInt(Berries.SOLD.ordinal(), sold);
			insertBerries.setInt(Berries.NON_SOLD.ordinal(), nonSold);
			insertBerries.setInt(Berries.PRICE.ordinal(), price);
			insertBerries.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void addPrice(int price, int berry_id) {
		try {
			insertPrices.setInt(Prices.PRICE.ordinal(), price);
			insertPrices.setInt(Prices.BERRY_ID.ordinal(), berry_id);
			insertPrices.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	ArrayList<HashMap<String, String>> getAllBerries() {
		try {
			ResultSet set = getAllBerries.executeQuery();
			ArrayList<HashMap<String, String>> data = new ArrayList<>();
			while (set.next()) {
				HashMap<String, String> rowData = new HashMap<>();
				for (Berries l : Berries.values())
					rowData.put(l.toString(), set.getString(l.ordinal() + 1));
				data.add(rowData);
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	ArrayList<HashMap<String, String>> getAllPrices() {
		try {
			ResultSet set = getAllPrices.executeQuery();
			ArrayList<HashMap<String, String>> data = new ArrayList<>();
			while (set.next()) {
				HashMap<String, String> rowData = new HashMap<>();
				for (Prices l : Prices.values())
					rowData.put(l.toString(), set.getString(l.ordinal() + 1));
				data.add(rowData);
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	void updateBerry(int id, String name, Integer number, Integer sold, Integer nonSold, Integer price) {
		if (name != null) {

		}
		if (number != null) {

		}
		if (sold != null) {

		}
		if (nonSold != null) {

		}
		if (price != null) {

		}
	}
	
	void updatePrice(int id, Integer price, Integer berryId) {
		if (price != null) {

		}
		if (berryId != null) {

		}
	}

	void deleteBerryItem(int id) {
		try {
			deleteBerries.setInt(Berries.ID.ordinal() + 1, id);
			deleteBerries.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void deletePriceItem(int id) {
		try {
			deletePrices.setInt(Prices.ID.ordinal() + 1, id);
			deletePrices.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
