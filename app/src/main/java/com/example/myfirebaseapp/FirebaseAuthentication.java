package com.example.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentication extends AppCompatActivity {

    private TextInputLayout mEmailLayout, mPasswordLayout;
    private Button mBtnSignin, mBtnRegisterUser, mBtnSignoutUser;
    private TextView mOutputText;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        initViews();

        mAuth = FirebaseAuth.getInstance();

        mBtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singInUser(v);
            }
        });
        mBtnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(v);
            }
        });
        mBtnSignoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser(v);
            }
        });
        hideProgressBar();
    }
    private void initViews() {
        mEmailLayout = findViewById(R.id.et_email);
        mPasswordLayout = findViewById(R.id.et_password);
        mBtnSignin = findViewById(R.id.btn_singin);
        mBtnRegisterUser = findViewById(R.id.btn_registeruser);
        mOutputText = findViewById(R.id.tv_output);
        mProgressBar = findViewById(R.id.progressbar);
        mBtnSignoutUser = findViewById(R.id.btn_signout);
    }

    private void singInUser(View view) {

        if (!validateEmailAddress() | !validatePassword()) {
            // Email or Password not valid,
            return;
        }
        //Email and Password valid, sign in user here

        String email = mEmailLayout.getEditText().getText().toString().trim();
        String password = mPasswordLayout.getEditText().getText().toString().trim();
        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)//this is the basic code to login the firebase
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            hideProgressBar();
                            Toast.makeText(FirebaseAuthentication.this, "User logged in", Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            Toast.makeText(FirebaseAuthentication.this, "Error occurred", Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        }

                    }
                });
    }

    private void createUser(View view) {

        if (!validateEmailAddress() | !validatePassword()) {
            // Email or Password not valid,
            return;
        }
        //Email and Password valid, create user here
        //Email and Password valid, create user here
        String email = mEmailLayout.getEditText().getText().toString().trim();
        String password = mPasswordLayout.getEditText().getText().toString().trim();
        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FirebaseAuthentication.this, "User created", Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                            updateUI();
                        } else {
                            Toast.makeText(FirebaseAuthentication.this, "Error occurred", Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        }
                    }
                });
    }
    private void signOutUser(View view) {
        mAuth.signOut();
        updateUI();
    }
    private void updateUI() {

        FirebaseUser user = mAuth.getCurrentUser();//will get the current user which is logged in
        //will return null if there is no user logged

        if (user == null) {
            mOutputText.setText("User not logged in");
            return;
        } else {
            mOutputText.setText(user.getEmail());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }
    private boolean validateEmailAddress() {

        String email = mEmailLayout.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            mEmailLayout.setError("Email is required. Can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailLayout.setError("Invalid Email. Enter valid email address.");
            return false;
        } else {
            mEmailLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String password = mPasswordLayout.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            mPasswordLayout.setError("Password is required. Can't be empty.");
            return false;
        } else if (password.length() < 6) {
            mPasswordLayout.setError("Password short. Minimum 6 characters required.");
            return true;
        } else {
            mPasswordLayout.setError(null);
            return true;
        }
    }
    private void showProgressBar() {
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
