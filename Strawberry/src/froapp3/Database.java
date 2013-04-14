package froapp3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

class Database {
	enum Berries implements DatabaseKeys{
		ID("INTEGER PRIMARY KEY", Integer.class, "ID"),
		NAME("TEXT", String.class, "Namn"),
		NUMBER("INTEGER", Integer.class, "Antal"),
		SOLD("INTEGER", Integer.class, "Sålda"),
		NON_SOLD("INTEGER", Integer.class, "Ej Sålda"),
		PRICE("INTEGER", Integer.class, "Pris");
		
		private final String databaseType;
		private final Class<?> javaClass;
		private final String name;
		
		private Berries(String databaseType, Class<?> javaClass, String name){
			this.databaseType = databaseType;
			this.javaClass = javaClass;
			this.name = name;
		}

		@Override
		public String getDatabaseKey(){
			return toString().toLowerCase();
		}
		
		@Override
		public String getDatabaseType() {
			return getDatabaseKey() + " " + databaseType;
		}

		@Override
		public Class<?> getJavaClass() {
			return javaClass;
		}

		@Override
		public String getName() {
			return name;
		}
	}
	interface DatabaseKeys{
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
		
		Class<?> getJavaClass();
		String getName();
	}
	enum Prices implements DatabaseKeys{
		ID("INTEGER PRIMARY KEY", Integer.class, "ID"),
		PRICE("INTEGER", Integer.class, "Pris"),
		BERRY_ID("INTEGER, FOREIGN KEY(berry_id) REFERENCES Berries(id) ON DELETE CASCADE", Integer.class, "Hidden");
		
		private final String databaseType;
		private final Class<?> javaClass;
		private final String name;
		
		private Prices(String databaseType, Class<?> javaClass, String name){
			this.databaseType = databaseType;
			this.javaClass = javaClass;
			this.name = name;
		}

		@Override
		public String getDatabaseKey(){
			return toString().toLowerCase();
		}
		
		@Override
		public String getDatabaseType() {
			return getDatabaseKey() + " " + databaseType;
		}

		@Override
		public Class<?> getJavaClass() {
			return javaClass;
		}

		@Override
		public String getName() {
			return name;
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
	
	private final PreparedStatement insertBerries, insertPrices, getAllBerries, getAllPrices, deleteBerries, deletePrices, minPrice, maxPrice, updatePrice;

	private Database() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:BerriesDatabase.db");
		connection.createStatement().execute("PRAGMA foreign_keys=ON;");
		
		if(!connection.getMetaData().getTables(null, null, Berries.class.getSimpleName(), null).next()){		
			Statement statement = connection.createStatement();
			statement.executeUpdate(generateTable(Berries.class));
			statement.executeUpdate(generateTable(Prices.class));
		}
		
		insertBerries = connection.prepareStatement(generateInsert(Berries.class, Berries.ID));
		insertPrices = connection.prepareStatement(generateInsert(Prices.class, Prices.ID));

		getAllBerries = connection.prepareStatement("SELECT * FROM "+Berries.class.getSimpleName());
		getAllPrices = connection.prepareStatement("SELECT * FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.BERRY_ID.getDatabaseKey()+" = ?");
		
		deleteBerries = connection.prepareStatement("DELETE FROM "+Berries.class.getSimpleName()+" WHERE "+Berries.ID.getDatabaseKey()+" = ?");
		deletePrices = connection.prepareStatement("DELETE FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.ID.getDatabaseKey()+" = ?");
		
		minPrice = connection.prepareStatement("SELECT MIN("+Prices.PRICE.getDatabaseKey()+") FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.BERRY_ID.getDatabaseKey()+" = ?");
		maxPrice = connection.prepareStatement("SELECT MAX("+Prices.PRICE.getDatabaseKey()+") FROM "+Prices.class.getSimpleName()+" WHERE "+Prices.BERRY_ID.getDatabaseKey()+" = ?");
		
		updatePrice = connection.prepareStatement("UPDATE "+Prices.class.getSimpleName()+" SET "+Prices.PRICE.getDatabaseKey()+" = ? WHERE id = ?");
	}
	
	boolean addBerry(EnumMap<Berries, Object> values) {
		try {
			insertBerries.setString(Berries.NAME.ordinal(), (String) values.get(Berries.NAME));
			insertBerries.setInt(Berries.NUMBER.ordinal(), (int) values.get(Berries.NUMBER));
			insertBerries.setInt(Berries.SOLD.ordinal(), (int) values.get(Berries.SOLD));
			insertBerries.setInt(Berries.NON_SOLD.ordinal(), (int) values.get(Berries.NON_SOLD));
			insertBerries.setInt(Berries.PRICE.ordinal(), (int) values.get(Berries.PRICE));
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
	
	ArrayList<EnumMap<Berries, Object>> getAllBerries() {
		try {
			ResultSet set = getAllBerries.executeQuery();
			ArrayList<EnumMap<Berries, Object>> data = new ArrayList<>();
			while (set.next()) {
				EnumMap<Berries, Object> rowData = new EnumMap<>(Berries.class);
				for (Berries l : Berries.values()){
					rowData.put(l, set.getObject(l.ordinal() + 1));
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
	
	ArrayList<EnumMap<Prices, Object>> getAllPrices(int id) {
		try {
			getAllPrices.setInt(1, id);
			ResultSet set = getAllPrices.executeQuery();
			ArrayList<EnumMap<Prices, Object>> data = new ArrayList<>();
			while (set.next()) {
				EnumMap<Prices, Object> rowData = new EnumMap<>(Prices.class);
				for (Prices l : Prices.values())
					rowData.put(l, set.getObject(l.ordinal() + 1));
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
	
	boolean updatePrice(int id, int price) {
		try {
			updatePrice.setInt(1, price);
			updatePrice.setInt(2, id);
			return updatePrice.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
