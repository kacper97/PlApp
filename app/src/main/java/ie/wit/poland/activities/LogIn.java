package ie.wit.poland.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ie.wit.poland.R;
import ie.wit.poland.main.LandmarkApp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LogIn extends AppCompatActivity {
    public LandmarkApp app;
    EditText loginemail, loginpassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        app = (LandmarkApp) getApplication();

        loginemail = (EditText) findViewById(R.id.loginemail);
        loginpassword = (EditText) findViewById(R.id.loginpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
    }


    public void signUp(View v) {
        startActivity(new Intent(this, Register.class));
    }


    public void login(View v) {
        loginUser();
    }

    private void loginUser() {

        String email = loginemail.getText().toString();
        String password = loginpassword.getText().toString();

        if (email.isEmpty()) {
            loginemail.setError(" Please enter an email ");
            loginemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginemail.setError(" Please enter a valid email ");
            loginemail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            loginpassword.setError(" Password is required ");
            loginpassword.requestFocus();
            return;
        }
        //progress bar
        progressBar.setVisibility(VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(GONE);
                if(task.isSuccessful()){
                   Intent intent= (new Intent (LogIn.this,Home.class));
                   //clears activity from stack so when back is pressed the user is not log out
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}

