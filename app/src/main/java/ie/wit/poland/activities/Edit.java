package ie.wit.poland.activities;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;

public class Edit extends Base {
    public Context context;
    public Landmark aLandmark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        context = this;
    }

    public void editLandmark(View v) {
        String landmarkName = ((EditText) findViewById(R.id.editLandmarkName)).getText().toString();
        String landmarkDescription = ((EditText) findViewById(R.id.editDescription)).getText().toString();
        String priceAdult = ((EditText) findViewById(R.id.editLandmarkPrice)).getText().toString();
        String location = ((EditText) findViewById(R.id.editLandmarkLocation)).getText().toString();
        String dateVisited = ((EditText) findViewById(R.id.editLandmarkDate)).getText().toString();
        double ratingLandmark =((RatingBar) findViewById(R.id.editRatingBarLandmark)).getRating();
        double ratingTransport =((RatingBar) findViewById(R.id.editRatingBarTransport)).getRating();
        double ratingFacility =((RatingBar) findViewById(R.id.editRatingBarFacilities)).getRating();
        double price;

        try {
            price = Double.parseDouble(priceAdult);
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        if ((landmarkName.length() > 0) && (landmarkDescription.length() > 0) && (priceAdult.length() > 0)) {
            aLandmark.landmarkName = landmarkName;
            aLandmark.landmarkDescription = landmarkDescription;
            aLandmark.price = price;
            aLandmark.location = location;
            aLandmark.dateVisited= dateVisited;
            aLandmark.ratingFacility = ratingFacility;
            aLandmark.ratingTransport = ratingTransport;
            aLandmark.ratingLandmark= ratingLandmark;
            // Update coffee & return home
        } else
            Toast.makeText(this, "You must Enter Something for Name and Shop",Toast.LENGTH_SHORT).show();
    }
}
