package ie.wit.poland.models;

import com.google.firebase.database.DataSnapshot;

public interface FirebaseListener {
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure();
}
