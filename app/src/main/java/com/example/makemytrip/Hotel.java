package com.example.makemytrip;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Hotel implements Parcelable {
    private float totalRating;
    private int numRatings;
    private float averageRating;
    private String id;
    private String name;
    private String address;
    private String imageUrl;
    private Float rating;
    private boolean isLiked;
    private int price;
    private List<String> otherImages;


    public List<String> getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(List<String> otherImages) {
        this.otherImages = otherImages;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Default constructor
    public Hotel() {
    }

    // Parameterized constructor
    public Hotel(String id, String name, String address, String imageUrl, boolean isLiked,int price,List<String> otherImages,float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.isLiked = isLiked;
        this.price=price;
        this.otherImages=otherImages;
        this.rating=rating;
    }
    public Hotel( float averageRating , int numRatings,float totalRating ) {
        this.averageRating = averageRating;
        this.numRatings = numRatings;
        this.totalRating =totalRating;
    }

    // Parcelable constructor
    protected Hotel(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        isLiked = in.readByte() != 0;
        price=in.readInt();
        otherImages = new ArrayList<>();
        in.readList(otherImages, String.class.getClassLoader());
        rating=in.readFloat();
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

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public float getTotalRating() {
        return totalRating;
    }


    public void setTotalRating(Float totalRating) {
        this.totalRating = totalRating;
    }

    public int getNumRatings() {

        return numRatings;
    }
    public void setNumRatings(int numRatings) {

        this.numRatings=numRatings;
    }

    public Float getRating() {

        if (rating == null) {
            rating = 0.0f;
        }

        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }


    public String setRatinginwords()
    {


        if (rating != null) {

            if (rating >= 4.5) {
                return "Excellent";
            } else if (rating >= 4.0) {
                return "Very Good";
            } else if (rating >= 3.5) {
                return "Good";
            } else if (rating >= 3.0) {
                return "Average";
            } else if (rating >= 2.5) {
                return "Fair";
            } else if (rating >= 2.0) {
                return "Poor";
            } else if (rating >= 1.0) {
                return "Very Poor";
            } else {
                return "Not Rated";
            }
        }else{

            return "Null rating ";
        }
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

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
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
        dest.writeInt(price);
        dest.writeList(otherImages);
        dest.writeFloat(rating);
    }
}

