package library_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FollowManager {

	private static Scanner scanner;
	static {
		scanner = new Scanner(System.in);
	}

	public static void followMenu() {
		System.out.println("Welcome to the Follow Menu ");
		System.out.println("1. Follow user");
		System.out.println("2. Unfollow user ");
		System.out.println("3. See Following");
		System.out.println("4. See Followers");
		System.out.println("5. See user activity");
		System.out.println("6. Back to Social Menu");
		System.out.println("");
	}

	public static void startFollowOptions(String username, String password) {
		int choice;
		do {
			followMenu();
			System.out.print("Enter your choice:");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				followUser(username);
				break;
			case 2:
				unfollowUser(username);
				break;
			case 3:
				viewFollowing(username);
				break;
			case 4:
				viewFollowers(username);
				break;
			case 5:
				viewUserActivity();
				break;
			case 6:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

		} while (choice != 6);
	}

	public static void followUser(String followerUsername) {
		try {
			Connection conn = DataBaseManager.connect();

			System.out.print("Enter the username of the user you want to follow: ");
			String followUsername = scanner.nextLine();

			int followerId = getUserIdByUsername(followerUsername, conn);
			int followeeId = getUserIdByUsername(followUsername, conn);
			if (followeeId != -1) {
				followUserInDatabase(followerId, followeeId, conn);
				System.out.println("You are now following " + followUsername);
			} else {
				System.out.println("User " + followUsername + " does not exist.");
			}
		} catch (SQLException e) {
			System.out.println("Error following user: " + e.getMessage());
		}
	}

	public static void unfollowUser(String followerUsername) {
		try {
			Connection conn = DataBaseManager.connect();

			System.out.print("Enter the username of the user you want to unfollow: ");
			String unfollowUsername = scanner.nextLine();

			int followerId = getUserIdByUsername(followerUsername, conn);
			int followeeId = getUserIdByUsername(unfollowUsername, conn);
			if (followeeId != -1) {
				unfollowUserInDatabase(followerId, followeeId, conn);
				System.out.println("You are no longer following " + unfollowUsername);
			} else {
				System.out.println("User " + unfollowUsername + " does not exist.");
			}
		} catch (SQLException e) {
			System.out.println("Error unfollowing user: " + e.getMessage());
		}
	}

	public static void viewFollowing(String username) {
		try {
			Connection conn = DataBaseManager.connect();

			int userId = getUserIdByUsername(username, conn);
			List<String> followingUsernames = getFollowingUsernames(userId, conn);

			if (!followingUsernames.isEmpty()) {
				System.out.println("Users you are following:");
				for (String followeeUsername : followingUsernames) {
					System.out.println("- " + followeeUsername);
				}
			} else {
				System.out.println("You are not following anyone.");
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving following list: " + e.getMessage());
		}
	}

	public static void viewFollowers(String username) {
		try {
			Connection conn = DataBaseManager.connect();

			int userId = getUserIdByUsername(username, conn);
			List<String> followerUsernames = getFollowerUsernames(userId, conn);

			if (!followerUsernames.isEmpty()) {
				System.out.println("Users following you:");
				for (String followerUsername : followerUsernames) {
					System.out.println("- " + followerUsername);
				}
			} else {
				System.out.println("You have no followers.");
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving followers list: " + e.getMessage());
		}
	}

	public static void viewUserActivity() {
		try {
			Connection conn = DataBaseManager.connect();

			System.out.print("Enter the username to see activity: ");
			String username = scanner.nextLine();

			String sql = "SELECT u.*, p.* FROM posts p " + "INNER JOIN user_profiles u ON p.user_id = u.id "
					+ "WHERE u.username = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, username);
				try (ResultSet rs = pstmt.executeQuery()) {
					System.out.println("User Profile:");
					while (rs.next()) {
						System.out.println("Username: " + rs.getString("username"));
						System.out.println("Favorite Books: " + rs.getString("favorite_books"));
						System.out.println("Reading Habits: " + rs.getString("reading_habits"));
						System.out.println("Literary Preferences: " + rs.getString("literary_preferences"));
					}
					System.out.println();

					rs.beforeFirst(); // Move cursor back to the beginning to iterate over posts
					System.out.println("User Activity for " + username);
					System.out.println("Posts:");
					while (rs.next()) {
						int postId = rs.getInt("id");
						String content = rs.getString("content");
						String timestamp = rs.getString("timestamp");

						System.out.println("Post ID: " + postId);
						System.out.println("Content: " + content);
						System.out.println("Timestamp: " + timestamp);
						System.out.println();
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving user activity: " + e.getMessage());
		}
	}

	public static int getUserIdByUsername(String username, Connection conn) throws SQLException {
		String sql = "SELECT id FROM user_profiles WHERE username = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? rs.getInt("id") : -1; // If user not found, return -1
			}
		}
	}

	public static void followUserInDatabase(int followerId, int followeeId, Connection conn) throws SQLException {
		String sql = "INSERT INTO followers (follower_id, followee_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, followerId);
			pstmt.setInt(2, followeeId);
			pstmt.executeUpdate();
		}
	}

	public static void unfollowUserInDatabase(int followerId, int followeeId, Connection conn) throws SQLException {
		String sql = "DELETE FROM followers WHERE follower_id = ? AND followee_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, followerId);
			pstmt.setInt(2, followeeId);
			pstmt.executeUpdate();
		}
	}

	public static List<String> getFollowingUsernames(int userId, Connection conn) throws SQLException {
		List<String> followingUsernames = new ArrayList<>();
		String sql = "SELECT u.username FROM followers f JOIN user_profiles u ON f.followee_id = u.id WHERE f.follower_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					followingUsernames.add(rs.getString("username"));
				}
			}
		}
		return followingUsernames;
	}

	public static List<String> getFollowerUsernames(int userId, Connection conn) throws SQLException {
		List<String> followerUsernames = new ArrayList<>();
		String sql = "SELECT u.username FROM followers f JOIN user_profiles u ON f.follower_id = u.id WHERE f.followee_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					followerUsernames.add(rs.getString("username"));
				}
			}
		}
		return followerUsernames;
	}
}
