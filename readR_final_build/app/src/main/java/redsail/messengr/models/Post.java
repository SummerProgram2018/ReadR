package redsail.messengr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String title;
    public String body;
    public int starCount = 0;
    public String imageURL;
    public HashMap<String, String> location;
    public Map<String, Boolean> stars = new HashMap<>();
    public String address;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String body, String imageURL, HashMap<String, String> location, String address) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.imageURL = imageURL;
        this.location = location;
        this.address = address;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("imageURL", imageURL);
        result.put("location", location);
        result.put("address", address);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
