package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import library_database.DataBaseManager;

public class SocialInteractionManager {

	private static Scanner scanner;

	static {
		scanner = new Scanner(System.in);
	}

	public static void interactionMenu() {
		System.out.println("Welcome to the Social Interaction Menu");
		System.out.println("1. Post a message");
		System.out.println("2. Comment on a post");
		System.out.println("3. Like a post");
		System.out.println("4. Share a post");
		System.out.println("5. Back to Social Menu");
		System.out.println("");
	}

	public static void startInteractionOptions(String username) {
		int choice;
		try {
			int userId = getUserIdByUsername(username);
			do {
				interactionMenu();
				System.out.print("Enter your choice:");
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				switch (choice) {
				case 1:
					System.out.print("Enter your message: ");
					String message = scanner.nextLine();
					postMessage(userId, message);
					System.out.println("Message posted successfully.");
					break;
				case 2:
					System.out.print("Enter the post ID: ");
					int postId = scanner.nextInt();
					scanner.nextLine(); // Consume newline
					System.out.print("Enter your comment: ");
					String comment = scanner.nextLine();
					commentOnPost(userId, postId, comment);
					System.out.println("Comment added successfully.");
					break;
				case 3:
					System.out.print("Enter the post ID: ");
					postId = scanner.nextInt();
					likePost(userId, postId);
					System.out.println("Post liked successfully.");
					break;
				case 4:
					System.out.print("Enter the post ID: ");
					postId = scanner.nextInt();
					sharePost(userId, postId);
					System.out.println("Post shared successfully.");
					break;
				case 5:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
				}

			} while (choice != 5);
		} catch (SQLException e) {
			System.out.println("Error starting interaction options: " + e.getMessage());
		}
	}

	public static void postMessage(int userId, String message) throws SQLException {
		String sql = "INSERT INTO posts (user_id, message) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setString(2, message);
			pstmt.executeUpdate();
		}
	}

	public static void commentOnPost(int userId, int postId, String comment) throws SQLException {
		String sql = "INSERT INTO comments (user_id, post_id, content) VALUES (?, ?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.setString(3, comment);
			pstmt.executeUpdate();
		}
	}

	public static void likePost(int userId, int postId) throws SQLException {
		String sql = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		}
	}

	public static void sharePost(int userId, int postId) throws SQLException {
		String sql = "INSERT INTO shares (user_id, post_id) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		}
	}

	public static int getUserIdByUsername(String username) throws SQLException {
		String sql = "SELECT id FROM user_profiles WHERE username = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id");
				} else {
					throw new SQLException("User not found");
				}
			}
		}
	}
}
