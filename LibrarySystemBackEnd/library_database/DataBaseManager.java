package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

	private static final String DATABASE_URL = "jdbc:sqlite:LibrarySystemBackEnd/library_database/library_database.db";

	public static Connection connect() throws SQLException {
		try {
			DriverManager.registerDriver(new org.sqlite.JDBC());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error registering SQLite JDBC driver", e);
		}
		return DriverManager.getConnection(DATABASE_URL);
	}

	private static void createTables(Connection conn) {
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
