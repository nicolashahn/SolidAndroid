package com.csform.android.uiapptemplate.util;

public class FirebaseUtil {
    private static final String URL = "https://crackling-torch-5178.firebaseio.com/";

    /**
     * Return the database URL
     */
    public static String getUrl(){
        return URL;
    }

    /**
     * Firebase keys cannot have a period (.) in them, so this converts the emails to valid keys
     */
    public static String emailToKey(String emailAddress) {
        return emailAddress.replace('.', ',');
    }
}
