package ie.wit.poland.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import java.text.DecimalFormat;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;


public class LandmarkListAdapter extends FirebaseListAdapter<Landmark> {

    private OnClickListener deleteListener;
    public Query query;

    public LandmarkListAdapter(Activity context, OnClickListener deleteListener,
                               Query query) {
        super(context, Landmark.class,R.layout.landmarkrow, query);
        Log.v("landmark", "Creating Adapter with :" + query);
        this.deleteListener = deleteListener;
        this.query = query;
    }

    @Override
    protected void populateView(View row, Landmark landmark, int position) {
        Log.v("Landmark", "Populating View Adapter with :" + landmark);
        //Set the rows TAG to the landmark 'key'
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

        ImageView imgDelete = (ImageView) row.findViewById(R.id.menu_item_delete_landmark);
    imgDelete.setTag(getRef(position).getKey());
      imgDelete.setOnClickListener(deleteListener);

    }
}
