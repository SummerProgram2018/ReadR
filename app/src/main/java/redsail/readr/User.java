package redsail.readr;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public UserProfile profile;

    public User(){}

    public User(String userId, String username, String email, String name) {
        this.username = username;
        this.email = email;
        this.profile = new UserProfile(name, userId);
    }

}