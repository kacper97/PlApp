package ie.wit.poland.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ie.wit.poland.R;
import ie.wit.poland.models.Landmark;


public class Home extends Base {

    TextView emptyList;
    ListView landmarkListView;
    ArrayAdapter<Landmark> landmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyList= findViewById(R.id.emptyList);
        landmarkListView= findViewById(R.id.recentlyAddedList);
        landmarkListView.setEmptyView(emptyList);
        landmarkAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,landmarkList);
        landmarkListView.setAdapter(landmarkAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("landmark", "Home : " + landmarkList);

        if(landmarkList.isEmpty())
            landmarkAdapter.notifyDataSetChanged();
    }

    public void add(View v)
    {
        startActivity(new Intent(this,Add.class));
    }
}