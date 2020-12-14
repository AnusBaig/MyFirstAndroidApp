package com.test.myfirstandroidapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {

    private Post matchedPost;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        if(getArguments()!=null){
            String[] fetchedPost = getArguments().getStringArray("Post");
            matchedPost = new Post(Integer.parseInt(fetchedPost[0]),fetchedPost[1],fetchedPost[2]);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference postDb = FirebaseDatabase.getInstance().getReference().child("posts");
        EditText idInput = (EditText) view.findViewById(R.id.txt_input_title);
        EditText postedByInput = (EditText) view.findViewById(R.id.txt_input_id);
        EditText titleInput = (EditText) view.findViewById(R.id.txt_input_postedBy);

        ((TextView) view.findViewById(R.id.txt_post_action_title)).setText("New Post");

        if(matchedPost!=null){
            if(matchedPost.getId()>0)
                ((TextView) view.findViewById(R.id.txt_post_action_title)).setText("Update Post");

            idInput.setText(""+matchedPost.getId());
            postedByInput.setText(matchedPost.getPostedBy());
            titleInput.setText(matchedPost.getTitle());
        }

            view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int id = Integer.parseInt(idInput.getText().toString().trim());
                    String postedBy = postedByInput.getText().toString();
                    String title = titleInput.getText().toString().trim();

                    if(id<=0 || postedBy.equals("") || title.equals("")){
                        Toast.makeText(SecondFragment.super.getContext(),"Incomplete Form!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Post post = new Post(id,postedBy,title);

                    if(matchedPost!=null){
                        UpdatePost(postDb,matchedPost.getId(),post);
                        Toast toast = Toast.makeText(SecondFragment.super.getContext(),"Post updated",Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        toastView.setBackgroundColor(Color.BLACK);
                        toast.setView(toastView);
                        toast.show();
                    }
                    else{
                        AddPost(postDb,post);
                        Toast.makeText(SecondFragment.super.getContext(),"New post created",Toast.LENGTH_LONG).show();
                    }

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_secondFragment_to_firstFragment);
                }
                catch (Exception e){
                    Toast.makeText(SecondFragment.super.getContext(),"Unknown error occurred while posting..",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void AddPost(DatabaseReference postDb, Post post){
        postDb.child("post-"+post.getId()).setValue(post);
    }

    public void GetPostById(DatabaseReference userDb, int id){
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot :dataSnapshot.getChildren()) {
                        Post post = userSnapshot.getValue(Post.class);

                        if(post.getId() == id) matchedPost=post;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SecondFragment.super.getContext(),"Post doesn't exist with id= "+id,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UpdatePost(DatabaseReference userDb,int id,Post post){
        Post[] postFound = new Post[1];
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot :dataSnapshot.getChildren()) {
                        Post fetchedPost = userSnapshot.getValue(Post.class);

                        if(fetchedPost.getId() == id) {
                            userDb.child("post-"+id).setValue(post);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SecondFragment.super.getContext(),"Post doesn't exist with id= "+id,Toast.LENGTH_LONG).show();
            }
        });
    }

}