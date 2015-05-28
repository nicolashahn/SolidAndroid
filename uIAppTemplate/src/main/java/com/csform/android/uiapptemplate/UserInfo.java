package com.csform.android.uiapptemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Nick on 5/26/2015.
 *
 * This is from Luca's AppInfo class
 * TODO: have it store all user parameters:
 *      real name
 *      username
 *      date joined
 *      phone number
 *      email
 *      profile picture (optional)
 *      list of favors this person has posted
 *      list of favors this person has completed
 *      rating ? probably not going to implement
 */
public class UserInfo {
    private static String LOG_TAG = "UserInfo";

    // for testing purposes
    public static final String PREF_USERID = "Mayweather";

    private static UserInfo instance = null;

    protected UserInfo() {
        // Exists only to defeat instantiation.
    }

    // Here are some values we want to keep global.
    public String userid;

    public static UserInfo getInstance(Context context) {
        if (instance == null) {
            instance = new UserInfo();
            // Creates a userid, if I don't have one.
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            instance.userid = settings.getString(PREF_USERID, null);
            if (instance.userid == null) {

                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_USERID, instance.userid);

                Log.i(LOG_TAG, "setting userID as " + instance.userid);

                editor.commit();
            }
        }
        return instance;
    }
}
