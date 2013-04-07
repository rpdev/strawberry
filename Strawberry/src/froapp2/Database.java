package froapp2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;

class Database {
	private static Database instance;
	private final Connection connection;
	private final PreparedStatement insertStatment, getAllStatement, deleteStatement;

	private Database() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		
		Labels[] values = Labels.values();
		
		Statement statement = connection.createStatement();		
		statement.executeUpdate("drop table if exists berries");
		StringBuilder sb = new StringBuilder("create table berries (");
		for(Labels l : values)
			sb.append(l.toString().toLowerCase() + " " + l.databaseType + ",");
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")");
		System.out.println(sb.toString());
		statement.executeUpdate(sb.toString());
		
		
		StringBuilder insertString = new StringBuilder("INSERT INTO berries(");
		for(int i=1;i<values.length;i++){
			insertString.append(values[i].toString().toLowerCase() + ",");
		}
		insertString.deleteCharAt(insertString.lastIndexOf(","));
		insertString.append(") VALUES(");
		for(int i=1;i<values.length;i++){
			insertString.append("?,");
		}
		insertString.deleteCharAt(insertString.lastIndexOf(","));
		insertString.append(")");		
		System.out.println(insertString.toString());
		insertStatment = connection.prepareStatement(insertString.toString());
		
		getAllStatement = connection.prepareStatement("SELECT * FROM berries");
		deleteStatement = connection.prepareStatement("DELETE FROM berries WHERE "+Labels.ID.toString().toLowerCase() + " = ?");
	}

	static Database getInstance() {
		if (instance == null){
			try {
				instance = new Database();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	ArrayList<EnumMap<Labels, String>> getAllContent(){
		try {
			ResultSet set = getAllStatement.executeQuery();
			ArrayList<EnumMap<Labels, String>> data = new ArrayList<>();
			while(set.next()){
				EnumMap<Labels, String> rowData = new EnumMap<>(Labels.class);
				for(Labels l : Labels.values())
					rowData.put(l, set.getString(l.ordinal()+1));
				data.add(rowData);				
			}
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	void addItem(String name, int number, int sold, int nonSold, int price){
		try {
			insertStatment.setString(Labels.NAME.ordinal(), name);
			insertStatment.setInt(Labels.NUMBER.ordinal(), number);
			insertStatment.setInt(Labels.SOLD.ordinal(), sold);
			insertStatment.setInt(Labels.NON_SOLD.ordinal(), nonSold);
			insertStatment.setInt(Labels.PRICE.ordinal(), price);
			insertStatment.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void deleteItem(int id){
		try {
			deleteStatement.setInt(Labels.ID.ordinal()+1, id);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
