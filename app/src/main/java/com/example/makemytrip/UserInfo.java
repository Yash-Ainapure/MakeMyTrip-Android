package com.example.makemytrip;

import android.util.Log;

public class UserInfo {
    public String userId,firstName,lastName, phoneNumber, email;
    public String gender="",city="",state="",nationality="",passportNo="",issuingCountry="",panCard="",selectedCountry="";
    String panExpiry="",dob="";

    public UserInfo() {
        //Default Constructor
    }
    public UserInfo(String email, String firstName, String lastName, String phoneNumber,String userId ,String selectedCountry) {
        Log.d("UserInfo", "UserInfo: " + userId + " " + firstName + " " + lastName + " " + phoneNumber + " " + email);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.selectedCountry = selectedCountry;
    }

    public UserInfo(String userId,String firstName, String phoneNumber, String email,
                    String gender, String city, String state, String nationality,
                    String passportNo, String issuingCountry, String panCard,String panExpiry,String dob,String selectedCountry) {
        this.userId=userId;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.nationality = nationality;
        this.passportNo = passportNo;
        this.issuingCountry = issuingCountry;
        this.panCard = panCard;
        this.panExpiry=panExpiry;
        this.dob=dob;
        this.selectedCountry=selectedCountry;
    }

    public String getSelectedCountry(){
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public String getPanExpiry() {
        return panExpiry;
    }

    public void setPanExpiry(String panExpiry) {
        this.panExpiry = panExpiry;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
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
