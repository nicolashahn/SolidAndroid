package com.csform.android.uiapptemplate.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Nick on 6/2/2015.
 */
public class UserModel implements Serializable {
    private String dateJoined;
    private String email;
    private String name;
    private String phone;
//    private Integer rating;
    private String avatar;

    public UserModel(){
        dateJoined = "";
        email = "";
        name = "";
        phone = "";
        avatar = "";
    }

    public UserModel(String dj, String e, String n, String p, String a){
        dateJoined = dj;
        email = e;
        name = n;
        phone = p;
        avatar = a;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        email = e;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String a) {
        avatar = a;
    }

    public String getName() {
        Log.i("returninig", name);
        return name;
    }

    public void setName(String n) {
        Log.i("setting, ", n);
        name = n;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String p) {
        phone = p;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dj) {
        dateJoined = dj;
    }

    @Override
    public String toString() {
        return email;
    }

    // no purpose other than to satisfy the abstract method Adapter
    public long getId() {
        return 0;
    }
}
