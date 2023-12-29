/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.util.LinkedList;

/**
 *
 * @author ntu-user
 */
public class CommonClass {

    public String localDirectory = "Folder/";
    public LinkedList<String> fileList = new LinkedList();
    private String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {

            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index = (int) (this.AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb 
            sb.append(this.AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

}
