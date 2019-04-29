package ie.wit.poland.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import ie.wit.poland.R;
import ie.wit.poland.models.Image;

public class ImageView extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private ProgressBar mProgressBar;

    private List<Image> mImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        mRecyclerView  = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true); //quicker
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.progress_bar);

        mImages = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("images");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Image image = postSnapshot.getValue(Image.class);
                    mImages.add(image);
                }


                // Adapter
                mAdapter = new ImageAdapter(ImageView.this, mImages);

                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(ImageView.this);

                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                FancyToast.makeText(ImageView.this,databaseError.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,true).show();;
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        FancyToast.makeText(this,"Image clicked, at postiion " + position, Toast.LENGTH_LONG ,FancyToast.INFO,true).show();
    }

    @Override
    public void onDeleteClick(int position) {

    }
}
