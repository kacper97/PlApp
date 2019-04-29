package ie.wit.poland.models;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseDB {

    private static final String TAG = "landmark";
    public DatabaseReference mFirebaseDatabase;
    public static String mFBUserId;
    public FirebaseListener mFBDBListener;

    public void open() {
        //Set up local caching
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Bind to remote Firebase Database
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        Log.v(TAG, "Database Connected :" + mFirebaseDatabase);
    }

    public void attachListener(FirebaseListener listener) {
        mFBDBListener = listener;
    }

    //Check to see if the Firebase User exists in the Database
//if not, create a new User
    public void checkUser(final String userid,final String username,final String email) {
        Log.v(TAG, "checkUser ID == " + userid);
        mFirebaseDatabase.child("users").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mFBDBListener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mFBDBListener.onFailure();
                    }
                }
        );
    }

    public void addLandmark(final Landmark l)
    {
            mFirebaseDatabase.child("users").child(mFBUserId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, error out
                            Log.v(TAG, "User " + mFBUserId + " is unexpectedly null");
                            //Toast.makeText(this,
                            //		"Error: could not fetch user.",
                            //		Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new landmark
                            writeNewLandmark(l);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void writeNewLandmark(Landmark l) {
        // Create new landmark at /user-landmarks/$userid/$landmarkid and at
        // /landmarks/$landmarkid simultaneously
        String key = mFirebaseDatabase.child("landmarks").push().getKey();
        Map<String, Object> landmarkValues = l.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        //All landmark
        childUpdates.put("/landmarks/" + key, landmarkValues);
        //All landmarks per user
        childUpdates.put("/user-landmarks/" + mFBUserId + "/" + key, landmarkValues);

        mFirebaseDatabase.updateChildren(childUpdates);
    }

    public Query getAllLandmarks()
    {
        Query query = mFirebaseDatabase.child("user-landmarks").child(mFBUserId)
                .orderByChild("latitude");

        return query;
    }

    public Query getFavouriteLandmarks()
    {
        Query query = mFirebaseDatabase.child("user-landmarks").child(mFBUserId)
                .orderByChild("favourite").equalTo(true);

        return query;
    }

    public Query getCheapest() {

        Query query = mFirebaseDatabase.child("user-landmarks").child(mFBUserId)
                .orderByChild("price");
        return query;
    }

    public void getALandmark(final String landmarkKey)
    {
        mFirebaseDatabase.child("user-landmarks").child(mFBUserId).child(landmarkKey).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v(TAG,"The read Succeeded: " + dataSnapshot.toString());
                        mFBDBListener.onSuccess(dataSnapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.v(TAG,"The read failed: " + firebaseError.getMessage());
                        mFBDBListener.onFailure();
                    }
                });
    }

    public void updateALandmark(final String landmarkKey,final Landmark updatedLandmark)
    {
        mFirebaseDatabase.child("user-landmarks").child(mFBUserId).child(landmarkKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v(TAG,"The update Succeeded: " + dataSnapshot.toString());
                        dataSnapshot.getRef().setValue(updatedLandmark);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.v(TAG,"The update failed: " + firebaseError.getMessage());
                        mFBDBListener.onFailure();
                    }
                });
    }

    public void toggleFavourite(final String landmarkKey,final boolean isFavourite)
    {
        mFirebaseDatabase.child("user-landmarks").child(mFBUserId).child(landmarkKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        snapshot.getRef().child("favourite").setValue(isFavourite);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.v(TAG,"The toggle failed: " + firebaseError.getMessage());
                    }
                });
    }

    public void deleteALandmark(final String landmarkKey)
    {
        mFirebaseDatabase.child("user-landmarks").child(mFBUserId).child(landmarkKey).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.v(TAG,"The delete failed: " + firebaseError.getMessage());
                    }
                });
    }

    public Query nameFilter(String s)
    {
        Query query = mFirebaseDatabase.child("user-landmarks").child(mFBUserId)
                .orderByChild("landmarkName").startAt(s).endAt(s+"\uf8ff");

        return query;
    }

    public void getAllLandmarksSnapshot()
    {
        ValueEventListener vel = mFirebaseDatabase.child("user-landmarks")
                .child(mFBUserId)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mFBDBListener.onSuccess(dataSnapshot);
                            }
                            @Override
                            public void onCancelled(DatabaseError firebaseError) {
                                mFBDBListener.onFailure();
                            }
                        });
    }


    public Query getAlphabetical() {
        Query query = mFirebaseDatabase.child("user-landmarks").child(mFBUserId)
                .orderByChild("landmarkName");
        return query;
    }

    public Query getRatingHighest() {
        Query query = mFirebaseDatabase.child("user-landmarks").child(mFBUserId)
                .orderByChild("ratingFacility");
        return query;
    }
}