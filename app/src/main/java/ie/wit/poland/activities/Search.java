package ie.wit.poland.activities;


import android.os.Bundle;

import ie.wit.poland.R;
import ie.wit.poland.fragments.LandmarkFragment;
import ie.wit.poland.fragments.SearchFragment;

public class Search extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        landmarkFragment = SearchFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, landmarkFragment)
                .commit(); // add it to the current activity
    }

}
