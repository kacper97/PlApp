package ie.wit.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import ie.wit.adapters.LandmarkListAdapter;
import ie.wit.poland.activities.Base;
import ie.wit.poland.activities.Edit;
import ie.wit.poland.models.Landmark;

public class LandmarkFragment  extends ListFragment implements View.OnClickListener
{
    public Base activity;
    public static LandmarkListAdapter listAdapter;
    public ListView listView;

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
        this.activity = (Base) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listAdapter = new LandmarkListAdapter(activity, this, Base.landmarkList);
        setListAdapter (listAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putString("landmarkId", (String) v.getTag());

        Intent goEdit = new Intent(getActivity(), Edit.class); // Creates a new Intent
        /* Add the bundle to the intent here */
        getActivity().startActivity(goEdit); // Launch the Intent
        goEdit.putExtras(activityInfo);
        getActivity().startActivity(goEdit);

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
                Base.landmarkList.remove(landmark); // remove from our list
                listAdapter.landmarkList.remove(landmark); // update adapters data
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
}