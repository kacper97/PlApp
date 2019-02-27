package ie.wit.poland.activities;

import android.os.Bundle;
import android.widget.TextView;

import ie.wit.poland.R;
import ie.wit.poland.fragments.LandmarkFragment;

public class Favourites extends Base {

    private TextView emptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites);

        emptyList = findViewById(R.id.emptyList);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(app.landmarkList.isEmpty())
            emptyList.setText(getString(R.string.emptyMessageLbl));
        else
            emptyList.setText("");

        landmarkFragment = LandmarkFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, landmarkFragment)
                .commit(); // add it to the current activity
    }

}
