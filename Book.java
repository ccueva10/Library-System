import java.util.UUID;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private boolean available;
    
    public Book(String ISBN, String title, String author, String genre) {
        this.ISBN = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = true;
    }
}
    public String getISBN() {
        return ISBN;
    }
    
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    
    public String getTitle() {
        return title;
    }
    
     public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
     public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getGenre() {
        return genre;
    }
    
     public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
}

class FictionBook extends Book {
    private String fictionType;
    
    public FictionBook(String ISBN, String title, String author, String genre, String fictionType) {
        super(ISBN, title, author, genre);
        this.fictionType = fictionType;
    }
    
     public String getFictionType() {
        return fictionType;
    }

    public void setFictionType(String fictionType) {
        this.fictionType = fictionType;
    }
}

class NonFictionBook extends Book {
    private String nonFicType;

    public NonFictionBook(String ISBN, String title, String author, String genre, String nonFicType) {
        super(ISBN, title, author, genre);
        this.nonFicType = nonFicType;
    }

    public String getNonFicType() {
        return nonFicType;
    }

    public void setNonFicType(String nonFicType) {
        this.nonFicType = nonFicType;
    }
}
