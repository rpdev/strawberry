package froapp2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Database {
	private static Database instance;
	private final Connection connection;

	private Database() {
		Connection tmp = null;
		try {
			Class.forName("org.sqlite.JDBC");
			tmp = DriverManager.getConnection("jdbc:sqlite:sample.db");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		connection = tmp;
	}

	static Database getInstance() {
		if (instance == null)
			instance = new Database();
		return instance;
	}
}
