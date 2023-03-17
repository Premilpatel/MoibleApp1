package com.example.moibleapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterEmail, editTextRegisterPwd, editTextRegisterName;
    private ProgressBar progressBar;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Sign Up!");
        database = FirebaseDatabase.getInstance().getReference();
        findViews();

        showHidePwd();

        Button ButtonRegister = findViewById(R.id.button_register);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPassword = editTextRegisterPwd.getText().toString();
                String textName = editTextRegisterName.getText().toString();
                //check to see if data is valid before registering the user
                if(TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterActivity.this,"Please enter your name", Toast.LENGTH_SHORT).show();
                    editTextRegisterName.setError("Name is required!");
                    editTextRegisterName.requestFocus();
                }else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this,"Please enter a Email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required!");
                    editTextRegisterEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this,"Please enter a Password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required!");
                    editTextRegisterPwd.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this,"Please re-enter your email address", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid email is required!");
                    editTextRegisterEmail.requestFocus();
                }else if(textPassword.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password length is too short!");
                    editTextRegisterPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textEmail, textName, textPassword);
                }
            }
        });

    }

    private void registerUser(String textEmail, String textName, String textPassword) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //check to see if user creation was successful
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if(firebaseUser != null){
                        //update the display name of the user
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
                        firebaseUser.updateProfile(profileUpdate);
                        Toast.makeText(RegisterActivity.this,"Registration Successful!", Toast.LENGTH_SHORT).show();
                        writeNewUser(textEmail, textName);

                        //Open UserProfileActivity after user is created
                        Intent userProfileActivity = new Intent(RegisterActivity.this, UserProfileActivity.class);
                        //Stop the user from going back to register Activity on pressing back button
                        userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(userProfileActivity);
                        finish();
                    }
                }else{//Handle Expections
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }


    private void writeNewUser(String textEmail, String textName) {
        User user = new User(textName, textEmail);
        database.child("users").child(firebaseUser.getUid()).setValue(user);
    }


    private void showHidePwd() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.outline_visibility_black_18);

        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextRegisterPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.outline_visibility_black_18);
                }else{
                    editTextRegisterPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.outline_visibility_off_black_18);
                }
            }
        });
    }

    private void findViews() {
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterPwd = findViewById(R.id.editText_register_pwd);
        editTextRegisterName = findViewById(R.id.editText_register_name);
        progressBar = findViewById(R.id.progressbar);

    }

}