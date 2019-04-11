package ie.wit.poland.models;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    public String userName;
    public String userId;
    public String email;
    public String profilePic;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String userEmail, String userProfilePic,String userName ){
        this.userId = userId;
        this.userName = userName;
        this.email = userEmail;
        this.profilePic = userProfilePic;
    }

}