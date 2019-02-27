package ie.wit.poland.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;

public class Edit extends Base {
    public Context context;
    public Landmark aLandmark;
    public boolean isFavourite;
    public ImageView editFavourite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        context = this;
        activityInfo = getIntent().getExtras();
        aLandmark = getLandmarkObject(activityInfo.getString("landmarkId"));

        Log.v("landmark", "EDIT : " + aLandmark);

        ((EditText)findViewById(R.id.editLandmarkName)).setText(aLandmark.landmarkName);
        ((EditText)findViewById(R.id.editDescription)).setText(aLandmark.landmarkDescription);
        ((EditText)findViewById(R.id.editLandmarkPrice)).setText(""+aLandmark.price);
        ((EditText)findViewById(R.id.editLandmarkLocation)).setText(aLandmark.location);
        ((RatingBar) findViewById(R.id.editRatingBarLandmark)).setRating((float)aLandmark.ratingLandmark);
        ((RatingBar) findViewById(R.id.editRatingBarFacilities)).setRating((float)aLandmark.ratingFacility);
        ((RatingBar) findViewById(R.id.editRatingBarTransport)).setRating((float)aLandmark.ratingTransport);
        ((EditText)findViewById(R.id.editLandmarkDate)).setText(aLandmark.dateVisited);

        editFavourite = findViewById(R.id.editFavourite);

        if (aLandmark.favourite==true) {
            editFavourite.setImageResource(R.drawable.favourites_72_on);
            isFavourite = true;
        } else {
            editFavourite.setImageResource(R.drawable.favourites_72);
            isFavourite = false;
        }
    }

    private Landmark getLandmarkObject(String id) {

        for (Landmark l : app.landmarkList)
            if (l.landmarkId.equalsIgnoreCase(id))
                return l;

        return null;
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

            startActivity(new Intent(this,Home.class));
            // Update coffee & return home
        } else
            Toast.makeText(this, "You must Enter Something for empty boxes",Toast.LENGTH_SHORT).show();
    }

    public void toggle (View view) {
        if (isFavourite) {
            aLandmark.favourite = false;
            Toast.makeText(this,"Removed From Favourites",Toast.LENGTH_SHORT).show();
            isFavourite = false;
            editFavourite.setImageResource(R.drawable.favourites_72);
        } else {
            aLandmark.favourite = true;
            Toast.makeText(this,"Added to Favourites !!",Toast.LENGTH_SHORT).show();
            isFavourite = true;
            editFavourite.setImageResource(R.drawable.favourites_72_on);
        }
    }
}
