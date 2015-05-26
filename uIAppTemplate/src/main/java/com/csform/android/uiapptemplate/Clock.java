package com.csform.android.uiapptemplate;

import android.text.format.Time;


/**
 * Created by Nick on 4/24/2015.
 * Original code from stackOverflow:
 * http://stackoverflow.com/questions/8077530/android-get-current-timestamp
 */
public class Clock {
    /**
     * Get current time in human-readable form.
     * @return current time as a string.
     */
    public static String getNow() {
        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%Y/%m/%d %T");
        return sTime;
    }
    /**
     * Get current time in human-readable form without spaces and special characters.
     * The returned value may be used to compose a file name.
     * @return current time as a string.
     */
    public static String getTimeStamp() {
        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%Y_%m_%d_%H_%M_%S");
        return sTime;
    }

}
