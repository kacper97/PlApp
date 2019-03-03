package ie.wit.poland.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Landmark implements Serializable {
    public String landmarkId;
    public String landmarkName;
    public String landmarkDescription;
    public double price;
    public String location;
    public double ratingLandmark;
    public double ratingTransport;
    public double ratingFacility;
    public String dateVisited;
    public boolean favourite;

    public Landmark(){}

    public Landmark(String landmarkName,String landmarkDescription, double price, String location,
                    double ratingLandmark, double ratingTransport, double ratingFacility, String dateVisited,boolean fav){
        this.landmarkId=UUID.randomUUID().toString();
        this.landmarkName=landmarkName;
        this.landmarkDescription=landmarkDescription;
        this.price=price;
        this.location=location;
        this.ratingLandmark=ratingLandmark;
        this.ratingTransport=ratingTransport;
        this.ratingFacility=ratingFacility;
        this.dateVisited=dateVisited;
        this.favourite = fav;
    }

    @Override
    public String toString(){
        return landmarkId+" "+landmarkName+","+landmarkDescription+","+price
                +","+location+","+ ratingLandmark+","+ratingTransport+
                ","+ratingFacility+","+dateVisited+","+favourite;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", landmarkId);
        result.put("landmarkname", landmarkName);
        result.put("landmarkdescription", landmarkDescription);
        result.put("price", price);
        result.put("location", location);
        result.put("ratinglandmark", ratingLandmark);
        result.put("ratingtransport", ratingTransport);
        result.put("ratingfacility", ratingFacility);
        result.put("dateVisited", dateVisited);
        result.put("fav", favourite);

        return result;
    }

}
