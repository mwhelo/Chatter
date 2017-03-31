package com.example.x1243.amdproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    ImageView iv;

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button button_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView Homepage_btn = (ImageView) findViewById(R.id.Homepage_btn);
        final Context context = this;
        Homepage_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(context, HomePage.class);
                startActivity(myIntent);
            }
        });

        ImageView message_btn = (ImageView) findViewById(R.id.message_btn);
        message_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(context, Message.class);
                startActivity(myIntent);
            }
        });

        ImageView location_btn = (ImageView) findViewById(R.id.location_btn);
        location_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(context, BlogPage.class);
                startActivity(myIntent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LogIn.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome "+user.getEmail());

        button_logout = (Button) findViewById(R.id.button_logout);

        button_logout.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view == button_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LogIn.class));
        }

    }
}
