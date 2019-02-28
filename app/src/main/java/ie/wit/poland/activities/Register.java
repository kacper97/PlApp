package ie.wit.poland.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ie.wit.poland.R;

public class Register extends AppCompatActivity {

    EditText registeremail, registerpassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        registeremail = (EditText) findViewById(R.id.registeremail);
        registerpassword = (EditText) findViewById(R.id.registerpassword);

        mAuth = FirebaseAuth.getInstance();
    }

    public void goToLogin(View v) {
        startActivity(new Intent(this, LogIn.class));
    }


    public void register(View v) {
        registerUser();
    }

    private void registerUser() {
        String email = registeremail.getText().toString();
        String password = registerpassword.getText().toString();

        if (email.isEmpty()) {
            registeremail.setError("Please put in email");
            registeremail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registeremail.setError("Please enter a valid email");
            registeremail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            registerpassword.setError("Password is required");
            registerpassword.requestFocus();
            return;
        }
        if (password.length()<6){
            registerpassword.setError("Password must contain more than 6 characters");
            registerpassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            //Annonymous class to see if reg is completed successfully
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"UserRegisteredSuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}





