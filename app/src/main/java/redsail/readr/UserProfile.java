package redsail.readr;

import android.app.usage.UsageEvents;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.*;
import java.time.*;

@IgnoreExtraProperties
public class UserProfile {
    public ArrayList<Book> collection;
    public String UID;
    public String name;
    public String bio = new String("Write something about yourself");
    public int currency;
    public LocalDate birthday;
    public String communityRole;
    public String occupation;
//    public ArrayList<Achievement> achievements;
//    public ArrayList<Conversation> conversations;
    public ArrayList<UsageEvents.Event> eventHistory;
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