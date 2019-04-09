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
    public String googlephoto;
    public String usertoken;
    public String address;
    public Marker marker = new Marker();

    public Landmark(String landmarkName,String landmarkDescription, double price, String location,
                    double ratingLandmark, double ratingTransport, double ratingFacility, String dateVisited,boolean fav,String photo, String token,
                    String address,double lat, double lng){
       // this.landmarkId=UUID.randomUUID().toString();
        this.landmarkName=landmarkName;
        this.landmarkDescription=landmarkDescription;
        this.price=price;
        this.location=location;
        this.ratingLandmark=ratingLandmark;
        this.ratingTransport=ratingTransport;
        this.ratingFacility=ratingFacility;
        this.dateVisited=dateVisited;
        this.favourite = fav;
        this.googlephoto = photo;
        this.usertoken = token;
        this.address = address;
        this.marker.coords.latitude = lat;
        this.marker.coords.longitude = lng;
    }

    public Landmark(){
        this.landmarkName="";
        this.landmarkDescription ="";
        this.ratingLandmark =0;
        this.ratingFacility=0;
        this.ratingTransport=0;
        this.favourite=false;
        this.location="";
        this.price=0.0;
        this.dateVisited=""
        this.googlephoto = "";
        this.usertoken = "";
        this.address = "";
        this.marker.coords.latitude = 0.0;
        this.marker.coords.longitude = 0.0;
    }

    @Override
    public String toString(){
        return landmarkId+" "+landmarkName+","+landmarkDescription+","+price
                +","+location+","+ ratingLandmark+","+ratingTransport+
                ","+ratingFacility+","+dateVisited+","+favourite +" "
                + usertoken + " " + address + " " + marker.coords.latitude
                + " " + marker.coords.longitude + "]";
    }


}
