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
            patron.addBorrowedBook(book);
            Transaction transaction = new Transaction(patron, book);
            transactions.add(transaction);
            return transaction;
        }
        return null;
    }

    public void returnBook(Transaction transaction) {
        transaction.returnBook();
    }
}
