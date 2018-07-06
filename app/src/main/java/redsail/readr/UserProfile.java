package redsail.readr;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserProfile {

    public ArrayList<Book> collection;
    public String UID;
    public String name;
    public String bio = New String("Write something about yourself");
    public int currency;
    public LocalDate birthday;
    public String communityRole;
    public String occupation;
    public ArrayList<Achievement> achievements;
    public ArrayList<Converstion> converstions;
    public ArrayList<Event> eventHistory;
    public ArrayList<Book> wishList;
    public ArrayList<Book> recommendations;
    public ArrayList<String> notes;
    public int rating;


    public UserProfile(){}

    public UserProfile(String name, String userId) {
        this.name = name;
        this.UID = userId;
    }

}