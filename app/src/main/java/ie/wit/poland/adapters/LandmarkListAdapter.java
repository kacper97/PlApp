package ie.wit.poland.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.Query;

import java.text.DecimalFormat;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;


public class LandmarkListAdapter extends FirebaseListAdapter<Landmark> {
    public Query query ;


    public LandmarkListAdapter(@NonNull FirebaseListOptions<Landmark> options) {
        super(options);
       // int landmarkrow = R.layout.landmarkrow;
       // Log.v("coffeemate","Creating Adapter with :" + query);
      //  this.query = query;
    }

    @Override
    protected void populateView(View row, Landmark landmark,int position) {
        Log.v("coffeemate","Populating View Adapter with :" + landmark);
        //Set the rows TAG to the coffee 'key'
        row.setTag(getRef(position).getKey());

        ((TextView) row.findViewById(R.id.rowLandmarkName)).setText(landmark.landmarkName);
        ((TextView) row.findViewById(R.id.rowLandmarkDescription)).setText(landmark.landmarkDescription);
        ((TextView) row.findViewById(R.id.rowRating)).setText(landmark.ratingFacility + " *");
        ((TextView) row.findViewById(R.id.rowPrice)).setText("€" +
                new DecimalFormat("0.00").format(landmark.price));

        ImageView imgIcon = (ImageView) row.findViewById(R.id.rowFavouriteImg);

        if (landmark.favourite == true)
            imgIcon.setImageResource(R.drawable.favourites_72);
        else
            imgIcon.setImageResource(R.drawable.favourites_72_on);


        //ImageView imgDelete = (ImageView) row.findViewById(R.id.rowFavouriteImg);
      //  imgDelete.setTag(getRef(position).getKey());
        //imgDelete.setOnClickListener(imgDelete);
    }
}