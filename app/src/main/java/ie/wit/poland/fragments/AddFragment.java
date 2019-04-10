package ie.wit.poland.fragments;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;

import ie.wit.poland.R;
import ie.wit.poland.activities.Home;
import ie.wit.poland.main.LandmarkApp;
import ie.wit.poland.models.FirebaseListener;
import ie.wit.poland.models.Landmark;

public class AddFragment extends Fragment implements OnMapReadyCallback {
    private String landmarkName, landmarkDescription, location, dateVisited;
    private double price, ratingLandmark, ratingTransport, ratingFacility;
    private Button save;
    private EditText name, description, priceAdult, date, locate;
    private RatingBar rateLandmark, rateTransport, rateFacility;
    private LandmarkApp app;
    private GoogleMap mMap;
    public Home activity;
    public FirebaseListener mFBDBListener;
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
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.activity = (Home) context;
      app.FirebaseDB.attachListener(mFBDBListener);

    }

    @Override
    public void onDetach() {
        super.onDetach();
         app.FirebaseDB.getAllLandmarks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        getActivity().setTitle(R.string.addALandmark);
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
                    ratingTransport, ratingFacility, dateVisited, false,app.googlePhotoURL,
                    app.googleToken,getAddressFromLocation(app.mCurrentLocation),
                    app.mCurrentLocation.getLatitude(),app.mCurrentLocation.getLongitude());

          app.FirebaseDB.addLandmark(l);
            //startActivity(new Intent(this.getActivity(), Home.class));
            app.FirebaseDB.getAllLandmarks();
            resetFields();
        } else
            Toast.makeText(
                    this.getActivity(),
                    "You must Enter Something for "
                            + "\'Name\', \'Description\', \'Price\', \'location\', \'dateVisited\'",
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        app.FirebaseDB.getAllLandmarks();
    }

    public void addLandmarks(List<Landmark> list)
    {
        for(Landmark l : list)
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l.marker.coords.latitude, l.marker.coords.longitude))
                    .title(l.landmarkName + " €" + l.price)
                    .snippet(l.landmarkName + " " + l.location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logoapp)));
        app.FirebaseDB.getAllLandmarksSnapshot();
    }

    private void resetFields() {
        name.setText("");
        description.setText("");
        priceAdult.setText("");
        rateLandmark.setRating(1);
        rateFacility.setRating(1);
        rateTransport.setRating(1);
        name.requestFocus();
        name.setFocusable(true);
    }

    private String getAddressFromLocation( Location location ) {
        Geocoder geocoder = new Geocoder( getActivity() );

        String strAddress = "";
        Address address;
        try {
            address = geocoder
                    .getFromLocation( location.getLatitude(), location.getLongitude(), 1 )
                    .get( 0 );
            strAddress = address.getAddressLine(0) +
                    " " + address.getAddressLine(1) +
                    " " + address.getAddressLine(2);
        }
        catch (IOException e ) {
        }

        return strAddress;
    }
}