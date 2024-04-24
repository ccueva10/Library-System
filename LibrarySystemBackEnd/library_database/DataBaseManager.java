package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import library.Book;
import library.User;

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
		String createUserProfilesTableSQL = "CREATE TABLE IF NOT EXISTS user_profiles (" + "id TEXT PRIMARY KEY,"
				+ "username TEXT NOT NULL," + "password TEXT NOT NULL," + "favorite_books TEXT,"
				+ "reading_habits TEXT," + "literary_preferences TEXT" + ");";

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createUserProfilesTableSQL);
		} catch (SQLException e) {
			System.out.println("Error creating user profiles table: " + e.getMessage());
		}
	}

	public static void createUserProfile(User user) {
		String insertSQL = "INSERT INTO user_profiles (id, username, password, favorite_books, reading_habits, literary_preferences) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setString(1, user.getId().toString());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, booksToString(user.getFavoriteBooks()));
			pstmt.setString(5, user.getReadingHabits());
			pstmt.setString(6, user.getLiteraryPref());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error adding user profile: " + e.getMessage());
		}
	}

	private static String booksToString(List<Book> books) {
		StringBuilder sb = new StringBuilder();
		for (Book book : books) {
			sb.append(book.getISBN()).append(",");
		}
		return sb.toString();
	}

}
