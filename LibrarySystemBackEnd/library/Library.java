package library;

import java.util.ArrayList;
import java.util.List;

class Library {
	private List<Book> books;
	private List<Patron> patrons;
	private List<Transaction> transactions;

	public Library() {
		this.books = new ArrayList<>();
		this.patrons = new ArrayList<>();
		this.transactions = new ArrayList<>();
	}

	public void addBook(Book book) {
		books.add(book);
	}

	public void removeBook(Book book) {
		books.remove(book);
	}

	public void registerPatron(Patron patron) {
		patrons.add(patron);
	}

	public void removePatron(Patron patron) {
		patrons.remove(patron);
	}

	public Transaction checkoutBook(Patron patron, Book book) {
		if (book.isAvailable()) {
			book.setAvailable(false);
			patron.addBorrowHistory(book);
			Transaction transaction = new Transaction(book, patron);
			transactions.add(transaction);
			return transaction;
		}
		return null;
	}

	public Transaction returnBook(Patron patron, String ISBN) {
        Book book = findBookByISBN(ISBN);
        
		if (!book.isAvailable() && patron.borrowHistory.contains(book)) {
			book.setAvailable(true);
			patron.removeBorrowHistory(book);
			Transaction transaction = new Transaction(book, patron);
			transactions.add(transaction);
			return transaction;
		}
		return null;
	}

	public Book findBookByISBN(String ISBN) {
		for (Book book : books) {
			if (book.getISBN().equals(ISBN)) {
				return book;
			}
		}
		return null;
	}

	public Patron findPatronByName(String name) {
		for (Patron patron : patrons) {
			if (patron.getName().equalsIgnoreCase(name)) {
				return patron;
			}
		}
		return null;
	}
}
