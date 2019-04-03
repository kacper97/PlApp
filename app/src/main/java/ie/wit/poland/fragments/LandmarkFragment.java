package ie.wit.poland.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ie.wit.poland.adapters.LandmarkListAdapter;
import ie.wit.poland.R;
import ie.wit.poland.activities.Base;
import ie.wit.poland.activities.Edit;
import ie.wit.poland.models.Landmark;
import ie.wit.poland.activities.Favourites;
import ie.wit.poland.adapters.LandmarkFilter;

public class LandmarkFragment  extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        AbsListView.MultiChoiceModeListener {
    public Base activity;
    public static LandmarkListAdapter listAdapter;
    public ListView listView;
    public LandmarkFilter landmarkFilter;
    public boolean favourites = false;

    public LandmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putString("landmarkId", (String) view.getTag());
        Intent goEdit = new Intent(getActivity(), Edit.class); // Creates a new Intent
        /* Add the bundle to the intent here */
        goEdit.putExtras(activityInfo);
        getActivity().startActivity(goEdit); // Launch the Intent
    }


    public static LandmarkFragment newInstance() {
        LandmarkFragment fragment = new LandmarkFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.activity = (Base) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listAdapter = new LandmarkListAdapter(activity, this,activity.app.landmarkList);
        landmarkFilter = new LandmarkFilter(activity.app.landmarkList,"all", listAdapter);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_home, parent, false);



        if (favourites) {
            landmarkFilter.setFilter("favourites"); // Set the filter text field from 'all' to 'favourites'
            landmarkFilter.filter(null); // Filter the data, but don't use any prefix
            listAdapter.notifyDataSetChanged(); // Update the adapter
        }
        setRandomLandmark();

        listView = v.findViewById(R.id.homeList);
        listView.setAdapter (listAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        listView.setEmptyView(v.findViewById(R.id.emptyList));

        checkEmptyList(v);


        return v;

    }


    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View view)
    {
        if (view.getTag() instanceof Landmark)
        {
            onLandmarkDelete ((Landmark) view.getTag());
        }
    }

    private void onLandmarkDelete(final Landmark landmark) {
        String stringName = landmark.landmarkName;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to Delete the \'Landmark\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                activity.app.landmarkList.remove(landmark); // remove from our list
                listAdapter.landmarkList.remove(landmark); // update adapters data
                setRandomLandmark();
                listAdapter.notifyDataSetChanged(); // refresh adapter
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.delete_list_context,menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        {
            switch(menuItem.getItemId())
            {
                case R.id.menu_item_delete_landmark:
                    deleteLandmarks(actionMode);
                    return true;
                default:
                    return false;
            }
        }
    }

    public void deleteLandmarks(ActionMode actionMode) {
        for (int i = listAdapter.getCount() - 1; i >= 0; i--)
        {
            if (listView.isItemChecked(i))
            {
                activity.app.landmarkList.remove(listAdapter.getItem(i));
                if (activity instanceof Favourites)
                    listAdapter.landmarkList.remove(listAdapter.getItem(i));
            }
        }
        setRandomLandmark();
        listAdapter.notifyDataSetChanged();  // refresh adapter

        actionMode.finish();
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

    public void setRandomLandmark() {

        ArrayList<Landmark> landmarkList = new ArrayList<>();

        for(Landmark l : activity.app.landmarkList)
            if (l.favourite)
                landmarkList.add(l);

        if (activity instanceof Favourites)
            if( !landmarkList.isEmpty()) {
                Landmark randomLandmark = landmarkList.get(new Random()
                        .nextInt(landmarkList.size()));

                ((TextView) getActivity().findViewById(R.id.favouriteLandmarkName)).setText(randomLandmark.landmarkName);
                ((TextView) getActivity().findViewById(R.id.favouriteLandmarkDescription)).setText(randomLandmark.landmarkDescription);
                ((TextView) getActivity().findViewById(R.id.favouritePrice)).setText("€ " + randomLandmark.price);
                ((TextView) getActivity().findViewById(R.id.favouriteLandmarkRating)).setText(randomLandmark.ratingLandmark + " *");
            }
            else {
                ((TextView) getActivity().findViewById(R.id.favouriteLandmarkName)).setText("N/A");
                ((TextView) getActivity().findViewById(R.id.favouriteLandmarkDescription)).setText("N/A");
                ((TextView) getActivity().findViewById(R.id.favouritePrice)).setText("N/A");
                ((TextView) getActivity().findViewById(R.id.favouriteLandmarkRating)).setText("N/A");
            }
    }

    public void checkEmptyList(View v)
    {
        TextView recentList = v.findViewById(R.id.emptyList);

        if(activity.app.landmarkList.isEmpty())
            recentList.setText(getString(R.string.emptyMessageLbl));
        else
            recentList.setText("");
    }

    public void setListView(View view)
    {
        listView.setAdapter (listAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        listView.setEmptyView(view.findViewById(R.id.emptyList));
    }
}