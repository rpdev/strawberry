package froapp3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

class Database {
	private static Database instance;
	private final Connection connection;
	private PreparedStatement insertBerries, insertPrices, getAllBerries, getAllPrices, deleteBerries, deletePrices;
	
	private interface DatabaseKeys{
		String getDatabaseType();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Database d = Database.getInstance();
		d.addBerry("test", 0, 0, 0, 0);
		d.updateBerry(1, null, null, 2, 1, 3);
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.addPrice(100, 1);
		d.updatePrice(1, 333, null);
		d.deleteBerryItem(1);
		d.printTable(Berries.class);
		d.printTable(Prices.class);
	}
	
	private void printTable(Class<? extends Enum<?>> table){
		ArrayList<LinkedHashMap<String, String>> list = null;
		if(table == Berries.class){
			list = getAllBerries();
			System.out.println("BERRIES");
		} else if(table == Prices.class){
			list = getAllPrices();
			System.out.println("PRICES");
		}
		for(HashMap<String, String> a : list){
			for(Entry<String, String> e : a.entrySet())
				System.out.print(e + " ");
			System.out.println();
		}		
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
		connection.createStatement().execute("PRAGMA foreign_keys=ON;");
		
		Statement statement = connection.createStatement();
		statement.executeUpdate("DROP TABLE IF EXISTS "+Berries.class.getSimpleName());
		statement.executeUpdate("DROP TABLE IF EXISTS "+Prices.class.getSimpleName());		
		statement.executeUpdate(generateTable(Berries.class));
		statement.executeUpdate(generateTable(Prices.class));
		
		insertBerries = connection.prepareStatement(generateInsert(Berries.class, Berries.ID));
		insertPrices = connection.prepareStatement(generateInsert(Prices.class, Prices.ID));

		getAllBerries = connection.prepareStatement("SELECT * FROM "+Berries.class.getSimpleName());
		getAllPrices = connection.prepareStatement("SELECT * FROM "+Prices.class.getSimpleName());
		
		deleteBerries = connection.prepareStatement("DELETE FROM "+Berries.class.getSimpleName()+" WHERE "+Berries.ID.toString().toLowerCase()+" = ?");
		deletePrices = connection.prepareStatement("DELETE FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.ID.toString().toLowerCase()+" = ?");
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
	
	private String generateInsert(Class<? extends Enum<?>> enums, Enum<?>... ignore){
		Arrays.sort(ignore);
		StringBuilder sb = new StringBuilder("INSERT INTO " + enums.getSimpleName() + "(");
		for (Object l : enums.getEnumConstants()){
			if(Arrays.binarySearch(ignore, l) < 0)
				sb.append(l.toString().toLowerCase() + ",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(") VALUES(");
		for (Object l : enums.getEnumConstants())
			if(Arrays.binarySearch(ignore, l) < 0)
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
	
	ArrayList<LinkedHashMap<String, String>> getAllBerries() {
		try {
			ResultSet set = getAllBerries.executeQuery();
			ArrayList<LinkedHashMap<String, String>> data = new ArrayList<>();
			while (set.next()) {
				LinkedHashMap<String, String> rowData = new LinkedHashMap<>();
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
	
	ArrayList<LinkedHashMap<String, String>> getAllPrices() {
		try {
			ResultSet set = getAllPrices.executeQuery();
			ArrayList<LinkedHashMap<String, String>> data = new ArrayList<>();
			while (set.next()) {
				LinkedHashMap<String, String> rowData = new LinkedHashMap<>();
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
		try {
			StringBuilder sb = new StringBuilder("UPDATE "+Berries.class.getSimpleName()+" SET ");
			ArrayList<Object> data = new ArrayList<>();
			if (name != null) {
				sb.append(Berries.NAME.toString().toLowerCase()+ " = ?,");
				data.add(name);
			}
			if (number != null) {
				sb.append(Berries.NUMBER.toString().toLowerCase()+ " = ?,");
				data.add(number);
			}
			if (sold != null) {
				sb.append(Berries.SOLD.toString().toLowerCase()+ " = ?,");
				data.add(sold);
			}
			if (nonSold != null) {
				sb.append(Berries.NON_SOLD.toString().toLowerCase()+ " = ?,");
				data.add(nonSold);
			}
			if (price != null) {
				sb.append(Berries.PRICE.toString().toLowerCase()+ " = ?,");
				data.add(price);
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(" WHERE "+Berries.ID.toString().toLowerCase()+" = ?");
			System.out.println(sb.toString());
			PreparedStatement st = connection.prepareStatement(sb.toString());
			for(int i=0;i<data.size();i++){
				Object d = data.get(i);
				if(d instanceof String)
					st.setString(i+1, (String) d);
				else if(d instanceof Integer)
					st.setInt(i+1, (int) d);
				else
					System.err.println("Unkown type "+d.getClass());
			}
			st.setInt(data.size()+1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void updatePrice(int id, Integer price, Integer berryId) {
		try {
			StringBuilder sb = new StringBuilder("UPDATE "+Prices.class.getSimpleName()+" SET ");
			ArrayList<Object> data = new ArrayList<>();
			if (price != null) {
				sb.append(Prices.PRICE.toString().toLowerCase()+ " = ?,");
				data.add(price);
			}
			if (berryId != null) {
				sb.append(Prices.BERRY_ID.toString().toLowerCase()+ " = ?,");
				data.add(berryId);
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(" WHERE "+Berries.ID.toString().toLowerCase()+" = ?");
			System.out.println(sb.toString());
			PreparedStatement st = connection.prepareStatement(sb.toString());
			for(int i=0;i<data.size();i++){
				Object d = data.get(i);
				if(d instanceof String)
					st.setString(i+1, (String) d);
				else if(d instanceof Integer)
					st.setInt(i+1, (int) d);
				else
					System.err.println("Unkown type "+d.getClass());
			}
			st.setInt(data.size()+1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void deleteBerryItem(int id) {
		try {
			deleteBerries.setInt(Berries.ID.ordinal() + 1, id); // enum index from 0, setInt index from 1
			deleteBerries.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void deletePriceItem(int id) {
		try {
			deletePrices.setInt(Prices.ID.ordinal() + 1, id); // enum index from 0, setInt index from 1
			deletePrices.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
