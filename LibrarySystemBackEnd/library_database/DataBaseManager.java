package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DataBaseManager {

	private static Scanner scanner;
	private static final String DATABASE_URL = "jdbc:sqlite:LibrarySystemBackEnd/library_database/library_database.db";

	static {
		scanner = new Scanner(System.in);
	}

	public static Connection connect() throws SQLException {
		try {
			DriverManager.registerDriver(new org.sqlite.JDBC());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error registering SQLite JDBC driver", e);
		}
		System.out.println("Successfully created/loaded db");
		return DriverManager.getConnection(DATABASE_URL);
	}

	public static void displaySocialMenu() {
		System.out.println("Welcome to the Library Management System!");
		System.out.println("1. User Profiles");
		System.out.println("2. Social Interactions");
		System.out.println("3. Groups and Discussions");
		System.out.println("4. Following and Followers");
		System.out.println("5. Events and Meetups");
		System.out.println("6. Exit");
	}

	public static void socialStart() {

		int choice;
		do {
			displaySocialMenu();
			System.out.print("Enter your choice:");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

		} while (choice != 6);
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
