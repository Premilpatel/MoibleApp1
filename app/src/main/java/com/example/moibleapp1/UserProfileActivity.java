package com.example.moibleapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewName, textViewEmail, textViewRegistration;
    private FirebaseAuth auth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Home");
        auth = FirebaseAuth.getInstance();
        findViews();
        signOut();
        updateInfo();

        //show profile details if User is not null
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null){
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);

        }else{
            Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }



    private void showUserProfile(FirebaseUser firebaseUser) {
        //To obtain metadata of the user object
        FirebaseUserMetadata metadata = firebaseUser.getMetadata();

        //get the register data of the user
        long registerTimeStamp = metadata.getCreationTimestamp();

        //Define a pattern for the data
        String datePattern = "E dd MMMM yyyy hh:mm a z";//Day, dd MMMM yyyy hh:mm AM/PM TimeZone
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        sdf.setTimeZone(TimeZone.getDefault());
        String register = sdf.format(new Date(registerTimeStamp));
        String registerDate = getResources().getString(R.string.user_since, register);
        textViewRegistration.setText(registerDate);
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        textViewEmail.setText(email);
        textViewName.setText(name);
        String welcome = getResources().getString(R.string.welcome_user, name);
        textViewWelcome.setText(welcome);
        progressBar.setVisibility(View.GONE);

    }

    private void signOut() {
        Button buttonSignOut = findViewById(R.id.button_signout);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(UserProfileActivity.this, MainActivity.class);
                //clear the stack to prevent using the back button
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                finish();

            }
        });

    }

    private void updateInfo() {
        Button buttonSignOut = findViewById(R.id.button_updateinfo);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(UserProfileActivity.this, update.class);
                //clear the stack to prevent using the back button
                update.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(update);
                finish();

            }
        });
    }

    private void findViews() {
        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewName = findViewById(R.id.textView_show_name);
        textViewEmail= findViewById(R.id.textView_show_email);
        textViewRegistration = findViewById(R.id.textView_show_register_date);
        progressBar = findViewById(R.id.progressbar);
    }
}