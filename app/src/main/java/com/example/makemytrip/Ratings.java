package com.example.makemytrip;

public class Ratings {
    private float totalRating;
    private int numRatings;
    private float averageRating;

    // Constructors, getters, setters, and other methods for Ratings
    // ...
    public Ratings() {
        // Default constructor required for Firebase
    }

    public Ratings(float totalRating, int numRatings, float averageRating) {
        this.totalRating = Float.parseFloat(String.format("%.1f", totalRating));
        this.averageRating = Float.parseFloat(String.format("%.1f", averageRating));

        // Keep the integer value for numRatings
        this.numRatings = numRatings;
    }
    public float getTotalRating() {

        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }
    public String setRatinginwords() {


        if (averageRating != 0 ) {

            if (averageRating  >= 4.5) {
                return "Excellent";
            } else if (averageRating  >= 4.0) {
                return "Very Good";
            } else if (averageRating  >= 3.5) {
                return "Good";
            } else if (averageRating  >= 3.0) {
                return "Average";
            } else if (averageRating  >= 2.5) {
                return "Fair";
            } else if (averageRating  >= 2.0) {
                return "Poor";
            } else if (averageRating  >= 1.0) {
                return "Very Poor";
            } else {
                return "Not Rated";
            }
        } else {

            return "Null rating ";
        }
    }
}
