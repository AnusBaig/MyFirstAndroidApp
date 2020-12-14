package com.test.myfirstandroidapp;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TableLayout table = (TableLayout) view.findViewById(R.id.tbl);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userDb = database.getReference().child("posts");

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot postSnapshot :dataSnapshot.getChildren()) {
                        Post post = postSnapshot.getValue(Post.class);

                        TableRow row = new TableRow(FirstFragment.super.getContext());

                        TextView id = new TextView(FirstFragment.super.getContext());
                        id.setId(getId());
                        id.setText(""+post.getId());
                        row.addView(id, 55, ViewGroup.LayoutParams.WRAP_CONTENT);

                        id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle args= new Bundle();
                                String [] fetchedPost=new String[3];
                                fetchedPost[0]=""+post.getId();
                                fetchedPost[1]=post.getPostedBy();
                                fetchedPost[2]=post.getTitle();

                                args.putStringArray("Post",fetchedPost);

                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_firstFragment_to_secondFragment,args);
                            }
                        });

                        TextView postedBy = new TextView(FirstFragment.super.getContext());
                        postedBy.setId(getId());
                        postedBy.setText(post.getPostedBy());
                        row.addView(postedBy, 108, ViewGroup.LayoutParams.WRAP_CONTENT);

                        TextView title = new TextView(FirstFragment.super.getContext());
                        title.setId(getId());
                        title.setText(post.getTitle());
                        row.addView(title, 96, ViewGroup.LayoutParams.WRAP_CONTENT);

                        table.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FirstFragment.super.getContext(),"Fetching from database failed",Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_firstFragment_to_secondFragment);
            }
        });
    }
}