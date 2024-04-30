package library_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EventManager {

	private static Scanner scanner;
	static {
		scanner = new Scanner(System.in);
	}

	public static void eventMenu() {
		System.out.println("Welcome to the Event Manager");
		System.out.println("1. Host Literary Event");
		System.out.println("2. Register for Event");
		System.out.println("3. View Events");
		System.out.println("4. Back to Social Menu");
		System.out.println("");
	}

	public static void startEventOptions(String username, String password) {
		int choice;
		do {
			eventMenu();
			System.out.print("Enter your choice:");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				hostLiteraryEvent(username);
				break;
			case 2:
				registerForEvent(username);
				break;
			case 3:
				viewEvents();
				break;
			case 4:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}

		} while (choice != 4);
	}

	public static void viewEvents() {
		try {
			Connection conn = DataBaseManager.connect();

			String sql = "SELECT * FROM events";
			try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("Available Events:");
					do {
						int eventId = rs.getInt("id");
						String eventName = rs.getString("name");
						String eventDescription = rs.getString("description");
						String eventDate = rs.getString("date");

						System.out.println("Event ID: " + eventId);
						System.out.println("Name: " + eventName);
						System.out.println("Description: " + eventDescription);
						System.out.println("Date: " + eventDate);
						System.out.println();
					} while (rs.next());
				} else {
					System.out.println("No events available.");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error viewing events: " + e.getMessage());
		}
	}

	public static void hostLiteraryEvent(String username) {
		try {
			Connection conn = DataBaseManager.connect();

			System.out.print("Enter the name of the event: ");
			String eventName = scanner.nextLine();
			System.out.print("Enter a description of the event: ");
			String eventDescription = scanner.nextLine();
			System.out.print("Enter the date of the event (YYYY-MM-DD HH:MM:SS): ");
			String eventDate = scanner.nextLine();

			String sql = "INSERT INTO events (name, description, date) VALUES (?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, eventName);
				pstmt.setString(2, eventDescription);
				pstmt.setString(3, eventDate);

				pstmt.executeUpdate();
				System.out.println("Event hosted successfully!");
			}
		} catch (SQLException e) {
			System.out.println("Error hosting event: " + e.getMessage());
		}
	}

	public static void registerForEvent(String username) {
		try {
			Connection conn = DataBaseManager.connect();

			System.out.print("Enter the ID of the event you want to register for: ");
			int eventId = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			// Check if the event exists
			if (eventExists(eventId)) {
				String sql = "INSERT INTO event_attendees (event_id, user_id) VALUES (?, ?)";
				try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setInt(1, eventId);
					pstmt.setString(2, username);

					pstmt.executeUpdate();
					System.out.println("Registered for the event successfully!");
				}
			} else {
				System.out.println("Event does not exist.");
			}
		} catch (SQLException e) {
			System.out.println("Error registering for event: " + e.getMessage());
		}
	}

	private static boolean eventExists(int eventId) throws SQLException {
		Connection conn = DataBaseManager.connect();
		String sql = "SELECT * FROM events WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, eventId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
}
