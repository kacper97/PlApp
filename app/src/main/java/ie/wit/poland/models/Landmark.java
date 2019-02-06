package ie.wit.poland.models;

import java.io.Serializable;

public class Landmark implements Serializable {
    public int landmarkId;
    public String landmarkName;
    public String landmarkDescription;
    public double price;
    public String location;
    public double ratingLandmark;
    public double ratingTransport;
    public double ratingFacility;
    public String dateVisited;

    public Landmark(){}

    public Landmark(String landmarkName,String landmarkDescription, double price, String location,
                    double ratingLandmark, double ratingTransport, double ratingFacility, String dateVisited){
        this.landmarkName=landmarkName;
        this.landmarkDescription=landmarkDescription;
        this.price=price;
        this.location=location;
        this.ratingLandmark=ratingLandmark;
        this.ratingTransport=ratingTransport;
        this.ratingFacility=ratingFacility;
        this.dateVisited=dateVisited;
    }

    @Override
    public String toString(){
        return landmarkName+","+landmarkDescription+","+price
                +","+location+","+ ratingLandmark+","+ratingTransport+
                ","+ratingFacility+","+dateVisited;
    }

}
