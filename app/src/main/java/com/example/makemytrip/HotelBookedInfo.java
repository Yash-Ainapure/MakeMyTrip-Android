package com.example.makemytrip;

public class HotelBookedInfo {
    private String hotelName,hotelAddress;
    private int numberOfRooms;
    private String startingDate,imageUrl,endingDate;

    public HotelBookedInfo() {
    }
    public HotelBookedInfo(String hotelName, String hotelAddress, int numberOfRooms, String startingDate,String endingDate, String imageUrl) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.numberOfRooms = numberOfRooms;
        this.startingDate = startingDate;
        this.endingDate=endingDate;
        this.imageUrl = imageUrl;
    }

    public String getEndingDate() {
        return endingDate;
    }
    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }
}
