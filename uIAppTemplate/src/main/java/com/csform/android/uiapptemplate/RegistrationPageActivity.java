package com.csform.android.uiapptemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class RegistrationPageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_registration_page);

        final TextView nameview = (TextView) findViewById(R.id.fullname);
        final TextView emailview = (TextView) findViewById(R.id.email);
        final TextView passview = (TextView) findViewById(R.id.password);
        final TextView numview = (TextView) findViewById(R.id.phoneNumber);

        TextView submitbut = (TextView) findViewById(R.id.submitbutton);
        submitbut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                final String emailuser = emailview.getText().toString();
                final String pwidpass = passview.getText().toString();
                final String phoneNumber = numview.getText().toString();
                final String fullName = nameview.getText().toString();

                int phoneLen = phoneNumber.length();

                if (emailuser.matches("") || pwidpass.matches("") || fullName.matches("")) {
                    Context context = getApplicationContext();
                    CharSequence text = "You cannot leave a field blank!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if (phoneLen != 10) {
                    Context context = getApplicationContext();
                    CharSequence text = "Phone number must be 10 digits";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    final Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com");
                    final Firebase userRef = ref.child("users");

                    ref.createUser(emailuser, pwidpass, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            // System.out.println("Successfully created user account with uid: " + result.get("uid"));
                            Context context = getApplicationContext();
                            CharSequence text = "Successful!";
                            int duration = Toast.LENGTH_SHORT;

                            // get today's date
                            Calendar c = Calendar.getInstance();
                            DateFormat df = SimpleDateFormat.getDateInstance();
                            df.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String todaysDate = df.format(c.getTime());

                            // build a request object, send it to server
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", fullName);
                            userData.put("phone", phoneNumber);
                            userData.put("dateJoined", todaysDate);

                            //Map<String, Map<String, String>> user = new HashMap<>();
                            //user.put(emailuser, userData);
                            userRef.child(emailToKey(emailuser)).updateChildren(userData);
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
//                      String unique_user_id = result.get("uid").toString();
                            //Intent myIntent = new Intent(RegistrationPageActivity.this, LogInPageActivity.class);
                            //myIntent.putExtra("key", unique_user_id); //Optional parameters
                            //RegistrationPageActivity.this.startActivity(myIntent);
                            Intent i = getIntent(); //gets the intent that called this intent
                            i.putExtra("email", emailuser);
                            Log.i("email", emailuser);
                            setResult(Activity.RESULT_OK, i);
                            finish();
                        }

                        public void onError(FirebaseError firebaseError) {
                            // there was an error
                            Context context = getApplicationContext();
                            CharSequence text = "Error!" + emailToKey(emailuser);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                        }
                    });
                }
            }
        });
    }

    /**
     * Firebase keys cannot have a period (.) in them, so this converts the emails to valid keys
     */
    public String emailToKey(String emailAddress) {
        return emailAddress.replace('.', ',');
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_page, menu);
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
}
