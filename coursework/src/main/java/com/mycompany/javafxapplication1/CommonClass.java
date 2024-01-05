/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntu-user
 */
public class CommonClass {

    public String localDirectory = "/home/ntu-user/APP/temp/";
    public LinkedList<String> fileList = new LinkedList();
    private String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

    String pattern = "dd-MM-yyyy HH:mm:ss";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    Date date = new Date();

    String currentDate = simpleDateFormat.format(date);

    public Date getDate(String date) {
        Date new_date = new Date();
        try {
            new_date = simpleDateFormat.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(CommonClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new_date;
    }

    public String displayDate(String date) {
        String new_pattern = "dd MMM, yyyy hh:mm";
        SimpleDateFormat new_simpleDateFormat = new SimpleDateFormat(new_pattern);
        String new_date = new_simpleDateFormat.format(this.getDate(date));

        return new_date;
    }

    public String parseDate(String date) {
        return simpleDateFormat.format(getDate(date));
    }

    public String generateRandomString(int n) {
        StringBuilder stringBuilder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {

            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index = (int) (this.AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb 
            stringBuilder.append(this.AlphaNumericString.charAt(index));
        }

        return stringBuilder.toString();
    }

    public Boolean checkFileExpiry(String date) {
        Boolean flag=false;
        try {
            Date d2 = this.simpleDateFormat.parse(this.currentDate);
            Date d1 = this.simpleDateFormat.parse(date);

            long difference_In_Time = d2.getTime() - d1.getTime();

            
            long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time);
            
            if(difference_In_Days>=31)
                flag=true;
            
            System.out.println(d2+": "+difference_In_Days);
           
        } catch (ParseException ex) {
            Logger.getLogger(CommonClass.class.getName()).log(Level.SEVERE, null, ex);
        }
         return flag;
    }

}
