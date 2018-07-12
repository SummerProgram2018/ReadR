package redsail.readr;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Book {
    public int ratingNum;
    public String src;
    public String title;
    public String author;
    public String genre;
    public int version;
    public String ISBN;
    public float rating;
    public String description;
    public float price;

    public Book(){}

    public Book(String bookTitle, String genre, String ISBN, String author, String description, float price) {
        this.title = bookTitle;
        this.genre = genre;
        this.ISBN = ISBN;
        this.author = author;
        this.description = description;
        this.price = price;
    }

    public void addCover(String src) {
        this.src = src;
    }

    public void rateBook(int rate) {
        this.rating = ((this.rating*this.ratingNum)+rate)/(this.ratingNum+1);
        this.ratingNum += 1;
    }

}