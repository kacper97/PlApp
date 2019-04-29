package ie.wit.poland.adapters;

import android.widget.Filter;

import com.google.firebase.database.Query;

import ie.wit.poland.fragments.LandmarkFragment;


public class LandmarkFilter extends Filter {

    private String                 filterText;
    public LandmarkListAdapter     adapter;
    private Query                query;
    public LandmarkFragment fragment;

    public LandmarkFilter(Query landmarkQuery, String filterText
            , LandmarkListAdapter adapter, LandmarkFragment fragment) {
        super();
        this.query = landmarkQuery;
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
                } else if(filterText.equals("cheapest")){
                    query = fragment.app.FirebaseDB.getCheapest();
                } else if (filterText.equals("alphabetical")){
                    query = fragment.app.FirebaseDB.getAlphabetical();
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

