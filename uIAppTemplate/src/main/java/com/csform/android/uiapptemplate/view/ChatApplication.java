package com.csform.android.uiapptemplate.view;

import com.firebase.client.Firebase;


public class ChatApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
