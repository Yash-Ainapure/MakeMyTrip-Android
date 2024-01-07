package com.example.makemytrip;

public class HotelBookedInfo {
    private String hotelName,hotelAddress;
    private int numberOfRooms,numberOfDays;
    private String startingDate;

    public HotelBookedInfo() {
    }
    public HotelBookedInfo(String hotelName, String hotelAddress, int numberOfRooms, int numberOfDays, String startingDate) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.numberOfRooms = numberOfRooms;
        this.numberOfDays = numberOfDays;
        this.startingDate = startingDate;
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

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }
}
