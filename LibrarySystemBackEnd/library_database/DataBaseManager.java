package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
//=======
import java.util.List;
//<<<<<<< HEAD
import java.util.Scanner;

import library.Book;
import library.User;
//>>>>>>> 1b5c23757a6b51489444a011931d8232ba8b7982

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
				postMessage();
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

	private static void postMessage() {
        try {
    		System.out.print("Enter your user ID: ");
    		int userId = scanner.nextInt();
           	scanner.nextLine(); // Consume newline
          	System.out.print("Enter your message: ");
           	String message = scanner.nextLine();

           	SocialInteractionManager.postMessage(userId, message);
           	System.out.println("Message posted successfully!");
    	} catch (SQLException e) {
            System.err.println("Error posting message: " + e.getMessage());
        }
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
