package ie.wit.poland.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;

public class LandmarkListAdapter extends ArrayAdapter<Landmark> {
    private Context context;
    private View.OnClickListener deleteListener;
    public List<Landmark> landmarkList;

    public LandmarkListAdapter(Context context, View.OnClickListener deleteListener, List<Landmark> landmarkList) {
        super(context, R.layout.landmarkrow, landmarkList);
        this.context = context;
        this.deleteListener = deleteListener;
        this.landmarkList = landmarkList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LandmarkItem item = new LandmarkItem(context, parent,
                deleteListener, landmarkList.get(position));
        return item.view;
    }

    @Override
    public int getCount() {
        return landmarkList.size();
    }
}
