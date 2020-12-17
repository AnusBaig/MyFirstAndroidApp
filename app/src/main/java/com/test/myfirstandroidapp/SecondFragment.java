package com.test.myfirstandroidapp;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {

    private User matchedUser;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Check if argument has bundler properties
        // Arguments are provided from Users List page
        if(getArguments()!=null){
            // Take all arguments and save it as a Matched User
            String[] fetchedUser = getArguments().getStringArray("User");
            matchedUser = new User(Integer.parseInt(fetchedUser[0]),fetchedUser[1],fetchedUser[2],fetchedUser[3],fetchedUser[4]);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get database refrence of users collection
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users");

        // Get refrences of allinputs from form
        EditText idInput = (EditText) view.findViewById(R.id.txt_input_userId);
        EditText emailInput = (EditText) view.findViewById(R.id.txt_input_email);
        EditText firstNameInput = (EditText) view.findViewById(R.id.txt_input_firstName);
        EditText lastNameInput = (EditText) view.findViewById(R.id.txt_input_lastName);
        EditText phoneInput = (EditText) view.findViewById(R.id.txt_input_phone);

        // If form is nvaigated for new post, change its label
        ((TextView) view.findViewById(R.id.txt_post_action_title)).setText("New Post");

        // Check if any matchUSer exist to update details
        if(matchedUser!=null){
            if(matchedUser.getUserId()>0)
                // If form is nvaigated for updating post, change its label
                ((TextView) view.findViewById(R.id.txt_post_action_title)).setText("Update Post");

            // Get all values from form inputs refrences
            idInput.setText(""+matchedUser.getUserId());
            emailInput.setText(matchedUser.getEmailAddress());
            firstNameInput.setText(matchedUser.getFirstName());
            lastNameInput.setText(matchedUser.getLastName());
            phoneInput.setText(matchedUser.getPhoneNumber());
        }

            // Set click listener on Submit button
            view.findViewById(R.id.btn_dashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    // Make values of form useable by converting to its preferred data type
                    int id = Integer.parseInt(idInput.getText().toString().trim());
                    String email = emailInput.getText().toString();
                    String firstName = firstNameInput.getText().toString().trim();
                    String lastName = lastNameInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();

                    // Check if form has valid data
                    if(id<=0 || phone.length()<7){
                        Toast.makeText(SecondFragment.super.getContext(),"Invalid Form Data",Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Create a user from provided form details
                    User user = new User(id,email,firstName,lastName,phone);

                    // Check if any matchUSer exist to update details
                    if(matchedUser!=null){
                        // Updating field in database whose values has updated in form
                        UpdateUser(userDb,matchedUser.getUserId(),user);
                        // Show toast message as post updated
                        Toast.makeText(SecondFragment.super.getContext(),"Post updated",Toast.LENGTH_LONG).show();
                    }
                    else{
                        AddUser(userDb,user);
                        // Show toast message as post created
                        Toast.makeText(SecondFragment.super.getContext(),"New post created",Toast.LENGTH_LONG).show();
                    }

                    // Navigates to Users List page
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_secondFragment_to_firstFragment);
                }
                catch (Exception e){
                    // Show toast message as Unknown error
                    Toast.makeText(SecondFragment.super.getContext(),"Unknown error occurred while posting..",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set click listener on Delete button
        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Check if any matchUSer exist to update details
                    if(matchedUser!=null){
                        //Deleting User from database
                        DeleteUser(userDb,matchedUser.getUserId());
                        // Show toast message as User deleted
                        Toast.makeText(SecondFragment.super.getContext(),"User deleted succefully",Toast.LENGTH_LONG).show();

                        // Navigates to Users List page
                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(R.id.action_secondFragment_to_firstFragment);
                    }
                    else{
                        // Show toast message as No user found
                        Toast.makeText(SecondFragment.super.getContext(),"No user found",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    // Show toast message as Unknown error
                    Toast.makeText(SecondFragment.super.getContext(),"Unknown error occurred while deleting..",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Adding new user to database
    public void AddUser(DatabaseReference userDb, User user){
        // Make a child refrence in database
        // Set values in that refrence
        userDb.child(""+user.getUserId()).setValue(user);
    }

    // Get user details from datbase by ID
    public void GetUserById(DatabaseReference userDb, int userId){
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

                        // When desired user matches with given user
                        // Save user details as a matched user
                        if(user.getUserId() == userId) matchedUser=user;
                    }
                }
            }

            // If refrence is not valid to take snapshot
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show toast message as User not exist
                Toast.makeText(SecondFragment.super.getContext(),"User doesn't exist with id= "+userId,Toast.LENGTH_LONG).show();
            }
        });
    }

    // Update user details in databse
    public void UpdateUser(DatabaseReference userDb,int userId,User user){
        User[] userFound = new User[1];
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
                        User fetchedUser = userSnapshot.getValue(User.class);

                        if(fetchedUser.getUserId() == userId) {
                            // When desired user matches with given user
                            // Save user details as a matched user
                            userDb.child(""+userId).setValue(user);
                        }
                    }
                }
            }

            // If refrence is not valid to take snapshot
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show toast message as User not exist
                Toast.makeText(SecondFragment.super.getContext(),"User doesn't exist with id= "+userId,Toast.LENGTH_LONG).show();
            }
        });
    }

    // Delete existing user from database
    public void DeleteUser(DatabaseReference userDb,int id){
        // Go to that refrence in databse where user exist
        // Remove user node from that refrence
        userDb.child(""+id).removeValue();
    }

}