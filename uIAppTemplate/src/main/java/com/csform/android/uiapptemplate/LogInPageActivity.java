package com.csform.android.uiapptemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LogInPageActivity extends Activity implements OnClickListener {

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";

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


		skip.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			TextView tv = (TextView) v;
			//Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();

            String regi = tv.getText().toString();
            if(regi.equalsIgnoreCase("REGISTER")){
                //Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(LogInPageActivity.this, RegistrationPageActivity.class);
                LogInPageActivity.this.startActivity(myIntent);
            }else if(regi.equalsIgnoreCase("LOGIN")) {

                final TextView usernameview = (TextView)findViewById(R.id.emailview);
                final TextView passwordview = (TextView) findViewById(R.id.passview);

                String emailuser = usernameview.getText().toString();
                String pwidpass = passwordview.getText().toString();
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
                            System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                            String value = authData.getUid().toString();

                            Intent myIntent = new Intent(LogInPageActivity.this, LeftMenusActivity.class);
                            myIntent.putExtra("key", value); //Optional parameters
                            LogInPageActivity.this.startActivity(myIntent);
                            finish();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // there was an error Context context = getApplicationContext();

                        }
                    });
                }
            }else{
                String value = "key";
                Intent myIntent = new Intent(LogInPageActivity.this, LeftMenusActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                LogInPageActivity.this.startActivity(myIntent);
            }
		}
	}
}
