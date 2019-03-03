package ie.wit.poland.main;

import ie.wit.poland.models.FirebaseDB;
import ie.wit.poland.models.Landmark;
import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.util.Log;

public class LandmarkApp extends Application
{
    public List <Landmark>  landmarkList = new ArrayList<>();
    public FirebaseDB FirebaseDB;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("landmark", "Landmark App Started");
        FirebaseDB = new FirebaseDB();
        FirebaseDB.open();
    }
}