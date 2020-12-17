package com.test.myfirstandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Second3Fragment extends Fragment {
    private User matchedUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        if(getArguments()!=null){
            // Take all arguments and save it as a Matched User
            String[] fetchedUser = getArguments().getStringArray("User");
            matchedUser = new User(Integer.parseInt(fetchedUser[0]),fetchedUser[1],fetchedUser[2],fetchedUser[3],fetchedUser[4]);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second3, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get database refrence of users collection
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users");

        // Get refrences of allinputs from form
        EditText idInput = (EditText) view.findViewById(R.id.txt_sql_input_userId);
        EditText emailInput = (EditText) view.findViewById(R.id.txt_sql_input_email);
        EditText firstNameInput = (EditText) view.findViewById(R.id.txt_sql_input_firstName);
        EditText lastNameInput = (EditText) view.findViewById(R.id.txt_sql_input_lastName);
        EditText phoneInput = (EditText) view.findViewById(R.id.txt_sql_input_phone);

        // If form is nvaigated for new post, change its label
        ((TextView) view.findViewById(R.id.txt_post_sql_action_title)).setText("New User");

        // Check if any matchUSer exist to update details
        if(matchedUser!=null){
            if(matchedUser.getUserId()>0)
                // If form is nvaigated for updating post, change its label
                ((TextView) view.findViewById(R.id.txt_post_sql_action_title)).setText("Update User");

            // Get all values from form inputs refrences
            idInput.setText(""+matchedUser.getUserId());
            emailInput.setText(matchedUser.getEmailAddress());
            firstNameInput.setText(matchedUser.getFirstName());
            lastNameInput.setText(matchedUser.getLastName());
            phoneInput.setText(matchedUser.getPhoneNumber());
        }

        // Set click listener on Submit button
        view.findViewById(R.id.btn_sql_dashboard).setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(Second3Fragment.super.getContext(),"Invalid Form Data",Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Create a user from provided form details
                    User user = new User(id,email,firstName,lastName,phone);

                    // Check if any matchUSer exist to update details
                    if(matchedUser!=null){
                        // Updating field in database whose values has updated in form
                        UpdateUser(userDb,matchedUser.getUserId(),user);
                        // Show toast message as post updated
                        Toast.makeText(Second3Fragment.super.getContext(),"User updated",Toast.LENGTH_LONG).show();
                    }
                    else{
                        AddUser(userDb,user);
                        // Show toast message as post created
                        Toast.makeText(Second3Fragment.super.getContext(),"New user added",Toast.LENGTH_LONG).show();
                    }

                    // Navigates to Users List page
                    NavHostFragment.findNavController(Second3Fragment.this)
                            .navigate(R.id.action_second3Fragment_to_first3Fragment);
                }
                catch (Exception e){
                    // Show toast message as Unknown error
                    Toast.makeText(Second3Fragment.super.getContext(),"Unknown error occurred while posting..",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set click listener on Delete button
        view.findViewById(R.id.btn_sql_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Check if any matchUSer exist to update details
                    if(matchedUser!=null){
                        //Deleting User from database
                        DeleteUser(userDb,matchedUser.getUserId());
                        // Show toast message as User deleted
                        Toast.makeText(Second3Fragment.super.getContext(),"User deleted succefully",Toast.LENGTH_LONG).show();

                        // Navigates to Users List page
                        NavHostFragment.findNavController(Second3Fragment.this)
                                .navigate(R.id.action_second3Fragment_to_first3Fragment);
                    }
                    else{
                        // Show toast message as No user found
                        Toast.makeText(Second3Fragment.super.getContext(),"No user found",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    // Show toast message as Unknown error
                    Toast.makeText(Second3Fragment.super.getContext(),"Unknown error occurred while deleting..",Toast.LENGTH_LONG).show();
                }
            }
        });
        }
    public void AddUser(DatabaseReference userDb, User user){
    }
    public void UpdateUser(DatabaseReference userDb,int userId,User user){
    }
    public void DeleteUser(DatabaseReference userDb,int id){
    }
}