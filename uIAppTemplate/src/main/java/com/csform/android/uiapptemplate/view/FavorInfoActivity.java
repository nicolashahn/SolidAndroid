package com.csform.android.uiapptemplate.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.csform.android.uiapptemplate.R;
import com.firebase.client.Firebase;

public class FavorInfoActivity extends ActionBarActivity {

    public String LOG_TAG = "FavorInfoActivity";
    private Firebase ref;
    public String FIREBASE_REQ_URL = "https://crackling-torch-5178.firebaseio.com/requests";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_info);
        Firebase.setAndroidContext(this);
        ref = new Firebase(FIREBASE_REQ_URL);
        // list view passes favor id to this activity
        // get id from bundle
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favor_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void doFavorButton(View v){
        // update the favor in the database
        // change the favor's user completing field to be this user

    }
}
