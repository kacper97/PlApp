package ie.wit.poland.adapters;
import android.widget.Filter;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import ie.wit.poland.fragments.LandmarkFragment;
import ie.wit.poland.models.FirebaseDB;
import ie.wit.poland.models.Landmark;

public class LandmarkFilter extends Filter {
    private String                 filterText;
    public LandmarkListAdapter     adapter;
    private Query                query;
    public LandmarkFragment        fragment;

    public LandmarkFilter(Query landmarkList, String filterText
            , LandmarkListAdapter adapter, LandmarkFragment fragment) {
        super();
        this.query = landmarkList;
        this.filterText = filterText;
        this.adapter = adapter;
        this.fragment = fragment;
    }


    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        if(prefix != null)
            if (prefix.length() > 0)
                query = fragment.app.FirebaseDB.nameFilter(prefix.toString());

            else {
                if (filterText.equals("all")) {
                    query = fragment.app.FirebaseDB.getAllLandmarks();
                } else if (filterText.equals("favourites")) {
                    query = fragment.app.FirebaseDB.getFavouriteLandmarks();
                }
            }
        results.values = query;

        return results;
    }

    @Override
    protected void publishResults(CharSequence prefix, FilterResults results) {

        fragment.updateUI((Query) results.values);
    }
}

