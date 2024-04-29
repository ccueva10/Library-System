package library;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

import library_database.DataBaseManager;

public class LibraryManagementSystemUI {
	private final Library library;
	private final Scanner scanner;

	public LibraryManagementSystemUI() {
		this.library = new Library();
		this.scanner = new Scanner(System.in);
	}

	public void displayMenu() {
		System.out.println("Welcome to the Library Management System!");
		System.out.println("1. Add Book");
		System.out.println("2. Remove Book");
		System.out.println("3. Register Patron");
		System.out.println("4. Remove Patron");
		System.out.println("5. Checkout Book");
		System.out.println("6. Return Book");
		System.out.println("7. Enter BiblioConnect");
		System.out.println("8. Exit");
	}

	public void start() {
		int choice;
		do {
			displayMenu();
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				removeBook();
				break;
			case 3:
				registerPatron();
				break;
			case 4:
				removePatron();
				break;
			case 5:
				checkoutBook();
				break;
			case 6:
				returnBook();
				break;
			case 7:

				System.out.println("Enter 1: to Login or Enter 2: to Register to enter the social network ");
				choice = scanner.nextInt();
				switch (choice) {
				case 1:
					// Login
					System.out.println("Enter username:");
					String username = scanner.next();
					System.out.println("Enter password:");
					String password = scanner.next();
					User loginUser = new User(username, password);
					if (loginUser.loginUser()) {
						System.out.println("Login successful!");
						// Proceed with socialStart
						try {
							DataBaseManager.connect();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
						DataBaseManager.socialStart(username, password);
					} else {
						System.out.println("Login failed. Please try again.");
					}
					break;
				case 2:
					// Register
					System.out.println("Enter username:");
					String regUsername = scanner.next();
					System.out.println("Enter password:");
					String regPassword = scanner.next();
					User registerUser = new User(regUsername, regPassword);
					if (registerUser.registerUser()) {
						System.out.println("Registration successful!");
						// Proceed with socialStart
						try {
							DataBaseManager.connect();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
						DataBaseManager.socialStart(regUsername, regPassword);
					} else {
						System.out.println("Registration failed. Please try again.");
					}
					break;

				default:
					System.out.println("Invalid choice.");

				}

				break;
			case 8:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 8);
	}

	private void addBook() {
		String ISBN, title, author, genre;

		while (true) {
			System.out.print("Enter ISBN: ");
			ISBN = scanner.nextLine();
			System.out.print("Enter Title: ");
			title = scanner.nextLine();
			System.out.print("Enter Author: ");
			author = scanner.nextLine();
			System.out.print("Enter Genre: ");
			genre = scanner.nextLine();
			System.out.print("Is the book fiction or non-fiction? (Enter 'fiction' or 'nonfiction'): ");
			String bookType = scanner.nextLine();

			if (bookType.equalsIgnoreCase("fiction")) {
				System.out.print("Enter Fiction Type: ");
				String fictionType = scanner.nextLine();
				FictionBook book = new FictionBook(ISBN, title, author, genre, fictionType);
				library.addBook(book);
				break;
			} else if (bookType.equalsIgnoreCase("nonfiction")) {
				System.out.print("Enter Non-Fiction Type: ");
				String nonFicType = scanner.nextLine();
				NonFictionBook book = new NonFictionBook(ISBN, title, author, genre, nonFicType);
				library.addBook(book);
				break;
			} else {
				System.out.println("Invalid book type. Please enter either 'fiction' or 'nonfiction'.");
			}
		}
	}

	private void removeBook() {
		System.out.print("Enter ISBN of the book to remove: ");
		String ISBN = scanner.nextLine();

		Book book = library.findBookByISBN(ISBN);
		if (book != null) {
			library.removeBook(book);
			System.out.println("Book removed successfully.");
		} else {
			System.out.println("Book not found.");
		}
	}

	private void registerPatron() {
		System.out.print("Is Patron Student or Faculty: Enter S or F: ");
		char type = scanner.next().charAt(0);
		scanner.nextLine();

		while (type != 'S' && type != 'F') {
			System.out.println("Invalid input. Please enter either S or F.");
			System.out.print("Is Patron Student or Faculty: Enter S or F: ");
			type = scanner.next().charAt(0);
			scanner.nextLine();
		}

		System.out.print("Enter name: ");
		String name = scanner.nextLine();

		System.out.print("Enter contact info: ");
		String contactInfo = scanner.nextLine();

		if (type == 'S') {
			System.out.print("Enter year of graduation: ");
			int yearOfGrad = scanner.nextInt();
			scanner.nextLine();

			Patron.Student student = new Patron.Student(UUID.randomUUID(), name, contactInfo, yearOfGrad);
			library.registerPatron(student);

		} else if (type == 'F') {
			Patron.Faculty faculty = new Patron.Faculty(UUID.randomUUID(), name, contactInfo);
			library.registerPatron(faculty);
		}

		System.out.println("Patron registered successfully.");
	}

	private void removePatron() {
		System.out.print("Enter Patron Name to remove: ");
		String name = scanner.nextLine();

		Patron patron = library.findPatronByName(name);
		if (patron != null) {
			library.removePatron(patron);
			System.out.println("Patron removed successfully.");
		} else {
			System.out.println("Patron not found.");
		}
	}

	private void checkoutBook() {

		System.out.print("Enter Patron Name: ");
		String patronName = scanner.nextLine();
		Patron patron = library.findPatronByName(patronName);
		if (patron == null) {
			System.out.println("Patron not found.");
			return;
		}
		System.out.print("Enter ISBN of the book to checkout: ");
		String ISBN = scanner.nextLine();
		Book book = library.findBookByISBN(ISBN);
		if (book == null) {
			System.out.println("Book not found.");
			return;
		}

		Transaction transaction = library.checkoutBook(patron, book);
		if (transaction != null) {
			System.out.println("Book checked out successfully.");
		} else {
			System.out.println("Book is not available for checkout.");
		}
	}

	private void returnBook() {
		System.out.print("Enter Patron Name: ");
		String patronName = scanner.nextLine();
		Patron patron = library.findPatronByName(patronName);
		if (patron == null) {
			System.out.println("Patron not found.");
			return;
		}
		System.out.print("Enter ISBN of the book to return: ");
		String ISBN = scanner.nextLine();
		Transaction transaction = library.returnBook(patron, ISBN);
		if (transaction != null) {
			System.out.println("Book returned successfully.");
		} else {
			System.out.println("Book is not available for checkout.");
		}
	}

	public static void main(String[] args) {
		LibraryManagementSystemUI ui = new LibraryManagementSystemUI();
		ui.start();
	}
}
