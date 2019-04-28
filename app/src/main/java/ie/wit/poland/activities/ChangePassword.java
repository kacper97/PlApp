package ie.wit.poland.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import ie.wit.poland.R;

public class ChangePassword extends AppCompatActivity {
    TextView email;
    EditText password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        password = (EditText) findViewById(R.id.settings_password);
        email = (TextView)findViewById(R.id.settings_email);
        auth = FirebaseAuth.getInstance();
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    public void change(View V){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.updatePassword(password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(getApplicationContext(), "Your password has been changed",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                                auth.signOut();
                                finish();
                                Intent l = new Intent(ChangePassword.this, LogIn.class);
                                startActivity(l);
                            }

                            else{
                                FancyToast.makeText(getApplicationContext(),"Password inputed incorrectly",Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                            }
                        }
                    });
        }
    }
}
