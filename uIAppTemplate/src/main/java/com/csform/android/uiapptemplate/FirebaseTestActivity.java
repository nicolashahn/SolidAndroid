package com.csform.android.uiapptemplate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class FirebaseTestActivity extends ActionBarActivity {

    private String LOG_TAG = "FirebaseTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_firebase_test);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_firebase_test, menu);
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

    public void clickButton(View v){
        // get server reference, then requests database, then build new request
        Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com/");
        Firebase reqRef = ref.child("requests");
        Firebase newReqRef = reqRef.push();

        // build a request object, send it to server
        Map<String, String> req1 = new HashMap<String, String>();
        req1.put("title","let me pet your dog");
        req1.put("description","preferably not a chihuahua");
        req1.put("completed", "false");
        newReqRef.setValue(req1);

        String reqId = newReqRef.getKey();
        Log.i(LOG_TAG, "key id = "+reqId);

    }
}
