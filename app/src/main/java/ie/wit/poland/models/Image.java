package ie.wit.poland.models;

import com.google.firebase.database.Exclude;

public class Image {
    private String mName;
    private String mImageUrl;
    private String mKey;

    public Image(){
        // empty needed
    }

    public Image (String name, String imageUrl){
       // avoid empty string
        if ( name.trim().equals("")){
            name ="No Name";
        }
        mName=name;
        mImageUrl=imageUrl;

    }

    public String getName(){
        return mName;
    }

    public void setName (String name){
        mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl( String imageUrl){
        mImageUrl =imageUrl;
    }
    @Exclude //not in database
    public String getKey(){
        return mKey;
    }
    @Exclude //not in database
    public void setKey(String key){
        mKey = key;
    }
}
