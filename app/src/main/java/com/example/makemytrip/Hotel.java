package com.example.makemytrip;

import android.os.Parcel;
import android.os.Parcelable;

public class Hotel implements Parcelable {
    private String id;
    private String name;
    private String address;
    private String imageUrl;
    private boolean isLiked;

    // Default constructor
    public Hotel() {
    }

    // Parameterized constructor
    public Hotel(String id, String name, String address, String imageUrl, boolean isLiked) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.isLiked = isLiked;
    }

    // Parcelable constructor
    protected Hotel(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        isLiked = in.readByte() != 0;
    }

    // Parcelable creator
    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    // Getter and setter for ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and setter for image URL
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter and setter for isLiked
    public boolean isLiked() {
        return isLiked;
    }
    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isLiked ? 1 : 0));
    }
}

