package com.example.makemytrip;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Hotel implements Parcelable {
    private String id;
    private String name;

    private String locationUrl;

    private String address;
    private String imageUrl;
    private Float rating;
    private boolean isLiked;
    private int price;
    private List<String> otherImages;
    private String state;
    private String city;



    public void logAllVariables(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Log.d("Hotel Classs", field.getName() + ": " + field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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
    public Hotel(String id, String name, String address, String imageUrl, boolean isLiked, int price, List<String> otherImages, float rating, String state, String city, String locationUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageUrl = imageUrl;
        this.isLiked = isLiked;
        this.price = price;
        this.otherImages = otherImages;
        this.rating = rating;
        this.state = state;
        this.city = city;
        this.locationUrl = locationUrl;
        Log.d("Hotel1", "Hotel: " + id + " " + name + " " + address + " " + imageUrl + " " + isLiked + " " + price + " " + otherImages + " " + rating + " " + state + " " + city + " " + locationUrl);
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeInt(price);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(locationUrl);
        dest.writeList(otherImages);
    }


    protected Hotel(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        isLiked = in.readByte() != 0;
        price = in.readInt();
        state = in.readString();
        city = in.readString();
        locationUrl = in.readString();
        otherImages = new ArrayList<>();
        in.readList(otherImages, String.class.getClassLoader());
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


    public Float getRating() {

        if (rating == null) {
            rating = 0.0f;
        }

        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
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

}

