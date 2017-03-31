package com.example.x1243.amdproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleBlogPostPage extends AppCompatActivity {

    private String mPost_key = null;

    private DatabaseReference database;

    private ImageView singlePostImage;

    private TextView singlePostTitle;
    private TextView singlePostDesc;

    private Button singleRemoveBtn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_blog_post_page);

        database = FirebaseDatabase.getInstance().getReference().child("Blog");
        firebaseAuth = firebaseAuth.getInstance();

        String mPost_key = getIntent().getExtras().getString("blog_id");

        singlePostImage = (ImageView)findViewById(R.id.single_post_image);

        singleRemoveBtn = (Button)findViewById(R.id.singleRemoveBtn);

        singlePostTitle = (TextView) findViewById(R.id.single_post_title);
        singlePostDesc = (TextView) findViewById(R.id.single_post_desc);



        //Toast.makeText(SingleBlogPostPage.this, post_key, Toast.LENGTH_LONG).show();

        database.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singlePostTitle.setText(post_title);
                singlePostDesc.setText(post_desc);

                Picasso.with(SingleBlogPostPage.this).load(post_image).into(singlePostImage);


                if(firebaseAuth.getCurrentUser().getUid().toString().equals(post_uid)){
                    singleRemoveBtn.setVisibility(View.VISIBLE);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
