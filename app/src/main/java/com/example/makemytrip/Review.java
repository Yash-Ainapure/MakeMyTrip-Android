package com.example.makemytrip;

public class Review {
    String Date;
    String review;
    String userID;
    public Review(){

    }
    public Review(String date, String review,String userID) {
        this.Date = date;
        this.review = review;
        this.userID=userID;
    }
    public String getUserId() {
        return userID;
    }
    public void setUserId(String userId) {
        this.userID = userId;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
}
