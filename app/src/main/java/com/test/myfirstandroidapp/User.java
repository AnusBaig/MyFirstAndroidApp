package com.test.myfirstandroidapp;

public class User {
    private int userId;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public User(){
    }

    public User(int id,String email,String first,String last,String phone){
        userId =id;
        emailAddress=email;
        firstName=first;
        lastName=last;
        phoneNumber=phone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
