package library;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Patron extends User {
	private UUID id;
	private String name;
	private String contactInfo;
	protected List<Book> borrowHistory;

	public Patron() {
		this.id = UUID.randomUUID();
		this.name = "";
		this.contactInfo = "";
		this.borrowHistory = new ArrayList<>();
	}

	public Patron(UUID id, String name, String contactInfo) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.contactInfo = contactInfo;
		this.borrowHistory = new ArrayList<>();
	}

	public List<Book> getBorrowHistory() {
		return borrowHistory;
	}

	public void addBorrowHistory(Book book) {
		borrowHistory.add(book);
	}

	public void removeBorrowHistory(Book book) {
		borrowHistory.remove(book);
	}

	public void displayBorrowHistory() {
		System.out.println("Borrowing History for " + getUsername() + ":");
		for (Book book : borrowHistory) {
			System.out.println(book.getTitle());
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public static class Student extends Patron {
		private int yearOfGrad;
		private final int MAX_BOOKS_ALLOWED = 10;

		public Student(UUID id, String name, String contactInfo, int yearOfGrad) {
			super(id, name, contactInfo);
			this.yearOfGrad = yearOfGrad;
		}

		public int getYearOfGrad() {
			return yearOfGrad;
		}

		public void setYearOfGrad(int yearOfGrad) {
			this.yearOfGrad = yearOfGrad;
		}

		public int getMaxBooks() {
			return MAX_BOOKS_ALLOWED;
		}
	}

	public static class Faculty extends Patron {
		private final int MAX_BOOKS_ALLOWED = 50;

		public Faculty(UUID id, String name, String contactInfo) {
			super(id, name, contactInfo);
		}

		public int getMaxBooks() {
			return MAX_BOOKS_ALLOWED;
		}
	}
}
