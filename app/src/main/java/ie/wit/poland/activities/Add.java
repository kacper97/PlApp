package ie.wit.poland.activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;

public class Add extends Base {
    private String  landmarkName, landmarkDescription, location,dateVisited;
    private double price, ratingLandmark, ratingTransport, ratingFacility;
    private EditText name, description, priceAdult,date,locate;
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
        locate=findViewById(R.id.addLandmarkLocation);
    }

    public void addImage(View v){
        Toast.makeText(this,"Not implemented in this version",Toast.LENGTH_SHORT).show();
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
        location =  locate.getText().toString();
        ratingLandmark =rateLandmark.getRating();
        ratingTransport =rateTransport.getRating();
        ratingFacility =rateFacility.getRating();
        dateVisited=date.getText().toString();

        if ((landmarkName.length() > 0) && (landmarkDescription.length() > 0)
                && (priceAdult.length() > 0)) {
            Landmark l = new Landmark(landmarkName, landmarkDescription, price,location,ratingLandmark,
                    ratingTransport,ratingFacility, dateVisited,false);

            Log.v("Polish Landmark","Add : " + app.landmarkList);
            app.landmarkList.add(l);
            startActivity(new Intent(this, Home.class));
        } else
            Toast.makeText(
                    this,
                    "You must Enter Something for "
                            + "\'Name\', \'Description\', \'Price\', \'location\', \'dateVisited\'",
                    Toast.LENGTH_SHORT).show();
    }
}
