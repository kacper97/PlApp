package ie.wit.poland.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.poland.R;
import ie.wit.poland.activities.Home;
import ie.wit.poland.main.LandmarkApp;
import ie.wit.poland.models.Landmark;

public class AddFragment extends Fragment {
    private String landmarkName, landmarkDescription, location, dateVisited;
    private double price, ratingLandmark, ratingTransport, ratingFacility;
    private Button save;
    private EditText name, description, priceAdult, date, locate;
    private RatingBar rateLandmark, rateTransport, rateFacility;
    private LandmarkApp app;
    DatabaseReference databaseLandmarks;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (LandmarkApp) getActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        getActivity().setTitle(R.string.addALandmark);
        databaseLandmarks = FirebaseDatabase.getInstance().getReference("landmark");

        name = v.findViewById(R.id.addLandmarkName);
        description = v.findViewById(R.id.addDescription);
        priceAdult = v.findViewById(R.id.addLandmarkPrice);
        date = v.findViewById(R.id.addLandmarkDate);
        rateLandmark = v.findViewById(R.id.addRatingBarLandmark);
        rateTransport = v.findViewById(R.id.addRatingBarTransport);
        rateFacility = v.findViewById(R.id.addRatingBarFacilities);
        locate = v.findViewById(R.id.addLandmarkLocation);
        save = v.findViewById(R.id.addALandmarkBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLandmark();
            }
        });

        return v;

    }
    public void addLandmark() {
        landmarkName = name.getText().toString();
        landmarkDescription = description.getText().toString();
        try {
            price = Double.parseDouble(priceAdult.getText().toString());
        } catch (NumberFormatException e) {
            price = 0.0;
        }
        location = locate.getText().toString();
        ratingLandmark = rateLandmark.getRating();
        ratingTransport = rateTransport.getRating();
        ratingFacility = rateFacility.getRating();
        dateVisited = date.getText().toString();

        if ((landmarkName.length() > 0) && (landmarkDescription.length() > 0)
                && (priceAdult.length() > 0)) {
            Landmark l = new Landmark(landmarkName, landmarkDescription, price, location, ratingLandmark,
                    ratingTransport, ratingFacility, dateVisited, false);
            String id = databaseLandmarks.push().getKey();
            databaseLandmarks.child(id).setValue(l);
            Toast.makeText(this.getActivity()   ,"Added landmark",Toast.LENGTH_LONG).show();
            Log.v("Polish Landmark", "Add : " + app.landmarkList);
            app.landmarkList.add(l);

            startActivity(new Intent(this.getActivity(), Home.class));
        } else
            Toast.makeText(
                    this.getActivity(),
                    "You must Enter Something for "
                            + "\'Name\', \'Description\', \'Price\', \'location\', \'dateVisited\'",
                    Toast.LENGTH_SHORT).show();

    }
}