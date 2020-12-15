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
        if(getArguments()!=null){
            String[] fetchedUser = getArguments().getStringArray("User");
            matchedUser = new User(Integer.parseInt(fetchedUser[0]),fetchedUser[1],fetchedUser[2],fetchedUser[3],fetchedUser[4]);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("users");
        EditText idInput = (EditText) view.findViewById(R.id.txt_input_userId);
        EditText emailInput = (EditText) view.findViewById(R.id.txt_input_email);
        EditText firstNameInput = (EditText) view.findViewById(R.id.txt_input_firstName);
        EditText lastNameInput = (EditText) view.findViewById(R.id.txt_input_lastName);
        EditText phoneInput = (EditText) view.findViewById(R.id.txt_input_phone);

        ((TextView) view.findViewById(R.id.txt_post_action_title)).setText("New Post");

        if(matchedUser!=null){
            if(matchedUser.getUserId()>0)
                ((TextView) view.findViewById(R.id.txt_post_action_title)).setText("Update Post");

            idInput.setText(""+matchedUser.getUserId());
            emailInput.setText(matchedUser.getEmailAddress());
            firstNameInput.setText(matchedUser.getFirstName());
            lastNameInput.setText(matchedUser.getLastName());
            phoneInput.setText(matchedUser.getPhoneNumber());
        }

            view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int id = Integer.parseInt(idInput.getText().toString().trim());
                    String email = emailInput.getText().toString();
                    String firstName = firstNameInput.getText().toString().trim();
                    String lastName = lastNameInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();

                    if(id<=0 || phone.length()<7){
                        Toast.makeText(SecondFragment.super.getContext(),"Invalid Form Data",Toast.LENGTH_LONG).show();
                        return;
                    }
                    User user = new User(id,email,firstName,lastName,phone);

                    if(matchedUser!=null){
                        UpdateUser(userDb,matchedUser.getUserId(),user);
                        Toast.makeText(SecondFragment.super.getContext(),"Post updated",Toast.LENGTH_LONG).show();
                    }
                    else{
                        AddUser(userDb,user);
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

    public void AddUser(DatabaseReference userDb, User user){
        userDb.child(""+user.getUserId()).setValue(user);
    }

    public void GetUserById(DatabaseReference userDb, int userId){
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot :dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);

                        if(user.getUserId() == userId) matchedUser=user;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SecondFragment.super.getContext(),"User doesn't exist with id= "+userId,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UpdateUser(DatabaseReference userDb,int userId,User user){
        User[] userFound = new User[1];
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot userSnapshot :dataSnapshot.getChildren()) {
                        User fetchedUser = userSnapshot.getValue(User.class);

                        if(fetchedUser.getUserId() == userId) {
                            userDb.child(""+userId).setValue(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SecondFragment.super.getContext(),"User doesn't exist with id= "+userId,Toast.LENGTH_LONG).show();
            }
        });
    }

}