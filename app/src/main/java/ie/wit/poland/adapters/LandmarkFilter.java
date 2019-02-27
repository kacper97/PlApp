package ie.wit.poland.adapters;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import ie.wit.poland.models.Landmark;

public class LandmarkFilter extends Filter {
    public List<Landmark> originalLandmarkList;
    public String filterText;
    public LandmarkListAdapter adapter;

    public LandmarkFilter(List<Landmark> originalLandmarkList, String filterText,
                        LandmarkListAdapter adapter) {
        super();
        this.originalLandmarkList = originalLandmarkList;
        this.filterText = filterText;
        this.adapter = adapter;
    }

    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        List<Landmark> newLandmarks;
        String landmarkName;

        if (prefix == null || prefix.length() == 0) {
            newLandmarks = new ArrayList<>();
            if (filterText.equals("all")) {
                results.values = originalLandmarkList;
                results.count = originalLandmarkList.size();
            } else {
                if (filterText.equals("favourites")) {
                    for (Landmark l : originalLandmarkList)
                        if (l.favourite)
                            newLandmarks.add(l);
                }
                results.values = newLandmarks;
                results.count = newLandmarks.size();
            }
        } else {
            String prefixString = prefix.toString().toLowerCase();
            newLandmarks = new ArrayList<>();

            for (Landmark l : originalLandmarkList) {
                landmarkName = l.landmarkName.toLowerCase();
                if (landmarkName.contains(prefixString)) {
                    if (filterText.equals("all")) {
                        newLandmarks.add(l);
                    } else if (l.favourite) {
                        newLandmarks.add(l);
                    }}}
            results.values = newLandmarks;
            results.count = newLandmarks.size();
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence prefix, FilterResults results) {

        adapter.landmarkList = (ArrayList<Landmark>) results.values;

        if (results.count >= 0)
            adapter.notifyDataSetChanged();
        else {
            adapter.notifyDataSetInvalidated();
            adapter.landmarkList = originalLandmarkList;
        }
    }
}
