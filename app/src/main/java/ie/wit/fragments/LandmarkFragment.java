package ie.wit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import ie.wit.adapters.LandmarkListAdapter;
import ie.wit.poland.activities.Base;

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

    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View view)
    {
    }
}