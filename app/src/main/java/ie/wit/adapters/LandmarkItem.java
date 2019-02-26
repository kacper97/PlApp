package ie.wit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;

public class LandmarkItem {
    View view;

    public LandmarkItem(Context context, ViewGroup parent,
                        View.OnClickListener deleteListener, Landmark landmark)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.landmarkrow, parent, false);
        view.setTag(landmark.landmarkId);

        updateControls(landmark);

        ImageView imgDelete = (ImageView) view.findViewById(R.id.rowDeleteImg);
        imgDelete.setTag(landmark);
        imgDelete.setOnClickListener(deleteListener);
    }

    private void updateControls(Landmark landmark) {
        ((TextView) view.findViewById(R.id.rowLandmarkName)).setText(landmark.landmarkName);
        ((TextView) view.findViewById(R.id.rowLandmarkDescription)).setText(landmark.landmarkDescription);
        ((TextView) view.findViewById(R.id.rowRating)).setText(landmark.ratingLandmark + " *");
        ((TextView) view.findViewById(R.id.rowPrice)).setText("€" + new DecimalFormat("0.00").format(landmark.price));
    }
}