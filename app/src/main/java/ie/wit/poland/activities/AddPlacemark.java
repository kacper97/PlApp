package ie.wit.poland.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import ie.wit.poland.R;

public class AddPlacemark extends Base {
    private String  landmarkName, landmarkDescription, location,dateVisited;
    private double price, ratingLandmark, ratingTransport, ratingFacility;
    private EditText name, description, priceAdult,date;
    private RatingBar rateLandmark, rateTransport, rateFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        name = findViewById(R.id.addLandmarkName);
        description= findViewById(R.id.addDescription);
        priceAdult =findViewById(R.id.addLandmarkPrice);
        date= findViewById(R.id.addLandmarkDate);
        rateLandmark =findViewById(R.id.addRatingBarLandmark);
        rateTransport=findViewById(R.id.addRatingBarTransport);
        rateFacility=findViewById(R.id.addRatingBarFacilities);
    }

    public void addLandmark(View v){
        landmarkName= name.getText().toString();
        landmarkDescription= description.getText().toString();
        try{
            price = Double.parseDouble(priceAdult.getText().toString());
        }
        catch (NumberFormatException e){
            price = 0.0;
        }
        ratingLandmark =rateLandmark.getRating();
        ratingTransport =rateTransport.getRating();
        ratingFacility =rateFacility.getRating();
        dateVisited=date.getText().toString();
    }
}
