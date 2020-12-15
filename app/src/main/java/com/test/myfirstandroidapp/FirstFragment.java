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
            // Get database refrence to users
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userDb = database.getReference().child("users");

            // Fetch users from database
            FetchUsers(userDb, table);

            // Set click listener on 'new post' button
            view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // On click it navigates to add new form
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_firstFragment_to_secondFragment);
                }
            });
        }
        catch (Exception e){
            // Show toast message as Unknown error
            Toast.makeText(FirstFragment.super.getContext(),"Unknown error occurred while fetching users",Toast.LENGTH_LONG).show();
        }
    }

    //Fetch Users from database
    public void FetchUsers(DatabaseReference userDb,TableLayout table){

        //Get complete snapshot of data from root data refrence in database
        userDb.addValueEventListener(new ValueEventListener() {
            // If refrence is valid to take snapshot
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Check if refrence has data
                if(dataSnapshot.exists()) {
                    // Go to each user node from data snapshot one by one
                    for(DataSnapshot userSnapshot :dataSnapshot.getChildren()) {
                        // Get user details from particular data snapshot refrence
                        User user = userSnapshot.getValue(User.class);

                        // Create row
                        TableRow row = new TableRow(FirstFragment.super.getContext());

                        // Create Id column
                        TextView id = new TextView(FirstFragment.super.getContext());
                        id.setId(getId());
                        id.setText("   "+user.getUserId());
                        id.setTextColor(Color.BLUE);
                        row.addView(id, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        // Add click listener on id column
                        id.setOnClickListener(new View.OnClickListener() {
                            // On click it navigates to update form
                            @Override
                            public void onClick(View v) {
                                //Create bundler
                                Bundle args= new Bundle();
                                // Add properties in bundler from fetched user from database
                                String [] fetchedUser=new String[5];
                                fetchedUser[0]=""+user.getUserId();
                                fetchedUser[1]=user.getEmailAddress();
                                fetchedUser[2]=user.getFirstName();
                                fetchedUser[3]=user.getLastName();
                                fetchedUser[4]=user.getPhoneNumber();

                                // Pass bunddler as argument
                                args.putStringArray("User",fetchedUser);

                                // Pass arguments to form
                                // Navigate to form to to update details
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_firstFragment_to_secondFragment,args);
                            }
                        });

                        // Create cells by provided properties and append in row
                        createCell(row,150,user.getEmailAddress(),Color.BLACK);
                        createCell(row,60,user.getFirstName(),Color.BLACK);
                        createCell(row,60,user.getLastName(),Color.BLACK);
                        createCell(row,40,user.getPhoneNumber(),Color.BLACK);

                        // Append row in table
                        table.addView(row);
                    }
                }
            }

            // If refrence is invalid to take snapshot
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show toast message as User not exist
                Toast.makeText(FirstFragment.super.getContext(),"Users couldn't fetched from database",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createCell(TableRow row,int width,String value,int color){
        TextView cell = new TextView(FirstFragment.super.getContext());
        cell.setId(getId());
        cell.setText(value);
        cell.setTextColor(color);
        row.addView(cell, width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}