package ie.wit.poland.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;


import ie.wit.poland.R;
import ie.wit.poland.activities.Home;
import ie.wit.poland.main.LandmarkApp;
import ie.wit.poland.models.FirebaseListener;
import ie.wit.poland.models.Landmark;

public class EditFragment extends Fragment implements FirebaseListener {

    public Landmark aLandmark;
    public boolean isFavourite;
    public ImageView editFavourite;
    private EditText editLandmarkName,editDescription,editLandmarkPrice,editLandmarkLocation,editdateVisited;
    private RatingBar editRatingBarLandmark, editRatingBarFacility, editRatingBarTransport;
    public LandmarkApp app = LandmarkApp.getInstance();

    private OnFragmentInteractionListener mListener;
   public String      landmarkKey;


    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(Bundle landmarkBundle) {
        EditFragment fragment = new EditFragment();
        fragment.setArguments(landmarkBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void editLandmark(View v) {
        if (mListener != null) {
            String landmarkName  = editLandmarkName.getText().toString();
            String landmarkDescritpion = editDescription.getText().toString();
            String priceAdult = editLandmarkPrice.getText().toString();
           String location = editLandmarkLocation.getText().toString();
           double ratingLandmark = editRatingBarLandmark.getRating();
           double ratingTransport = editRatingBarTransport.getRating();
           double ratingFacility = editRatingBarFacility.getRating();
            String dateVisited = editdateVisited.getText().toString();

            double price;
            try {
                price = Double.parseDouble(priceAdult);
            } catch (NumberFormatException e)
            {            price = 0.0;        }

            if ((landmarkName.length() > 0) && (landmarkDescritpion.length() > 0) && (priceAdult.length() > 0)) {
                aLandmark.landmarkName = landmarkName;
                aLandmark.landmarkDescription = landmarkDescritpion;
                aLandmark.price = price;
                aLandmark.location = location;
                aLandmark.dateVisited = dateVisited;
                aLandmark.ratingLandmark = ratingLandmark;
                aLandmark.ratingFacility = ratingFacility;
                aLandmark.ratingTransport = ratingTransport;

            }
                app.FirebaseDB.updateALandmark(landmarkKey,aLandmark);


        } else
            Toast.makeText(getActivity(), "You must Enter Something for Name and Shop", Toast.LENGTH_SHORT).show();
    }


    public void toggle(View v) {
            app.FirebaseDB.toggleFavourite(landmarkKey,isFavourite);
        if (isFavourite) {
            aLandmark.favourite = false;
            Toast.makeText(getActivity(), "Removed From Favourites", Toast.LENGTH_SHORT).show();
            isFavourite = false;
            editFavourite.setImageResource(R.drawable.favourites_72);
        } else {
            aLandmark.favourite = true;
            Toast.makeText(getActivity(), "Added to Favourites !!", Toast.LENGTH_SHORT).show();
            isFavourite = true;
            editFavourite.setImageResource(R.drawable.favourites_72_on);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        app.FirebaseDB.attachListener(this);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void updateUI() {
        editLandmarkName.setText(aLandmark.landmarkName);
        editDescription.setText(aLandmark.landmarkDescription);
        editLandmarkLocation.setText(aLandmark.location);
        editdateVisited.setText(aLandmark.dateVisited);
        editLandmarkPrice.setText(""+aLandmark.price);
        editRatingBarLandmark.setRating((float)aLandmark.ratingLandmark);
        editRatingBarTransport.setRating((float)aLandmark.ratingTransport);
        editRatingBarFacility.setRating((float)aLandmark.ratingFacility);

        if (aLandmark.favourite == true) {
            editFavourite.setImageResource(R.drawable.favourites_72_on);
            isFavourite = true;
        } else {
            editFavourite.setImageResource(R.drawable.favourites_72);
            isFavourite = false;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
       // aLandmark = dataSnapshot.getValue(Landmark.class);
        }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        editLandmarkName = v.findViewById(R.id.editLandmarkName);
        editDescription =  v.findViewById(R.id.editDescription);
        editLandmarkPrice = v.findViewById(R.id.editLandmarkPrice);
        editLandmarkLocation =  v.findViewById(R.id.editLandmarkLocation);
        editdateVisited =  v.findViewById(R.id.editLandmarkDate);
        editRatingBarLandmark = v.findViewById(R.id.editRatingBarLandmark);
        editRatingBarTransport = v.findViewById(R.id.editRatingBarTransport);
        editRatingBarFacility =v.findViewById(R.id.editRatingBarFacilities);

        editLandmarkName.setText(aLandmark.landmarkName);
        editDescription.setText(aLandmark.landmarkDescription);
        editLandmarkPrice.setText(""+ aLandmark.price);
        editLandmarkLocation.setText(aLandmark.location);
        editdateVisited.setText(aLandmark.location);
        editRatingBarLandmark.setRating((float)aLandmark.ratingLandmark);
        editRatingBarTransport.setRating((float)aLandmark.ratingFacility);
        editRatingBarFacility.setRating((float)aLandmark.ratingTransport);

        editFavourite = v.findViewById(R.id.editFavourite);

        if (aLandmark.favourite==true) {
            editFavourite.setImageResource(R.drawable.favourites_72_on);
            isFavourite = true;
        } else {
            editFavourite.setImageResource(R.drawable.favourites_72);
            isFavourite = false;
        }
        return v;
    }

    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onFailure() {

    }

    public interface OnFragmentInteractionListener {
        void toggle(View v);
        void editLandmark(View v);
    }
}