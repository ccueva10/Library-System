package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import library.Book;
import library.User;

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
		System.out.println("Successfully logged in to BiblioConnect");
		Connection conn = DriverManager.getConnection(DATABASE_URL);

		// Call createTables method to create the necessary tables
		createTables(conn);

		return conn;
	}

	public static void displaySocialMenu() {
		System.out.println("Welcome to the Library Management System!");
		System.out.println("1. Update User Profile");
		System.out.println("2. Social Interactions");
		System.out.println("3. Groups and Discussions");
		System.out.println("4. Following and Followers");
		System.out.println("5. Events and Meetups");
		System.out.println("6. Exit");
	}

	public static void socialStart(String username, String password) {

		int choice;
		do {
			displaySocialMenu();
			System.out.print("Enter your choice:");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:

				// edit profile habits literary preferences
				break;
			case 2:
				// social interactions
				// patrons can interact posting messages, commenting, and liking or sharing
				// content
				break;
			case 3:
				// groups and discussions
				// join or create interest based groups
				// discussions
				break;
			case 4:
				// following
				// follow eachother to stay updated on their activies, book recommendations and
				// discussions

				break;
			case 5:
				// events & meet up s
				// the platform hosts literary events, book clubs, author signings and other
				// literary gatherings
				// this should be managed by librarian and read by users

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
		String createUserProfilesTableSQL = "CREATE TABLE IF NOT EXISTS user_profiles (" + "id TEXT PRIMARY KEY,"
				+ "username TEXT NOT NULL," + "password TEXT NOT NULL," + "favorite_books TEXT,"
				+ "reading_habits TEXT," + "literary_preferences TEXT" + ");";

		String createPostsTableSQL = "CREATE TABLE IF NOT EXISTS posts (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "user_id TEXT," + "content TEXT," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
				+ "FOREIGN KEY (user_id) REFERENCES user_profiles(id)" + ");";

		String createCommentsTableSQL = "CREATE TABLE IF NOT EXISTS comments ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "post_id INTEGER," + "user_id TEXT," + "content TEXT,"
				+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + "FOREIGN KEY (post_id) REFERENCES posts(id),"
				+ "FOREIGN KEY (user_id) REFERENCES user_profiles(id)" + ");";

		String createLikesTableSQL = "CREATE TABLE IF NOT EXISTS likes (" + "post_id INTEGER," + "user_id TEXT,"
				+ "PRIMARY KEY (post_id, user_id)," + "FOREIGN KEY (post_id) REFERENCES posts(id),"
				+ "FOREIGN KEY (user_id) REFERENCES user_profiles(id)" + ");";

		String createGroupsTableSQL = "CREATE TABLE IF NOT EXISTS groups (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT," + "description TEXT" + ");";

		String createUserGroupsTableSQL = "CREATE TABLE IF NOT EXISTS user_groups (" + "user_id TEXT,"
				+ "group_id INTEGER," + "PRIMARY KEY (user_id, group_id),"
				+ "FOREIGN KEY (user_id) REFERENCES user_profiles(id)," + "FOREIGN KEY (group_id) REFERENCES groups(id)"
				+ ");";

		String createGroupDiscussionsTableSQL = "CREATE TABLE IF NOT EXISTS group_discussions ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "group_id INTEGER," + "user_id TEXT," + "content TEXT,"
				+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + "FOREIGN KEY (group_id) REFERENCES groups(id),"
				+ "FOREIGN KEY (user_id) REFERENCES user_profiles(id)" + ");";

		String createFollowersTableSQL = "CREATE TABLE IF NOT EXISTS followers (" + "follower_id TEXT,"
				+ "followee_id TEXT," + "PRIMARY KEY (follower_id, followee_id),"
				+ "FOREIGN KEY (follower_id) REFERENCES user_profiles(id),"
				+ "FOREIGN KEY (followee_id) REFERENCES user_profiles(id)" + ");";

		String createEventsTableSQL = "CREATE TABLE IF NOT EXISTS events (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT," + "description TEXT," + "date DATETIME" + ");";

		String createEventAttendeesTableSQL = "CREATE TABLE IF NOT EXISTS event_attendees (" + "event_id INTEGER,"
				+ "user_id TEXT," + "PRIMARY KEY (event_id, user_id)," + "FOREIGN KEY (event_id) REFERENCES events(id),"
				+ "FOREIGN KEY (user_id) REFERENCES user_profiles(id)" + ");";

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createUserProfilesTableSQL);
			stmt.execute(createPostsTableSQL);
			stmt.execute(createCommentsTableSQL);
			stmt.execute(createLikesTableSQL);
			stmt.execute(createGroupsTableSQL);
			stmt.execute(createUserGroupsTableSQL);
			stmt.execute(createGroupDiscussionsTableSQL);
			stmt.execute(createFollowersTableSQL);
			stmt.execute(createEventsTableSQL);
			stmt.execute(createEventAttendeesTableSQL);
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
