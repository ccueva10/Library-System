package library_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import library.Book;
import library.SocialInteractionManager;
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
		System.out.println("...");
		Connection conn = DriverManager.getConnection(DATABASE_URL);

		// Call createTables method to create the necessary tables
		createTables(conn);

		return conn;
	}

	public static void displaySocialMenu() {
		System.out.println("Welcome to the BiblioConnect Social Network!");
		System.out.println("1. Update User Profile");
		System.out.println("2. Social Interactions");
		System.out.println("3. Groups and Discussions");
		System.out.println("4. Following and Followers");
		System.out.println("5. Events and Meetups");
		System.out.println("6. Exit");
		System.out.println("");
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
				updateUserProfile(username);
				break;
			case 2:
				SocialInteractionManager.startInteractionOptions(username);
				break;
			case 3:
				// groups and discussions
				// join or create interest based groups
				// discussions

				GroupsManager.groupsStart();
				break;
			case 4:
				// following
				// follow eachother to stay updated on their activies, book recommendations and
				// discussions
				FollowManager.startFollowOptions(username, password);
				break;
			case 5:
				// events & meet up s
				// the platform hosts literary events, book clubs, author signings and other
				// literary gatherings
				// this should be managed by librarian and read by users
				EventManager.startEventOptions(username, password);

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
				+ "user_id TEXT," + "message TEXT," + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
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

	private static void updateUserProfile(String username) {

		// Get user input for favorite books, reading habits, and literary preferences
		System.out.println("Enter your favorite books (comma-separated list): ");
		String favoriteBooksInput = scanner.nextLine();
		List<Book> favoriteBooks = parseFavoriteBooksInput(favoriteBooksInput);

		System.out.print("Enter your reading habits: ");
		String readingHabits = scanner.nextLine();

		System.out.print("Enter your literary preferences: ");
		String literaryPreferences = scanner.nextLine();

		// Create a User object with the updated profile information
		User user = new User(username, "");
		user.setFavoriteBooks(favoriteBooks);
		user.setReadingHabits(readingHabits);
		user.setLiteraryPref(literaryPreferences);

		// Update the user profile in the database
		updateUserProfileInDatabase(user);
	}

	private static List<Book> parseFavoriteBooksInput(String input) {
		List<Book> favoriteBooks = new ArrayList<>();
		String[] bookISBNs = input.split(",");

		// Directly add the user's input to the favorite books list without checking against the library
		for (String ISBN : bookISBNs) {
			Book book = new Book(ISBN.trim(), "", "", ""); // Create a new Book object with the ISBN provided by the
															// user
			favoriteBooks.add(book);
		}

		return favoriteBooks;
	}

	private static void updateUserProfileInDatabase(User user) {
		// Update user profile in the database with the new information
		String updateSQL = "UPDATE user_profiles SET favorite_books = ?, reading_habits = ?, literary_preferences = ? WHERE username = ?";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
			pstmt.setString(1, booksToString(user.getFavoriteBooks()));
			pstmt.setString(2, user.getReadingHabits());
			pstmt.setString(3, user.getLiteraryPref());
			pstmt.setString(4, user.getUsername());

			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User profile updated successfully.");
			} else {
				System.out.println("Failed to update user profile.");
			}
		} catch (SQLException e) {
			System.out.println("Error updating user profile: " + e.getMessage());
		}
	}

}
