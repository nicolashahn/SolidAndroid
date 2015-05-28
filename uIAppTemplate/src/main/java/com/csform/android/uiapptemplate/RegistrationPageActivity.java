package com.csform.android.uiapptemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


public class RegistrationPageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_registration_page);

        final TextView emailview = (TextView)findViewById(R.id.emailid);
        final TextView passview = (TextView)findViewById(R.id.pwid);
        final TextView numview = (TextView)findViewById(R.id.phoneNumber);
        final TextView nameview= (TextView)findViewById(R.id.fullname);

        Button submitbut = (Button)findViewById(R.id.submitbutton);
        submitbut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String emailuser = emailview.getText().toString();
                String pwidpass = passview.getText().toString();
                String phoneNumber=numview.getText().toString();
                String fullName=nameview.getText().toString();

                int phoneLen=phoneNumber.length();

                if(emailuser.matches("") || pwidpass.matches("") || fullName.matches("")){
                    Context context = getApplicationContext();
                    CharSequence text = "You cannot leave a field blank!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else if(phoneLen!=10) {
                    Context context = getApplicationContext();
                    CharSequence text = "Phone number must be 10 digits";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }else{

                        Firebase ref = new Firebase("https://solidtest01.firebaseio.com");
                        ref.createUser(pwidpass, emailuser, new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                // System.out.println("Successfully created user account with uid: " + result.get("uid"));
                                Context context = getApplicationContext();
                                CharSequence text = "Successful!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

//                            String unique_user_id = result.get("uid").toString();
                                Intent myIntent = new Intent(RegistrationPageActivity.this, LogInPageActivity.class);
                                //myIntent.putExtra("key", unique_user_id); //Optional parameters
                                RegistrationPageActivity.this.startActivity(myIntent);
                            }
                            @Override
                            public void onError(FirebaseError firebaseError) {
                                // there was an error
                                Context context = getApplicationContext();
                                CharSequence text = "Error!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        });

                    }
                }


            });



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
