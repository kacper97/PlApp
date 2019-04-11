package ie.wit.poland.main;

import ie.wit.poland.models.FirebaseDB;
import ie.wit.poland.models.Landmark;
import java.util.ArrayList;
import java.util.List;
import android.app.Application;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandmarkApp extends Application
{
    public FirebaseUser FirebaseUser ;
    public FirebaseAuth mFirebaseAuth;
    private static final String TAG =LandmarkApp.class.getName();
    public List <Landmark>  landmarkList = new ArrayList<>();
    public FirebaseDB FirebaseDB;
    /* Client used to interact with Google APIs. */
    public GoogleApiClient mGoogleApiClient;
    public GoogleSignInOptions mGoogleSignInOptions;

    public boolean signedIn = false;
    public String googleToken;
    public String googleName;
    public String googleMail;
    public String googlePhotoURL;
    public Bitmap googlePhoto;
    public Location mCurrentLocation;
    private static LandmarkApp mInstance;


    public void onCreate()
    {
        super.onCreate();
        Log.v("landmark", "Landmark App Started");
        FirebaseDB = new FirebaseDB();
        FirebaseDB.open();
        mInstance = this;
    }

    public static synchronized LandmarkApp getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}