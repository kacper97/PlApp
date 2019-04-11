package ie.wit.poland.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import com.google.firebase.database.Query;

import java.util.Objects;

import ie.wit.poland.R;
import ie.wit.poland.adapters.LandmarkFilter;
import ie.wit.poland.adapters.LandmarkListAdapter;
import ie.wit.poland.main.LandmarkApp;


public class LandmarkFragment  extends Fragment implements AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener
{
    protected static LandmarkListAdapter listAdapter;
    protected         ListView 			listView;
    protected LandmarkFilter landmarkFilter;
    public            boolean             favourites = false;
    protected         TextView            titleBar;
    protected         SwipeRefreshLayout  mSwipeRefreshLayout;
    public            Query                 query;
    public            View.OnClickListener  deleteListener;

    public LandmarkApp app = LandmarkApp.getInstance();

    public LandmarkFragment() {
        // Required empty public constructor
    }

    public static LandmarkFragment newInstance() {
        LandmarkFragment fragment = new LandmarkFragment();
        return fragment;
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        v = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) v.findViewById(R.id.homeList);

        mSwipeRefreshLayout =   (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        setSwipeRefreshLayout();

        deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLandmarkDelete (view.getTag().toString());
            }
        };

        return v;
    }

    protected void setSwipeRefreshLayout()
    {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = app.FirebaseDB.getAllLandmarks();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        query = app.FirebaseDB.getAllLandmarks();

        if(favourites)
            query = app.FirebaseDB.getFavouriteLandmarks();

        updateUI(query);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void updateUI(Query query) {

     //   titleBar = (TextView)getActivity().findViewById(R.id.recentlyAdded);
     //   titleBar.setText(R.string.recentlyViewedLbl);

        listAdapter = new LandmarkListAdapter(getActivity(), deleteListener, query);
        setListView(listView);

        if (favourites) {
            titleBar.setText(R.string.favouriteLandmarkLbl);
            ((TextView)getActivity().findViewById(R.id.emptyList)).setText(R.string.emptyMessageLbl);
        }

        if(app.landmarkList.isEmpty())
            ((TextView)getActivity().findViewById(R.id.emptyList)).setText(R.string.emptyMessageLbl);
        else
            ((TextView)getActivity().findViewById(R.id.emptyList)).setText("");

        listAdapter.notifyDataSetChanged(); // Update the adapter
    }

    public void setListView(ListView listview) {

        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listview.setMultiChoiceModeListener(this);
        listview.setAdapter (listAdapter);
        listview.setOnItemClickListener(this);
        listview.setEmptyView(Objects.requireNonNull(getActivity()).findViewById(R.id.emptyList));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public void onLandmarkDelete(final String landmarkKey)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to Delete this Landmark ?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                app.FirebaseDB.deleteALandmark(landmarkKey);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle activityInfo = new Bundle();
        activityInfo.putString("landmarkKey", (String) view.getTag());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = EditFragment.newInstance(activityInfo);
        ft.replace(R.id.homeFrame, fragment);
        ft.addToBackStack(null);
        ft.commit();
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
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu)
    {
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
        for (int i = listAdapter.getCount() - 1; i >= 0; i--) {
            if (listView.isItemChecked(i))
                app.FirebaseDB.deleteALandmark(listView.getChildAt(i).getTag().toString());
        }
        actionMode.finish();

        if (favourites) {
            //Update the filters data
            query = app.FirebaseDB.getFavouriteLandmarks();
            landmarkFilter = new LandmarkFilter(query,"favourites",listAdapter,this);
            landmarkFilter.filter(null);
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode)
    {}

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked)
    {}
    /* ************ MultiChoiceModeListener methods (end) *********** */
}

