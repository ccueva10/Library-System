package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import library_database.DataBaseManager;

public class User {
	private UUID id;
	private String username;
	private String password;
	private List<Book> favoriteBooks;
	private String readingHabits;
	private String literaryPref;

	public User() {
		this.username = "";
		this.password = "";
		this.favoriteBooks = new ArrayList<>();
		this.readingHabits = "";
		this.literaryPref = "";
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.favoriteBooks = new ArrayList<>();
		this.readingHabits = "";
		this.literaryPref = "";
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Book> getFavoriteBooks() {
		return favoriteBooks;
	}

	public void addFavoriteBook(Book book) {
		favoriteBooks.add(book);
	}

	public String getReadingHabits() {
		return readingHabits;
	}

	public void setReadingHabits(String readingHabits) {
		this.readingHabits = readingHabits;
	}

	public String getLiteraryPref() {
		return literaryPref;
	}

	public void setLiteraryPref(String literaryPref) {
		this.literaryPref = literaryPref;
	}

	public static User getUserFromDatabase(UUID userId) {
		String sql = "SELECT * FROM user_profiles WHERE id = ?";
		User user = null;

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userId.toString());
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				List<Book> favoriteBooks = stringToBooks(rs.getString("favorite_books"));
				String readingHabits = rs.getString("reading_habits");
				String literaryPref = rs.getString("literary_preferences");

				user = new User(username, password);
				user.setId(userId);
				user.favoriteBooks = favoriteBooks;
				user.readingHabits = readingHabits;
				user.literaryPref = literaryPref;
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving user from database: " + e.getMessage());
		}

		return user;
	}

	private static List<Book> stringToBooks(String bookString) {
		String[] bookIds = bookString.split(",");
		List<Book> books = new ArrayList<>();

		Library library = new Library();
		for (String id : bookIds) {
			Book book = library.findBookByISBN(id);
			if (book != null) {
				books.add(book);
			}
		}
		return books;
	}

	public boolean registerUser() {
		// Check if username already exists
		if (checkUsernameExists(this.username)) {
			System.out.println("Username already exists. Please choose another username.");
			return false;
		}

		// Insert new user into database
		String sql = "INSERT INTO user_profiles (id, username, password) VALUES (?, ?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			this.id = UUID.randomUUID();
			pstmt.setString(1, this.id.toString());
			pstmt.setString(2, this.username);
			pstmt.setString(3, this.password);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println("Error registering user: " + e.getMessage());
			return false;
		}
	}

	public boolean loginUser() {
		// Fetch user from database based on username and password
		String sql = "SELECT * FROM user_profiles WHERE username = ? AND password = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, this.username);
			pstmt.setString(2, this.password);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				this.id = UUID.fromString(rs.getString("id"));
				// Set other user properties here if needed
				return true;
			} else {
				System.out.println("Invalid username or password.");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Error logging in: " + e.getMessage());
			return false;
		}
	}

	private boolean checkUsernameExists(String username) {
		// Check if username already exists in the database
		String sql = "SELECT * FROM user_profiles WHERE username = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs.next(); // Returns true if username exists, false otherwise
		} catch (SQLException e) {
			System.out.println("Error checking username: " + e.getMessage());
			return false;
		}
	}

}
