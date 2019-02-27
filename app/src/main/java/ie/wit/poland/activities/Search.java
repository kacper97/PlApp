package ie.wit.poland.activities;


import android.os.Bundle;

import ie.wit.poland.R;
import ie.wit.poland.fragments.LandmarkFragment;

public class Search extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        landmarkFragment = LandmarkFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, landmarkFragment)
                .commit(); // add it to the current activity
    }

}
