package ie.wit.poland.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ie.wit.poland.R;
import ie.wit.poland.main.LandmarkApp;

public class LogIn extends AppCompatActivity {
    public LandmarkApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        app = (LandmarkApp) getApplication();
    }


    public void signUp(View v)
    {
        startActivity(new Intent(this,Register.class));
    }
    }

