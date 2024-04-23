package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
	private static final String DATABASE_URL = "Library-System/LibrarySystemBackEnd/library/sqlite-jdbc-3.7.2.jar";

	public Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DATABASE_URL);
			if (conn != null) {
				System.out.println("Connected to the database.");
				createTables(conn); // Create tables if they don't exist
			}
		} catch (SQLException e) {
			System.out.println("Error connecting to the database: " + e.getMessage());
		}
		return conn;
	}

	private void createTables(Connection conn) {
		String createUserProfilesTableSQL = "CREATE TABLE IF NOT EXISTS user_profiles ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "username TEXT NOT NULL," + "password TEXT NOT NULL,"
				+ "favorite_books TEXT," + "reading_habits TEXT," + "literary_preferences TEXT" + ");";

		String createPostsTableSQL = "CREATE TABLE IF NOT EXISTS posts (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "user_id INTEGER," + "content TEXT," + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
				+ "FOREIGN KEY(user_id) REFERENCES user_profiles(id)" + ");";

		// Add other table creation SQL statements as needed

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createUserProfilesTableSQL);
			stmt.execute(createPostsTableSQL);
			// Execute other table creation SQL statements
		} catch (SQLException e) {
			System.out.println("Error creating tables: " + e.getMessage());
		}
	}
}
