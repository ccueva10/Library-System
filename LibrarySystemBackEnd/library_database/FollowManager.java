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
		System.out.println("6. Exit");

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
				System.out.print("Enter the username of the user you want to follow: ");
				String followUsername = scanner.nextLine();
				try {
					int followerId = getUserIdByUsername(username); // Get the ID of the current user
					int followeeId = getUserIdByUsername(followUsername); // Get the ID of the user to follow
					if (followeeId != -1) { // If the user to follow exists
						followUser(followerId, followeeId); // Follow the user
						System.out.println("You are now following " + followUsername);
					} else {
						System.out.println("User " + followUsername + " does not exist.");
					}
				} catch (SQLException e) {
					System.out.println("Error following user: " + e.getMessage());
				}
				break;
			case 2:
				System.out.print("Enter the username of the user you want to follow: ");
				String unfollowUserName = scanner.nextLine();
				try {
					int followerId = getUserIdByUsername(username); // Get the ID of the current user
					int followeeId = getUserIdByUsername(unfollowUserName); // Get the ID of the user to follow
					if (followeeId != -1) { // If the user to follow exists
						unfollowUser(followerId, followeeId); // unfollow the user
						System.out.println("You are now following " + unfollowUserName);
					} else {
						System.out.println("User " + unfollowUserName + " does not exist.");
					}
				} catch (SQLException e) {
					System.out.println("Error following user: " + e.getMessage());
				}

				break;
			case 3:
				try {
					int userId = getUserIdByUsername(username); // Get the ID of the current user
					List<Integer> followingIds = getFollowing(userId); // Get the list of users the current user is
																		// following
					if (!followingIds.isEmpty()) {
						System.out.println("Users you are following:");
						for (int followeeId : followingIds) {
							String followeeUsername = getUsernameById(followeeId); // Get the username of the followee
							System.out.println("- " + followeeUsername);
						}
					} else {
						System.out.println("You are not following anyone.");
					}
				} catch (SQLException e) {
					System.out.println("Error retrieving following list: " + e.getMessage());
				}

				break;
			case 4:
				try {
					int userId = getUserIdByUsername(username); // Get the ID of the current user
					List<Integer> followerIds = getFollowers(userId); // Get the list of users following the current
																		// user
					if (!followerIds.isEmpty()) {
						System.out.println("Users following you:");
						for (int followerId : followerIds) {
							String followerUsername = getUsernameById(followerId); // Get the username of the follower
							System.out.println("- " + followerUsername);
						}
					} else {
						System.out.println("You have no followers.");
					}
				} catch (SQLException e) {
					System.out.println("Error retrieving followers list: " + e.getMessage());
				}

				break;
			case 5:
				System.out.print("Enter their username to see activity");
				String followUserName = scanner.next();
				try {
					FollowManager.printUserActivity(followUserName);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 6:

				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

		} while (choice != 6);

	}

	public static void followUser(int followerId, int followeeId) throws SQLException {
		String sql = "INSERT INTO followers (follower_id, followee_id) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, followerId);
			pstmt.setInt(2, followeeId);

			pstmt.executeUpdate();
		}
	}

	public static void unfollowUser(int followerId, int followeeId) throws SQLException {
		String sql = "DELETE FROM followers WHERE follower_id = ? AND followee_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, followerId);
			pstmt.setInt(2, followeeId);

			pstmt.executeUpdate();
		}
	}

	public static List<Integer> getFollowers(int userId) throws SQLException {
		List<Integer> followers = new ArrayList<>();
		String sql = "SELECT follower_id FROM followers WHERE followee_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					followers.add(rs.getInt("follower_id"));
				}
			}
		}

		return followers;
	}

	public static List<Integer> getFollowing(int userId) throws SQLException {
		List<Integer> following = new ArrayList<>();
		String sql = "SELECT followee_id FROM followers WHERE follower_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					following.add(rs.getInt("followee_id"));
				}
			}
		}

		return following;
	}

	public static void printUserActivity(String username) throws SQLException {
		// Query to retrieve the user's posts
		String sql = "SELECT * FROM posts WHERE user_id = (SELECT id FROM user_profiles WHERE username = ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);

			try (ResultSet rs = pstmt.executeQuery()) {
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
	}

	public static int getUserIdByUsername(String username) throws SQLException {
		String sql = "SELECT id FROM user_profiles WHERE username = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("id");
				} else {
					return -1; // User not found
				}
			}
		}
	}

	public static String getUsernameById(int userId) throws SQLException {
		String sql = "SELECT username FROM user_profiles WHERE id = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("username");
				} else {
					return null; // User not found
				}
			}
		}
	}

}
