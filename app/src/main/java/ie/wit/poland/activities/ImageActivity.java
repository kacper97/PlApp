package ie.wit.poland.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import ie.wit.poland.R;

public class ImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage, mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mButtonChooseImage = findViewById(R.id.chooseImage);
        mButtonUpload = findViewById(R.id.upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);


        mButtonChooseImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*"); // only see images
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(mImageView);
            //mImageView.setImageURI(mImageUri);
            FancyToast.makeText(getApplicationContext(),"Image Added to View",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
        }
    }
}
