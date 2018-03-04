package com.example.rewan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateConverter {
    public String convertDate(String time) {
        Date date = null;
        String str = null;
        if (time!=null) {
            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}
