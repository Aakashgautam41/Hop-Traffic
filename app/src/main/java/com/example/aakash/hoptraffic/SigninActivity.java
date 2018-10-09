package com.example.aakash.hoptraffic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button   enter;
    Button   register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Getting UI elements
        email = findViewById(R.id.signinEmail);
        password = findViewById(R.id.signinPassword);
        enter = findViewById(R.id.enter);
        register = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        //Setting onClick listener on buttons
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(){

        email.setError(null);
        password.setError(null);

        //Grab values
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Pass Validation
        if(TextUtils.isEmpty(userPassword) == true){
            password.setError("Your password field is empty");
            focusView = password;
            cancel = true;
        }
        else if (checkPassword(userPassword) == false) {
            password.setError("Your password is too short");
            focusView = password;
            cancel = true;
        }


        //Email validation
        if (TextUtils.isEmpty(userEmail) == true){
            email.setError("Your email field is empty");
            focusView = email;
            cancel = true;
        }
        else if(checkEmail(userEmail) == false){
            email.setError("Email is invalid");
            focusView = email;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }
        else {
            String myEmail = email.getText().toString();
            String myPassword = password.getText().toString();

            Toast.makeText(this, "Logging you in....", Toast.LENGTH_SHORT).show();
//            Snackbar.make(findViewById(android.R.id.content), "Logging you in ...", Snackbar.LENGTH_LONG).show();

            mAuth.signInWithEmailAndPassword(myEmail, myPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(SigninActivity.this, DepartmentActivity.class);
                                startActivity(intent);
                                Log.i("Tag", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SigninActivity.this, "Authentication Successful",Toast.LENGTH_SHORT).show();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("Tag", "signInWithEmail:failure", task.getException());
                                showErrorbox("Sign In Failed !!!");
                            }
                        }
                    });
        }

    }

    //Validation rules for email
    private boolean checkEmail(String email){
        return  email.contains("@");
    }

    //Validation rules for password
    private boolean checkPassword(String password){
        return password.length()>=6 ;

    }

    //Create errorBox for errors
    private void showErrorbox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
