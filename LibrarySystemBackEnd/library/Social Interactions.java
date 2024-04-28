import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private static int nextId = 1;

    private int id;
    private String content;
    private UserProfile author;
    private Date timestamp;
    private List<Comment> comments;
    private int likes;

    public Post(String content, UserProfile author, Date timestamp) {
        this.id = nextId++;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.comments = new ArrayList<>();
        this.likes = 0;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void likePost() {
        likes++;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public void displayPostDetails() {
        System.out.println("Post ID: " + id);
        System.out.println("Content: " + content);
        System.out.println("Posted by: " + author.getPatron().getName() + " on " + timestamp);
        System.out.println("Likes: " + likes);
        System.out.println("Comments:");
        for (Comment comment : comments) {
            System.out.println(comment.getContent() + " - by " + comment.getAuthor().getPatron().getName());
        }
    }
}

public class Comment {
    private String content;
    private UserProfile author;
    private Post post;
    private Date timestamp;

    public Comment(String content, UserProfile author, Post post, Date timestamp) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public Post getPost() {
        return post;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void displayCommentDetails() {
        System.out.println("Comment by: " + author.getPatron().getName() + " at " + timestamp);
        System.out.println("Content: " + content);
    }
}
