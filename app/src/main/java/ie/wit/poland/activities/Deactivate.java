package ie.wit.poland.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import ie.wit.poland.R;

public class Deactivate extends AppCompatActivity{

    TextView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate);
        information = (TextView)findViewById(R.id.info);
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        information.setText(auth.getEmail());
    }

    public void deleteAcc (View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        FancyToast.makeText(getApplicationContext(),"ACCOUNT DELETED \n   HOPE TO SEE YOU SOON",Toast.LENGTH_LONG, FancyToast.INFO,true).show();
                        finish();
                        Intent i = new Intent(Deactivate.this,LogIn.class);
                        startActivity(i);
                    }
                    else
                    {
                        FancyToast.makeText(getApplicationContext(),"Account Could not be deactivated",Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                }
            });
        }
    }

}