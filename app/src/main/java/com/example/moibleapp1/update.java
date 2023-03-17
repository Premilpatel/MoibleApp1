package com.example.moibleapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class update extends AppCompatActivity {
    private EditText editTextUpdateEmail,editTextUpdateName;
    private ProgressBar progressBar;

    private DatabaseReference database;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getSupportActionBar().setTitle("Updated!");
        database = FirebaseDatabase.getInstance().getReference();
        findViews();

        Button Buttonupdate = findViewById(R.id.button_updateinfo);
        Buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textName = editTextUpdateName.getText().toString();
                String textEmail = editTextUpdateEmail.getText().toString();
                //check to see if data is valid before registering the user
                if (TextUtils.isEmpty(textName)) {
                    Toast.makeText(update.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    editTextUpdateName.setError("Name is required!");
                    editTextUpdateName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(update.this, "Please enter a Email", Toast.LENGTH_SHORT).show();
                    editTextUpdateEmail.setError("Email is required!");
                    editTextUpdateName.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    updateuser(textEmail, textName);

                }
            }
        });

    }


    private void updateuser(String textEmail, String textName) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
        findViews();


        firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(update.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    firebaseUser.updateProfile(profileUpdate);
                    firebaseUser.updateEmail(textEmail);
                    Toast.makeText(update.this,"update Successful!", Toast.LENGTH_SHORT).show();

                    String currentuidofuser = firebaseUser.getUid();
                    User changeUser = new User(textName, textEmail);
                    database.child("users").child(currentuidofuser).setValue(changeUser);


                    //Open UserProfileActivity after user is created.
                    Toast.makeText(update.this,"Signed Out!",Toast.LENGTH_SHORT).show();
                    Intent mainActivity = new Intent(update.this, UserProfileActivity.class);
                    //Stop the user from going back to register Activity on pressing back button
                    mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainActivity);
                    finish();

                }else{
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }


    private void findViews() {
        editTextUpdateEmail = findViewById(R.id.editText_updateregister_email);
        editTextUpdateName = findViewById(R.id.editText_updateregister_name);
        progressBar = findViewById(R.id.progressbar);
    }
}