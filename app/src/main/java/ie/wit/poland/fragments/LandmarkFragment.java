package ie.wit.poland.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ie.wit.poland.adapters.LandmarkListAdapter;
import ie.wit.poland.R;
import ie.wit.poland.activities.Base;
import ie.wit.poland.models.Landmark;
import ie.wit.poland.activities.Favourites;
import ie.wit.poland.adapters.LandmarkFilter;

public class LandmarkFragment  extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        AbsListView.MultiChoiceModeListener
{
    public Base activity;
    public static LandmarkListAdapter listAdapter;
    public ListView listView;
    public LandmarkFilter landmarkFilter;
    public boolean favourites = false;
    public View v;

    public LandmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putString("landmarkId", (String) view.getTag());
        Fragment fragment = EditFragment.newInstance(activityInfo);
        getActivity().setTitle(R.string.editALandmark);
        getFragmentManager().beginTransaction()
                .replace(R.id.homeFrame, fragment)
                .addToBackStack(null)
                .commit();
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
        LandmarkApi.attachListener(this);
        LandmarkApi.attachDialog(activity.loader);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        LandmarkApi.detachListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LandmarkApi.get("/coffees/" + app.googleToken);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_home, parent, false);
        listView = v.findViewById(R.id.homeList);
        updateView();
        return v;
    }

    private void updateView() {
        listAdapter = new LandmarkListAdapter(activity, this, activity.app.landmarkList);
        landmarkFilter = new LandmarkFilter(activity.app.landmarkList,"all",listAdapter);

        if (favourites) {
            getActivity().setTitle("Favourite Landmark's");
            landmarkFilter.setFilter("favourites"); // Set the filter text field from 'all' to 'favourites'
            landmarkFilter.filter(null); // Filter the data, but don't use any prefix
          //  listAdapter.notifyDataSetChanged(); // Update the adapter
        }
        setListView(v);
        setSwipeRefresh(v);
        //setRandomLandmark();
        if(!favourites)
            getActivity().setTitle(R.string.recentlyViewedLbl);
        else
            getActivity().setTitle(R.string.favouriteLandmarkLbl);


        listAdapter.notifyDataSetChanged(); // Update the adapter
    }



    @Override
    public void onStart()
    {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        LandmarkApi.attachListener(this);
        updateView();
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
                LandmarkApi.delete("/coffees/" + app.googleToken +
                        "/" + landmark._id,app.googleToken);
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
                LandmarkApi.delete("/coffees/" + app.googleToken
                        + "/" + listAdapter.getItem(i)._id,app.googleToken);
            }
        }
        LandmarkApi.get("/coffees/" + app.googleToken);
        listAdapter.notifyDataSetChanged();  // refresh adapter

        actionMode.finish();
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }

    public void setSwipeRefresh(View v)
    {
        SwipeRefreshLayout swipeRefresh = v.findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Landmark.get("/coffees/" + app.googleToken);
            }
        });
    }

    public void checkSwipeRefresh(View v)
    {
        SwipeRefreshLayout swipeRefresh = v.findViewById(R.id.swiperefresh);
        if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
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