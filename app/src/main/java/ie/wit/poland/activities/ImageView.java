package ie.wit.poland.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import ie.wit.poland.R;
import ie.wit.poland.models.Image;

public class ImageView extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private ProgressBar mProgressBar;

    private ValueEventListener mDBListener;

    private List<Image> mImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true); //quicker
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.progress_bar);

        mImages = new ArrayList<>();

        mAdapter = new ImageAdapter(ImageView.this, mImages);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImageView.this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");

        mStorage = FirebaseStorage.getInstance();


        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mImages.clear(); // to avoid duplicates

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Image image = postSnapshot.getValue(Image.class); // get key

                    image.setKey(postSnapshot.getKey()); // unique key to delete entry

                    mImages.add(image);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                FancyToast.makeText(ImageView.this, databaseError.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                ;
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        FancyToast.makeText(this, "Image clicked, at postiion " + position, Toast.LENGTH_LONG, FancyToast.INFO, true).show();
    }

    @Override
    public void OnDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onDeleteClick(int position) {
        Image selectedItem = mImages.get(position);
        final String selectedKey = selectedItem.getKey();


        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue(); // to make sure it wont have a database entry if deleted from storage
                FancyToast.makeText(ImageView.this, "Image deleted successfully", Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
            }
        });
    }


}

