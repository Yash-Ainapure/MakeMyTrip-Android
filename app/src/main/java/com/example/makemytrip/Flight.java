package com.example.makemytrip;

import android.os.Parcelable;

import android.os.Parcel;

public class Flight implements Parcelable {
    private String flightId;
    private String flightName;
    private String departureCity;
    private String destinationCity;
    private double flightPrice;
    private boolean isLiked;
    private String departureTime;
    private String destinationTime;
    private String flightImage;

    // Default constructor
    public Flight() {
        // Default constructor required for Firebase
    }

    // Constructor with parameters
    // Constructor with parameters
    public Flight(String flightId, String flightName, String departureCity, String destinationCity, double flightPrice, boolean isLiked, String departureTime, String destinationTime,String flightImage) {
    this.flightId = flightId;
    this.flightName = flightName;
    this.departureCity = departureCity;
    this.destinationCity = destinationCity;
    this.flightPrice = flightPrice;
    this.isLiked = isLiked;
    this.departureTime = departureTime;
    this.destinationTime = destinationTime;
    this.flightImage = flightImage;
}

// Parcelable constructor
    protected Flight(Parcel in) {
    flightId = in.readString();
    flightName = in.readString();
    departureCity = in.readString();
    destinationCity = in.readString();
    flightPrice = in.readDouble();
    isLiked = in.readByte() != 0;
    departureTime = in.readString();
    destinationTime = in.readString();
    flightImage = in.readString();
}



    // Getter and Setter methods

    public String getFlightImage() {
        return flightImage;
    }
    public void setFlightImage(String flightImage) {
        this.flightImage = flightImage;
    }
    public String getDepartureTime() {
    return departureTime;
}

public void setDepartureTime(String departureTime) {
    this.departureTime = departureTime;
}

public String getDestinationTime() {
    return destinationTime;
}

public void setDestinationTime(String destinationTime) {
    this.destinationTime = destinationTime;
}
    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public double getFlightPrice() {
        return flightPrice;
    }

    public void setFlightPrice(double flightPrice) {
        this.flightPrice = flightPrice;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    // Parcelable methods
    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(flightId);
    dest.writeString(flightName);
    dest.writeString(departureCity);
    dest.writeString(destinationCity);
    dest.writeDouble(flightPrice);
    dest.writeByte((byte) (isLiked ? 1 : 0));
    dest.writeString(departureTime);
    dest.writeString(destinationTime);
    dest.writeString(flightImage);
}
}
