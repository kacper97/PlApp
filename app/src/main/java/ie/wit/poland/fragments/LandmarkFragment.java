package ie.wit.poland.activities.

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import com.google.firebase.database.Query;

import java.util.List;

import ie.wit.poland.R;
import ie.wit.poland.adapters.LandmarkFilter;
import ie.wit.poland.adapters.LandmarkListAdapter;
import ie.wit.poland.fragments.EditFragment;
import ie.wit.poland.main.LandmarkApp;
import ie.wit.poland.models.FirebaseListener;
import ie.wit.poland.models.Landmark;

import static ie.wit.poland.activities.Home.app;

public class LandmarkFragment  extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        AbsListView.MultiChoiceModeListener
{
    public Home activity;
    public static LandmarkListAdapter listAdapter;
    public ListView listView;
    public LandmarkFilter coffeeFilter;
    public boolean favourites = false;
    public View v;
    public LandmarkApp app;

    public LandmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putString("landmarkKey", (String) view.getTag());

        Fragment fragment = EditFragment.newInstance(activityInfo);
        getActivity().setTitle(R.string.editALandmark);

        getActivity().getSupportFragmentManager().beginTransaction()
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
        this.activity = (Home) context;
      //  aLandmark = dataSnapshot.getValue(Landmark.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
     //   aLandmark = dataSnapshot.getValue(Landmark.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app.FirebaseDB.getAllLandmarks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, parent, false);
        listView = v.findViewById(R.id.homeList);
        updateView();
        return v;
    }

    public void setListView(View view)
    {
        listView.setAdapter (listAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        listView.setEmptyView(view.findViewById(R.id.emptyList));
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        FirebaseListener listener;
        app.FirebaseDB.attachListener(listener);
        updateView();
        app.FirebaseDB.getALandmark("landmarkKey");
    }

    public void updateView() {
        listAdapter = new LandmarkListAdapter( );
       // landmarkFilter = new LandmarkFilter(app.landmarkList,"all",listAdapter);

        if(favourites){
            Query query = app.FirebaseDB.getFavouriteLandmarks();
            landmarkFilter.setFilter("favourites"); // Set the filter text field from 'all' to 'favourites'
            landmarkFilter.filter(null); // Filter the data, but don't use any prefix
            //  listAdapter.notifyDataSetChanged(); // Update the adapter
        }
        setListView(v);

        if(!favourites)

    {
        getActivity().setTitle(R.string.recentlyViewedLbl);
    }else

     {
        getActivity().setTitle(R.string.favouriteLandmarkLbl);
     }
        listAdapter.notifyDataSetChanged(); // Update the adapter
        }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Landmark) {
            onLandmarkDelete ((Landmark) view.getTag());
        }
    }

    public void onLandmarkDelete(final Landmark landmark)
    {
        String stringName = landmark.landmarkName;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to Delete the \'Landmark\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                app.FirebaseDB.deleteALandmark("landmarkKey");
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

    /* ************ MultiChoiceModeListener methods (begin) *********** */
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu)
    {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.delete_list_context, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.menu_item_delete_landmark:
                deleteLandmarks(actionMode);
                return true;
            default:
                return false;
        }
    }

    public void deleteLandmarks(ActionMode actionMode)
    {
        for (int i = listAdapter.getCount() -1 ; i >= 0; i--)
        {
            if (listView.isItemChecked(i))
            {
                app.FirebaseDB.deleteALandmark("landmarkKey");

            }
        }
        app.FirebaseDB.getAllLandmarks();
        listAdapter.notifyDataSetChanged(); // refresh adapter
        actionMode.finish();
    }


    @Override
    public void onDestroyActionMode(ActionMode actionMode)
    {}

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
    }

    public void updateUI(Query values) {
    }
    /* ************ MultiChoiceModeListener methods (end) *********** */


}
