package com.test.myfirstandroidapp;

import android.graphics.Color;
import android.os.Bundle;
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
        Toast.makeText(FirstFragment.super.getContext(),"Fetching Users from Firebase database...",Toast.LENGTH_LONG).show();

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userDb = database.getReference().child("users");

            FetchUsers(userDb, table);

            view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_firstFragment_to_secondFragment);
                }
            });
        }
        catch (Exception e){
            Toast.makeText(FirstFragment.super.getContext(),"Unknown error occurred while fetching users",Toast.LENGTH_LONG).show();
        }
    }

    public void FetchUsers(DatabaseReference userDb,TableLayout table){
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot :dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);

                        TableRow row = new TableRow(FirstFragment.super.getContext());

                        TextView id = new TextView(FirstFragment.super.getContext());
                        id.setId(getId());
                        id.setText("   "+user.getUserId());
                        id.setTextColor(Color.BLUE);
                        row.addView(id, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        id.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle args= new Bundle();
                                String [] fetchedUser=new String[5];
                                fetchedUser[0]=""+user.getUserId();
                                fetchedUser[1]=user.getEmailAddress();
                                fetchedUser[2]=user.getFirstName();
                                fetchedUser[3]=user.getLastName();
                                fetchedUser[4]=user.getPhoneNumber();

                                args.putStringArray("User",fetchedUser);

                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_firstFragment_to_secondFragment,args);
                            }
                        });

                        createCell(row,150,user.getEmailAddress());
                        createCell(row,60,user.getFirstName());
                        createCell(row,60,user.getLastName());
                        createCell(row,40,user.getPhoneNumber());

                        table.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FirstFragment.super.getContext(),"Users couldn't fetched from database",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createCell(TableRow row,int width,String value){
        TextView cell = new TextView(FirstFragment.super.getContext());
        cell.setId(getId());
        cell.setText(value);
        row.addView(cell, width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}