package com.csform.android.uiapptemplate.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Nick on 6/2/2015.
 */
public class UserModel implements Serializable {
    private String LOG_TAG = "UserModel";

    private String dateJoined;
    private String email;
    private String name;
    private String phone;
//    private Integer rating;
    private String avatar;

    private static UserModel instance = null;

    // given no arguments, creates a default UserModel instance
    public UserModel(){
        dateJoined = "defaultDate";
        email = "default@email.com";
        name = "Default Name";
        phone = "5551234567";
        avatar = "no avatar";
    }

    public UserModel( String dj, String e, String n, String p, String a){
        dateJoined = dj;
        email = e;
        name = n;
        phone = p;
        avatar = a;
    }

    // given context and all fields of UserModel, store in SharedPreferences
    public void storeInfo(Context context, String dj, String e, String n, String p, String a){
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("dateJoined", dj);
        editor.putString("email",e);
        editor.putString("name",n);
        editor.putString("phone",p);
        editor.putString("avatar",a);
        editor.apply();
    }

    public UserModel getInfo(Context context){
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(context);
        String dj = "defValue";
        String n = "defValue";
        String e = "defValue";
        String p = "defValue";
        String a = "defValue";
        // Try getting the name first - if we can, then get the rest of the strings
        // relies on the assumption that if n is not null, the other fields will also
        // not be null.
        settings.getString("name", n);
        if (n != null && n != "defValue"){
            settings.getString("dateJoined",dj);
            settings.getString("email",e);
            settings.getString("phone",p);
            settings.getString("avatar",a);
            UserModel um = new UserModel(dj, e, n, p, a);
            return um;
        }

        // if we can't get the data from SharedPreferences, just return a default user
        return new UserModel();
    }

    // 1st arg = context -> UserModel.setField(this, , )
    // 2nd arg = name of the field - dateJoined, email, phone, name, avatar
    // 3rd arg = the field entry - if 2nd arg was "email", then "bob@ucsc.edu"
    public void setField(Context context, String fieldname, String entry){
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(fieldname, entry);
        editor.commit();
        Log.i(LOG_TAG, "Putting "+entry+" in field "+fieldname);
    }

    public static String getField(Context context, String fieldname){
        String result = "";
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(context);
        result = settings.getString(fieldname, result);
        return result;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(Context context, String e) {
//        SharedPreferences settings =
//                PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("email", e);
//        editor.commit();
//    }
//
//    public String getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(Context context, String a) {
//        SharedPreferences settings =
//                PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("avatar", a);
//        editor.commit();
//    }
//
//    public String getName() {
//        Log.i("returning name "+name, name);
//        return name;
//    }
//
//    public void setName(Context context, String n) {
//        Log.i("setting name "+n, n);
//        name = n;
//        SharedPreferences settings =
//                PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("name", n);
//        editor.commit();
//    }
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String p) {
//        phone = p;
//    }
//
//    public String getDateJoined() {
//        return dateJoined;
//    }
//
//    public void setDateJoined(String dj) {
//        dateJoined = dj;
//    }
//
//    @Override
//    public String toString() {
//        return email;
//    }
//
//    // no purpose other than to satisfy the abstract method Adapter
//    public long getId() {
//        return 0;
//    }
}
