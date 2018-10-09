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

public class SignUpActivity extends AppCompatActivity {

    Button register;
    EditText email;
    EditText password;
    EditText confPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Getting UI elements
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        confPassword = findViewById(R.id.signupConfPassword);
        register = findViewById(R.id.signupRegister);

        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser(){

        email.setError(null);
        password.setError(null);

        //Grab values
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Pass Validation
        if(TextUtils.isEmpty(userPassword) == true){
            password.setError("Your password field is empty or less than 6 characters");
            focusView = password;
            cancel = true;
        }
        else if (checkPassword(userPassword) == false) {
            confPassword.setError("Your password does not match");
            focusView = confPassword;
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
        } else {
            createUser();
        }

    }

    public  void createUser(){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Sending user to signIn activity after success

                            Intent intent = new Intent(SignUpActivity.this,SigninActivity.class);
                            startActivity(intent);

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("tag", "createUserWithEmail:success");

                            Toast.makeText(SignUpActivity.this,"You are successfully registered", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("tag", "createUserWithEmail:failure", task.getException());
                            showErrorbox("Registration Failed !!!");
                        }

                    }
                });
    }


    //Validation rules for email
    private boolean checkEmail(String email){
        return  email.contains("@");
    }

    //Validation rules for password
    private boolean checkPassword(String password){
        String confirmPassword = confPassword.getText().toString() ;
        return confirmPassword.equals(password) && password.length()>=6 ;

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
