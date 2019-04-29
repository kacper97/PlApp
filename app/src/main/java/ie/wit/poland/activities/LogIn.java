package ie.wit.poland.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.shashank.sony.fancytoastlib.FancyToast;

import ie.wit.poland.R;
import ie.wit.poland.main.LandmarkApp;
import ie.wit.poland.models.FirebaseListener;
import ie.wit.poland.models.User;


public class LogIn extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        OnClickListener, FirebaseListener {

    public LandmarkApp app = LandmarkApp.getInstance();

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "Landmarks";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        app.mGoogleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API
        app.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, app.mGoogleSignInOptions)
                .addApi(LocationServices.API)
                .build();
        app.mFirebaseAuth = FirebaseAuth.getInstance();
        app.FirebaseDB.attachListener(this);

        setContentView(R.layout.log_in);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(app.mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            app.googleName = acct.getDisplayName();
            app.googleToken = acct.getId();
            app.signedIn = true;
            app.googleMail = acct.getEmail();
            if (acct.getPhotoUrl() == null)
                ; //New Account may not have Google+ photo
            else app.googlePhotoURL = acct.getPhotoUrl().toString();

            firebaseAuthWithGoogle(acct);
            // Show a message to the user that we are signing in.
            FancyToast.makeText(this, "Signing in " + app.googleName + " with " + app.googleMail, Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
            //startHomeScreen();
        } else
            FancyToast.makeText(this, "Please Sign in ", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
    }
    // [END handleSignInResult]


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            signIn();
        } else if (v.getId() == R.id.disconnect_button) {
            revokeAccess();
        }
    }

    private void startHomeScreen() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void startLoginScreen() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(app.mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(app.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        startLoginScreen();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        FancyToast.makeText(this, "Error Signing in to Google " + connectionResult, Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
        Log.v(TAG, "ConnectionResult : " + connectionResult);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.v(TAG, "firebaseAuthWithGoogle:" + acct.getEmail());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        app.mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        validateFirebaseUser();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.v(TAG, "signInWithCredential", task.getException());
                            FancyToast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT, FancyToast.CONFUSING, true).show();
                        }
                    }
                });
    }

    private void validateFirebaseUser() {
        Log.v(TAG, "Calling validateFirebaseUser() ");
        if (app.FirebaseUser == null)
            app.FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        app.FirebaseDB.checkUser(app.FirebaseUser.getUid(),
                app.FirebaseUser.getDisplayName(),
                app.FirebaseUser.getEmail());
    }

    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            Log.v(TAG, "User found : ");
        } else {
            Log.v(TAG, "User not found, Creating User on Firebase");
            User newUser = new User(app.FirebaseUser.getUid(),
                    app.FirebaseUser.getDisplayName(),
                    app.FirebaseUser.getEmail(), null);
            app.FirebaseDB.mFirebaseDatabase.child("users")
                    .child(app.FirebaseUser.getUid())
                    .setValue(newUser);
        }
        app.FirebaseDB.mFBUserId = app.FirebaseUser.getUid();

        startHomeScreen();
    }

    @Override
    public void onFailure() {
        Log.v(TAG, "Unable to Validate Existing Firebase User: ");
        FancyToast.makeText(this, "Unable to Validate Existing Firebase User:", Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
    }

    public void signUp(View v) {
        startActivity(new Intent(this, Register.class));
    }
}
