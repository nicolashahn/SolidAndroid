package com.csform.android.uiapptemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.model.UserModel;
import com.csform.android.uiapptemplate.util.ImageUtil;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class UserProfileEditActivity extends ActionBarActivity {
    private EditText nameField;
    private EditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameField = (EditText) findViewById(R.id.nameEdit);
        nameField.setText(UserModel.getField(this, "name"));
        phoneField = (EditText) findViewById(R.id.phoneEdit);
        phoneField.setText(UserModel.getField(this, "phone"));
        TextView emailEdit = (TextView) findViewById(R.id.emailView);
        emailEdit.setText(UserModel.getField(this, "email"));
        ImageView avatarView = (ImageView) findViewById(R.id.image);
        ImageUtil.displayImage(avatarView, UserModel.getField(this, "avatar"), null);
        TextView cancelButton = (TextView) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });
        TextView saveButton = (TextView) findViewById(R.id.saveChanges);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        final String nameEdit = nameField.getText().toString();
        final String phoneEdit = phoneField.getText().toString();

        if (nameEdit.matches("") || phoneEdit.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "You cannot leave a field blank";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        if (phoneEdit.length() != 10) {
            Context context = getApplicationContext();
            CharSequence text = "Phone number must be 10 digits";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        final Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com");
        final Firebase userRef = ref.child("users").child(emailToKey(UserModel.getField(this, "email")));
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", nameEdit);
        userData.put("phone", phoneEdit);
        userRef.updateChildren(userData);
                /*
                 * How do I update User Model here?
                 * UserModel.setField(this, "phone", "newPhone");
                 */
//          UserModel.setField(LeftMenusActivity, "phone", "newPhone");
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_favor_spec, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	public String emailToKey(String emailAddress) {
		return emailAddress.replace('.', ',');
	}
}
