import java.time.LocalDate;

// Transaction class
public class Transaction {
    private Book book;
    private Patron patron;
    private LocalDate checkoutDate;
    private LocalDate returnDate;

    // Constructor
    public Transaction(Book book, Patron patron) {
        this.book = book;
        this.patron = patron;
        this.checkoutDate = LocalDate.now(); 
        this.returnDate = null; // 
    }

    // Getters and setters
    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    // Method to mark the book as returned
    public void markAsReturned() {
        this.returnDate = LocalDate.now(); // Set the return date to the current date
    }
}
