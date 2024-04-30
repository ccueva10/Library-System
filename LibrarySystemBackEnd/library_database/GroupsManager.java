package library_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GroupsManager {

	private static Scanner scanner;
	static {
		scanner = new Scanner(System.in);
	}

	public static void displayGroupsMenu() {
		System.out.println("Groups and Discussions:");
		System.out.println("1. Join a Group");
		System.out.println("2. Create a Group");
		System.out.println("3. View Group Discussions");
		System.out.println("4. View All Groups and Discussions");
		System.out.println("5. Back to Social Menu");
	}

	public static void groupsStart() {
		int choice;
		do {
			displayGroupsMenu();
			System.out.print("Enter your choice:");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				joinGroup();
				break;
			case 2:
				createGroup();
				break;
			case 3:
				viewGroupDiscussions();
				break;
			case 4:
				displayAllGroupsAndDiscussions();
				break;
			case 5:
				System.out.println("Returning to Social Menu...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

		} while (choice != 5);
	}

	private static void joinGroup() {
		System.out.println("Enter the name of the group you want to join:");
		String groupName = scanner.nextLine();
		System.out.println("Enter your username:");
		String username = scanner.nextLine();
		try {
			joinGroup(groupName, username);
			System.out.println("Joined group successfully!");
		} catch (SQLException e) {
			System.out.println("Error joining group: " + e.getMessage());
		}
	}

	private static void createGroup() {
		scanner.nextLine();
		System.out.println("Enter the name of the group:");
		String groupName = scanner.nextLine();
		System.out.println("Enter the description of the group:");
		String groupDescription = scanner.nextLine();
		try {
			createGroup(groupName, groupDescription);
			System.out.println("Group created successfully!");
		} catch (SQLException e) {
			System.out.println("Error creating group: " + e.getMessage());
		}
	}

	private static void viewGroupDiscussions() {
		System.out.println("Enter the name of the group to view discussions:");
		String groupName = scanner.nextLine();
		try {
			List<String> discussions = viewGroupDiscussions(groupName);
			System.out.println("Group Discussions:");
			for (String discussion : discussions) {
				System.out.println(discussion);
			}
		} catch (SQLException e) {
			System.out.println("Error viewing group discussions: " + e.getMessage());
		}
	}

	public static void displayAllGroupsAndDiscussions() {
		try {
			List<String> groups = getAllGroups();
			System.out.println("All Groups and Discussions:");
			for (String group : groups) {
				System.out.println("Group: " + group);
				List<String> discussions = viewGroupDiscussions(group);
				System.out.println("Discussions:");
				for (String discussion : discussions) {
					System.out.println("\t- " + discussion);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error displaying groups and discussions: " + e.getMessage());
		}
	}

	public static void joinGroup(String groupName, String username) throws SQLException {
		String sql = "INSERT INTO user_groups (user_id, group_id) " + "SELECT u.user_id, g.group_id "
				+ "FROM users u, groups g " + "WHERE u.username = ? AND g.group_name = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			pstmt.setString(2, groupName);
			pstmt.executeUpdate();
		}
	}

	public static void createGroup(String groupName, String groupDescription) throws SQLException {
		String sql = "INSERT INTO groups (group_name, description) VALUES (?, ?)";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, groupName);
			pstmt.setString(2, groupDescription);
			pstmt.executeUpdate();
		}
	}

	public static List<String> viewGroupDiscussions(String groupName) throws SQLException {
		List<String> discussions = new ArrayList<>();
		String sql = "SELECT content FROM group_discussions "
				+ "JOIN groups ON group_discussions.group_id = groups.group_id " + "WHERE groups.group_name = ?";
		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, groupName);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					discussions.add(rs.getString("content"));
				}
			}
		}
		return discussions;
	}

	public static List<String> getAllGroups() throws SQLException {
		List<String> groups = new ArrayList<>();
		String sql = "SELECT group_name FROM groups";
		try (Connection conn = DataBaseManager.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				groups.add(rs.getString("group_name"));
			}
		}
		return groups;
	}
}