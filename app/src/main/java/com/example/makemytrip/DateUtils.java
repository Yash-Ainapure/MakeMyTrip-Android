package com.example.makemytrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static long getDuration(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

        try {
            Date startDateObj = sdf.parse(startDate);
            Date endDateObj = sdf.parse(endDate);

            // Calculate the duration in milliseconds
            long durationInMillis = endDateObj.getTime() - startDateObj.getTime();

            // Convert milliseconds to days
            return TimeUnit.MILLISECONDS.toDays(durationInMillis);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Handle the error as needed
        }
    }

}
