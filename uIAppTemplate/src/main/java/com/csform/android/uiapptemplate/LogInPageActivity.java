package com.csform.android.uiapptemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LogInPageActivity extends Activity implements OnClickListener {

    private static final int REG_REQUEST = 1;
    public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";
    public static final String DARK = "Dark";
    public static final String LIGHT = "Light";
    public static String prev_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
        String category = LIGHT;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(LOGIN_PAGE_AND_LOADERS_CATEGORY)) {
            category = extras.getString(LOGIN_PAGE_AND_LOADERS_CATEGORY, LIGHT);
        }
        if (extras != null && extras.containsKey("email")) {
            prev_email = extras.getString("email");
        }
        setContentView(category);
    }

    private void setContentView(String category) {
        if (category.equals(DARK)) {
            setContentView(R.layout.activity_login_page_dark);
        } else if (category.equals(LIGHT)) {
            setContentView(R.layout.activity_login_page_light);
        }
        TextView login, register, skip;
        login = (TextView) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        skip = (TextView) findViewById(R.id.skip);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        TextView emailview = (TextView)findViewById(R.id.emailview);
        emailview.setText(prev_email);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            //Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
            String regi = tv.getText().toString();
            if(regi.equalsIgnoreCase("REGISTER")){
                Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(LogInPageActivity.this, RegistrationPageActivity.class);
                startActivityForResult(myIntent, REG_REQUEST);
                //LogInPageActivity.this.startActivity(myIntent);
            }else if(regi.equalsIgnoreCase("LOGIN")) {
                final TextView usernameview = (TextView) findViewById(R.id.emailview);
                final TextView passwordview = (TextView) findViewById(R.id.passview);
                final String emailuser = usernameview.getText().toString();
                final String pwidpass = passwordview.getText().toString();
                if(emailuser.matches("") || pwidpass.matches("")){
                    Context context = getApplicationContext();
                    CharSequence text = "You cannot leave one or both fields blank!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else {
                    Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com");
                    ref.authWithPassword(emailuser, pwidpass, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Intent myIntent = new Intent(LogInPageActivity.this, MainActivity.class);
                            myIntent.putExtra("email", emailuser);
                            startActivity(myIntent);
                            finish();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // there was an error Context context = getApplicationContext();
                            Context context = getApplicationContext();
                            CharSequence text = "Incorrect email/password combination";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                        }
                    });
                }
            }else{
                Intent myIntent = new Intent(LogInPageActivity.this, MainActivity.class);
//                myIntent.putExtra("email", "a@a.com");
                myIntent.putExtra("email", "skip@pb.com");
                startActivity(myIntent);
                finish();
            }
        }
    }
    @Override
    // Gets result from RegistrationPageActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REG_REQUEST) return; // Check which request we're responding to
        if (resultCode != RESULT_OK) return; // Make sure the request was successful
        if (data.getExtras().containsKey("email")){
            Log.i("Contains", "true");
            TextView emailview = (TextView)findViewById(R.id.emailview);
            emailview.setText(data.getStringExtra("email"));
            TextView passview = (TextView)findViewById(R.id.passview);
            passview.requestFocus();
            // maybe set PW here too if there is a secure way to do so
        }
    }
}
