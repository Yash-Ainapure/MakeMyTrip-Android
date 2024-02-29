package com.example.makemytrip;

import android.os.Parcel;
import android.os.Parcelable;

public class HolidayPackageInfo implements Parcelable {

    private String imageurl;
    private String packagedetails;
    private String packageduration;
    private String packagename;
    private int price;

    public HolidayPackageInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(HolidayPackageInfo.class)
    }

    public HolidayPackageInfo(String imageurl, String packagedetails, String packageduration, String packagename, int price) {
        this.imageurl = imageurl;
        this.packagedetails = packagedetails;
        this.packageduration = packageduration;
        this.packagename = packagename;
        this.price = price;
    }

    protected HolidayPackageInfo(Parcel in) {
        imageurl = in.readString();
        packagedetails = in.readString();
        packageduration = in.readString();
        packagename = in.readString();
        price = in.readInt();
    }

    public static final Creator<HolidayPackageInfo> CREATOR = new Creator<HolidayPackageInfo>() {
        @Override
        public HolidayPackageInfo createFromParcel(Parcel in) {
            return new HolidayPackageInfo(in);
        }

        @Override
        public HolidayPackageInfo[] newArray(int size) {
            return new HolidayPackageInfo[size];
        }
    };

    public String getImageUrl() {
        return imageurl;
    }

    public String getPackageDetails() {
        return packagedetails;
    }

    public String getPackageDuration() {
        return packageduration;
    }

    public String getPackageName() {
        return packagename;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageurl);
        dest.writeString(packagedetails);
        dest.writeString(packageduration);
        dest.writeString(packagename);
        dest.writeInt(price);
    }
}

