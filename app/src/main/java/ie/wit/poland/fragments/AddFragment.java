package ie.wit.poland.fragments;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shashank.sony.fancytoastlib.FancyToast;


import java.io.IOException;
import java.util.List;

import ie.wit.poland.R;
import ie.wit.poland.activities.Home;
import ie.wit.poland.main.LandmarkApp;
import ie.wit.poland.models.Landmark;

public class AddFragment extends Fragment implements OnMapReadyCallback ,View.OnClickListener {

    private String landmarkName, landmarkDescription, location, dateVisited;
    private double price, ratingLandmark, ratingTransport, ratingFacility;
    private Button save;
    private EditText name, description, priceAdult, date, locate;
    private RatingBar rateLandmark, rateTransport, rateFacility;
    private LandmarkApp app = LandmarkApp.getInstance();
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
        save.setOnClickListener(this);

        return v;

    }
    public void onClick(View v) {
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
            Landmark l = new Landmark(app.FirebaseDB.mFBUserId,landmarkName, landmarkDescription, price, location, ratingLandmark,
                    ratingTransport, ratingFacility, dateVisited, false,app.googlePhotoURL,
                    app.googleToken,getAddressFromLocation(app.mCurrentLocation),
                    app.mCurrentLocation.getLatitude(),app.mCurrentLocation.getLongitude());

            app.FirebaseDB.addLandmark(l);
            FancyToast.makeText(
                    this.getActivity(),"Added Successfully", Toast.LENGTH_LONG,FancyToast.INFO,true).show();
            startActivity(new Intent(this.getActivity(), Home.class));
        } else
            FancyToast.makeText(
                    this.getActivity(),
                    "You must Enter Something for "
                            + "\'Name\', \'Description\', \'Price\', \'location\', \'dateVisited\'",
                    Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        addLandmarks(app.landmarkList,googleMap);
    }

    public void addLandmarks(List<Landmark> list,GoogleMap mMap)
    {
        for(Landmark l : list)
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l.marker.coords.latitude, l.marker.coords.longitude))
                    .title(l.landmarkName + " €" + l.price)
                    .snippet(l.landmarkName + " " + l.location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logoapp)));
    }


    private String getAddressFromLocation( Location location ) {
        Geocoder geocoder = new Geocoder(getActivity());

        String strAddress = "";
        Address address;
        try {
            address = geocoder
                    .getFromLocation(location.getLatitude(), location.getLongitude(), 1)
                    .get(0);
            strAddress = address.getAddressLine(0) +
                    " " + address.getAddressLine(1) +
                    " " + address.getAddressLine(2);
        } catch (IOException e) {
        }

        return strAddress;
    }

}