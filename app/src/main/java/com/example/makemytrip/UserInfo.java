package com.example.makemytrip;

import android.util.Log;

public class UserInfo {
    public String userId,firstName,lastName, phoneNumber, email;

    public UserInfo() {
        //Default Constructor
    }
    public UserInfo(String email, String firstName, String lastName, String phoneNumber,String userId ) {
        Log.d("UserInfo", "UserInfo: " + userId + " " + firstName + " " + lastName + " " + phoneNumber + " " + email);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
