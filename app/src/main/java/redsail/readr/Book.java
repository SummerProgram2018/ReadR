package redsail.readr;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Book {

    public String title;
    public String author;
    public String genre;
    public int version;
    public String ISBN;
    public float rating;
    public String tradeID;
    public String description;
    public String price;

    public Book(){}

    public Book(String bookTitle, String ISBN) {
        this.title = bookTitle;
        this.ISBN = ISBN;
    }

}