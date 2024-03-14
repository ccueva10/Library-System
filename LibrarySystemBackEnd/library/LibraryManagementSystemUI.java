package library;

import java.util.Scanner;
import java.util.UUID;

public class LibraryManagementSystemUI {
	private Library library;
	private Scanner scanner;

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
		System.out.println("7. Exit");
	}

	public void start() {
		int choice;
		do {
			displayMenu();
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				removeBook();
				break;
			case 3:
				registerPatron(library);
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
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 7);
	}

	private void addBook() {
		System.out.print("Enter ISBN: ");
		String ISBN = scanner.nextLine();
		System.out.print("Enter Title: ");
		String title = scanner.nextLine();
		System.out.print("Enter Author: ");
		String author = scanner.nextLine();
		System.out.print("Enter Genre: ");
		String genre = scanner.nextLine();
		System.out.print("Is the book fiction or non-fiction? (Enter 'fiction' or 'nonfiction'): ");
		String bookType = scanner.nextLine();

		if (bookType.equalsIgnoreCase("fiction")) {
			System.out.print("Enter Fiction Type: ");
			String fictionType = scanner.nextLine();
			FictionBook book = new FictionBook(ISBN, title, author, genre, fictionType);
			library.addBook(book);
		} else if (bookType.equalsIgnoreCase("nonfiction")) {
			System.out.print("Enter Non-Fiction Type: ");
			String nonFicType = scanner.nextLine();
			NonFictionBook book = new NonFictionBook(ISBN, title, author, genre, nonFicType);
			library.addBook(book);
		} else {
			System.out.println("Invalid book type. Please enter either 'fiction' or 'nonfiction'.");
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

	private void registerPatron(Library library) {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.print("Is Patron Student or Faculty: Enter S or F: ");
			char type = scanner.next().charAt(0);
			scanner.nextLine();

			System.out.print("Enter name: ");
			String name = scanner.nextLine();

			System.out.print("Enter contact info: ");
			String contactInfo = scanner.nextLine();

			Patron patron = new Patron();

			if (type == 'S') {
				System.out.print("Enter year of graduation: ");
				int yearOfGrad = scanner.nextInt();
				scanner.nextLine();

				Patron.Student student = patron.new Student(UUID.randomUUID(), name, contactInfo, yearOfGrad);
				library.registerPatron(student);
			} else if (type == 'F') {
				Patron.Faculty faculty = patron.new Faculty(UUID.randomUUID(), name, contactInfo);
				library.registerPatron(faculty);
			} else {
				System.out.println("Invalid input. Please enter either S or F.");
			}

			System.out.println("Patron registered successfully.");
		} finally {
			scanner.close();
		}

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
		Book book = library.findBookByISBN(ISBN);
		if (book == null) {
			System.out.println("Book not found.");
			return;
		}

		Transaction transaction = library.returnBook(patron, book);
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
