package com.csform.android.uiapptemplate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.firebase.client.Firebase;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


public class FavorFormActivity extends ActionBarActivity {

    public String LOG_TAG = "FavorFormActivity";

    public String FIREBASE_URL = "https://crackling-torch-5178.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_favor_form);
        // auto fill the completed by date with tomorrow's date
        autofillDate();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void autofillDate(){
        EditText df = (EditText) findViewById(R.id.dateField);
        String ts = Clock.getTimeStamp();
        df.setText(ts);
        Log.i(LOG_TAG, "in autofillDate()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favor_form, menu);
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
        Firebase ref = new Firebase(FIREBASE_URL);

        // first get whether we're sending a request or offer
        Switch s = (Switch) findViewById(R.id.reqOfferSwitch);
        String type = "requests";
        if(s.isChecked()) {
            type = "offers";
        }

        // now we send either an offer or request
        Firebase favorRef = ref.child(type);
        Firebase newFavorRef = favorRef.push();

        // get all the strings from the form
        EditText titleField = (EditText) findViewById(R.id.titleField);
        String title = titleField.getText().toString();

        EditText descField = (EditText) findViewById(R.id.descField);
        String desc = descField.getText().toString();

        EditText dateField = (EditText) findViewById(R.id.dateField);
        String date = dateField.getText().toString();

        EditText compField = (EditText) findViewById(R.id.compField);
        String comp = compField.getText().toString();

        ///////////////////////////
        // to add:
        // checkbox for monetary compensation,
        // currencyField - float?
        //////////////////////////

        // a random number for the favor ID
        SecureRandom random = new SecureRandom();
        String favorId = new BigInteger(130, random).toString(32);


        // build a request object, send it to server
        Map<String, String> f1 = new HashMap<String, String>();
        f1.put("title", title);
        f1.put("description", desc);
        f1.put("completed", "false");
        f1.put("compensation",comp);
        f1.put("dateToBeCompletedBy",date);
        f1.put("datePosted",Clock.getTimeStamp());
        // change this later - use appInfo from hw3?
        f1.put("userPosted","batman");
        f1.put("favorId",favorId);
        newFavorRef.setValue(f1);


        // this is to get the id of the object we just sent to the server
        String returnId = newFavorRef.getKey();
        Log.i(LOG_TAG, "key id = " + returnId);

//        Toast t = Toast.makeText(this.getApplicationContext(), "Favor posted!", Toast.LENGTH_SHORT);
//        t.show();

    }
}
