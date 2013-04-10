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
	private enum Berries implements DatabaseKeys{
		ID("INTEGER PRIMARY KEY", Integer.class),
		NAME("TEXT", String.class),
		NUMBER("INTEGER", Integer.class),
		SOLD("INTEGER", Integer.class),
		NON_SOLD("INTEGER", Integer.class),
		PRICE("INTEGER", Integer.class);
		
		private final String databaseType;
		private final Class<?> javaClass;
		private static HashMap<String, Class<?>> map;
		
		private Berries(String databaseType, Class<?> javaClass){
			this.databaseType = databaseType;
			this.javaClass = javaClass;
		}

		@Override
		public String getDatabaseKey(){
			return toString().toLowerCase();
		}
		
		@Override
		public String getDatabaseType() {
			return getDatabaseKey() + " " + databaseType;
		}
		
		private synchronized static HashMap<String, Class<?>> getJavaClassMapping(){
			if(map == null){
				map = new HashMap<String, Class<?>>();
				for(Berries e : values())
					map.put(e.getDatabaseKey(), e.javaClass);
			}
			return map;
		}
	}
	private interface DatabaseKeys{
		/**
		 * Returns the name of this enum value in the database,
		 * for which values are stored.
		 * @return Name of the key in the database.
		 */
		String getDatabaseKey();

		/**
		 * Returners the database type of this enum value, it can also
		 * contain constraints and similar information.
		 * @return Database type.
		 */
		String getDatabaseType();
	}
	private enum Prices implements DatabaseKeys{
		ID("INTEGER PRIMARY KEY", Integer.class),
		PRICE("INTEGER", Integer.class),
		BERRY_ID("INTEGER, FOREIGN KEY(berry_id) REFERENCES Berries(id) ON DELETE CASCADE", Integer.class);
		
		private final String databaseType;
		private final Class<?> javaClass;
		private static HashMap<String, Class<?>> map;
		
		private Prices(String databaseType, Class<?> javaClass){
			this.databaseType = databaseType;
			this.javaClass = javaClass;
		}

		@Override
		public String getDatabaseKey(){
			return toString().toLowerCase();
		}
		
		@Override
		public String getDatabaseType() {
			return getDatabaseKey() + " " + databaseType;
		}
		
		private synchronized static HashMap<String, Class<?>> getJavaClassMapping(){
			if(map == null){
				map = new HashMap<String, Class<?>>();
				for(Prices e : values())
					map.put(e.getDatabaseKey(), e.javaClass);
			}
			return map;
		}
	}
	
	private static Database instance;
	
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
	
	private final Connection connection;
	
	private final PreparedStatement insertBerries, insertPrices, getAllBerries, getAllPrices, deleteBerries, deletePrices, minPrice, maxPrice;

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
		
		deleteBerries = connection.prepareStatement("DELETE FROM "+Berries.class.getSimpleName()+" WHERE "+Berries.ID.getDatabaseKey()+" = ?");
		deletePrices = connection.prepareStatement("DELETE FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.ID.getDatabaseKey()+" = ?");
		
		minPrice = connection.prepareStatement("SELECT MIN("+Prices.PRICE.getDatabaseKey()+") FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.BERRY_ID.getDatabaseKey()+" = ?");
		maxPrice = connection.prepareStatement("SELECT MAX("+Prices.PRICE.getDatabaseKey()+") FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.BERRY_ID.getDatabaseKey()+" = ?");
	}
	
	boolean addBerry(String name, int number, int sold, int nonSold, int price) {
		try {
			insertBerries.setString(Berries.NAME.ordinal(), name);
			insertBerries.setInt(Berries.NUMBER.ordinal(), number);
			insertBerries.setInt(Berries.SOLD.ordinal(), sold);
			insertBerries.setInt(Berries.NON_SOLD.ordinal(), nonSold);
			insertBerries.setInt(Berries.PRICE.ordinal(), price);
			return insertBerries.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	boolean addPrice(int price, int berry_id) {
		try {
			insertPrices.setInt(Prices.PRICE.ordinal(), price);
			insertPrices.setInt(Prices.BERRY_ID.ordinal(), berry_id);
			return insertPrices.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	boolean deleteBerryItem(int id) {
		try {
			deleteBerries.setInt(Berries.ID.ordinal() + 1, id); // enum index from 0, setInt index from 1
			return deleteBerries.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	boolean deletePriceItem(int id) {
		try {
			deletePrices.setInt(Prices.ID.ordinal() + 1, id); // enum index from 0, setInt index from 1
			return deletePrices.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String generateInsert(Class<? extends Enum<?>> enums, Enum<?>... ignore){
		Arrays.sort(ignore);
		StringBuilder sb = new StringBuilder("INSERT INTO " + enums.getSimpleName() + "(");
		for (Enum<?> l : enums.getEnumConstants()){
			if(Arrays.binarySearch(ignore, l) < 0){
				DatabaseKeys e =  (DatabaseKeys) l;
				sb.append(e.getDatabaseKey() + ",");
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(") VALUES(");
		for (Enum<?> l : enums.getEnumConstants())
			if(Arrays.binarySearch(ignore, l) < 0)
				sb.append("?,");
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")");
		String returnValue = sb.toString();
		System.out.println(returnValue);
		return returnValue;
	}

	private String generateTable(Class<? extends Enum<? extends DatabaseKeys>> enums){
		StringBuilder sb = new StringBuilder("CREATE TABLE " + enums.getSimpleName() + "(");
		for (Enum<?> l : enums.getEnumConstants()){
			DatabaseKeys e =  (DatabaseKeys) l;
			sb.append(e.getDatabaseType() + ",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")");
		String returnValue = sb.toString();
		System.out.println(returnValue);
		return returnValue;
	}
	
	ArrayList<LinkedHashMap<String, Object>> getAllBerries() {
		try {
			ResultSet set = getAllBerries.executeQuery();
			ArrayList<LinkedHashMap<String, Object>> data = new ArrayList<>();
			while (set.next()) {
				LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
				for (Berries l : Berries.values()){
					rowData.put(l.toString(), set.getObject(l.ordinal() + 1));
				}
				data.add(rowData);
			}
			set.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	ArrayList<LinkedHashMap<String, Object>> getAllPrices() {
		try {
			ResultSet set = getAllPrices.executeQuery();
			ArrayList<LinkedHashMap<String, Object>> data = new ArrayList<>();
			while (set.next()) {
				LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
				for (Prices l : Prices.values())
					rowData.put(l.toString(), set.getObject(l.ordinal() + 1));
				data.add(rowData);
			}
			set.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	Integer getMaxPrice(int id){
		try {
			maxPrice.setInt(1, id);
			ResultSet result = maxPrice.executeQuery();
			if(result.next()){
				int v = result.getInt(1);
				return !result.wasNull() ? v : null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return the minimum value of all prices for a specific berry id,
	 * note that the berry id have to be present in the Berries table.
	 * If the value don't exist, the list is empty or if an error occur
	 * is the return value null;
	 * @param id Id of a berry in the berries database table.
	 * @return Minimum value or null if value don't exist or if error occur.
	 */
	Integer getMinPrice(int id){
		try {
			minPrice.setInt(1, id);
			ResultSet result = minPrice.executeQuery();
			if(result.next()){
				int v = result.getInt(1);
				return !result.wasNull() ? v : null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void printTable(Class<? extends Enum<?>> table){
		ArrayList<LinkedHashMap<String, Object>> list = null;
		if(table == Berries.class){
			list = getAllBerries();
			System.out.println("BERRIES");
		} else if(table == Prices.class){
			list = getAllPrices();
			System.out.println("PRICES");
		}
		for(HashMap<String, Object> a : list){
			for(Entry<String, Object> e : a.entrySet())
				System.out.print(e + " ");
			System.out.println();
		}		
	}

	boolean updateBerry(int id, String name, Integer number, Integer sold, Integer nonSold, Integer price) {
		try {
			StringBuilder sb = new StringBuilder("UPDATE "+Berries.class.getSimpleName()+" SET ");
			ArrayList<Object> data = new ArrayList<>();
			if (name != null) {
				sb.append(Berries.NAME.getDatabaseKey()+ " = ?,");
				data.add(name);
			}
			if (number != null) {
				sb.append(Berries.NUMBER.getDatabaseKey()+ " = ?,");
				data.add(number);
			}
			if (sold != null) {
				sb.append(Berries.SOLD.getDatabaseKey()+ " = ?,");
				data.add(sold);
			}
			if (nonSold != null) {
				sb.append(Berries.NON_SOLD.getDatabaseKey()+ " = ?,");
				data.add(nonSold);
			}
			if (price != null) {
				sb.append(Berries.PRICE.getDatabaseKey()+ " = ?,");
				data.add(price);
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(" WHERE "+Berries.ID.getDatabaseKey()+" = ?");
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
			return st.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	boolean updatePrice(int id, Integer price, Integer berryId) {
		try {
			StringBuilder sb = new StringBuilder("UPDATE "+Prices.class.getSimpleName()+" SET ");
			ArrayList<Object> data = new ArrayList<>();
			if (price != null) {
				sb.append(Prices.PRICE.getDatabaseKey()+ " = ?,");
				data.add(price);
			}
			if (berryId != null) {
				sb.append(Prices.BERRY_ID.getDatabaseKey()+ " = ?,");
				data.add(berryId);
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(" WHERE "+Prices.ID.getDatabaseKey()+" = ?");
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
			return st.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
