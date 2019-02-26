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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyList= findViewById(R.id.emptyList);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Information", Snackbar.LENGTH_LONG)
                        .setAction("More Info...", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }).show();
            }
        });
        this.setupLandmarks();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(landmarkList.isEmpty())
            emptyList.setText(getString(R.string.emptyMessageLbl));
        else
            emptyList.setText("");
    }

    public void add(View v)
    {
        startActivity(new Intent(this,Add.class));
    }

    public void setupLandmarks(){
        landmarkList.add(new Landmark("Sopot", "Beach",2.5,"North",1.99,4,5,"19/11/2013"));
        landmarkList.add(new Landmark("Malbork", "Old Castle",3.5,"North",2.99, 4,5,"19/11/2013"));
        landmarkList.add(new Landmark("Warsaw", "Capital City",4.5,"Centre",1.49, 4,5,"19/11/2013"));
    }
}