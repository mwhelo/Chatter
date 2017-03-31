package com.example.x1243.amdproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogPage extends AppCompatActivity {

    private RecyclerView blogList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private DatabaseReference database;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseLikes;

    private boolean processLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_page);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference().child("Blog");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");

        database.keepSynced(true);
        databaseUsers.keepSynced(true);
        databaseLikes.keepSynced(true);

        blogList = (RecyclerView)findViewById(R.id.blog_list);
        blogList.setHasFixedSize(true);
        blogList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                database
        ){
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, final int position){

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setEmail(model.getEmail());

                viewHolder.setLikeButton(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleBlogIntent = new Intent(BlogPage.this, SingleBlogPostPage.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);

                    }
                });

                viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        processLike = true;

                        databaseLikes.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(processLike) {

                                    if (dataSnapshot.child(post_key).hasChild(firebaseAuth.getCurrentUser().getUid())) {

                                        databaseLikes.child(post_key).child(firebaseAuth.getCurrentUser().getUid()).removeValue();

                                        processLike = false;

                                    } else {
                                        databaseLikes.child(post_key).child(firebaseAuth.getCurrentUser().getUid()).setValue("RandomValue");

                                        processLike = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

            }

        };

        blogList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton likeButton;

        DatabaseReference databaseLikes;
        FirebaseAuth firebaseAuth;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            likeButton = (ImageButton) mView.findViewById(R.id.like_btn);

            databaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
            firebaseAuth = FirebaseAuth.getInstance();

            databaseLikes.keepSynced(true);
        }

        public void setTitle(String Title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(Title);
        }

        public void setDesc(String desc){

            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){

            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }

        public void setEmail(String email){
            TextView post_Email = (TextView)mView.findViewById(R.id.post_Email);
            post_Email.setText(email);
        }

        public void setLikeButton(final String post_key){

            databaseLikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_key).hasChild(firebaseAuth.getCurrentUser().getUid())){

                        likeButton.setImageResource(R.drawable.like_button);

                    } else{

                        likeButton.setImageResource(R.mipmap.ic_thumb_up_black_24dp);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_add)
        {
            startActivity(new Intent(BlogPage.this, PostActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}
