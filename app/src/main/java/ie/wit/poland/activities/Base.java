package ie.wit.poland.activities;

import ie.wit.poland.R;
import ie.wit.poland.fragments.LandmarkFragment;
import ie.wit.poland.main.LandmarkApp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class Base extends AppCompatActivity {
    public LandmarkApp app;
    public Bundle activityInfo; // Used for persistence (of sorts)
    public LandmarkFragment landmarkFragment; // How we'll 'share' our List of Coffees between Activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (LandmarkApp) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void menuHome(MenuItem m) {
        startActivity(new Intent(this, Home.class));
    }

    public void menuInfo(MenuItem m)
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.appAbout))
                .setMessage(getString(R.string.appDesc)
                        + "\n\n"
                        + getString(R.string.appMoreInfo))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we could put some code here too
                    }
                })
                .show();
    }

    public void menuChangePass(MenuItem m ){
        startActivity(new Intent(this,ChangePassword.class));
    }

    public void menuHelp(MenuItem m)
    {
        startActivity(new Intent(this,Help.class));
    }

    public void menuDeactivate(MenuItem m)
    {
        startActivity(new Intent(this,Deactivate.class));
    }


}
