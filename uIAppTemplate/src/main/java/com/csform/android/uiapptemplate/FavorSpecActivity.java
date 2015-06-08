package com.csform.android.uiapptemplate;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.model.FavorModel;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;


public class FavorSpecActivity extends ActionBarActivity {

    private FavorModel fm;
    private String LOG_TAG = "FavorSpecActivity";
    private static String nameField = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_spec);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fm = (FavorModel) extras.getSerializable("fm");
        } else {
            int duration = Toast.LENGTH_SHORT;
            String s = "Unable to retrieve data";
            Toast toast = Toast.makeText(getApplicationContext(), s, duration);
            toast.show();
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com");
//        final Firebase userRef = ref.child("users").child(fm.getUser());
        Log.i("To check",  fm.getUser());
        ref.child("users").child(fm.getUser())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) errorKill();
                        Map<?, ?> userDataMap = (Map<?, ?>) dataSnapshot.getValue();
                        if (userDataMap == null) errorKill();
                        Log.i("Checking",  fm.getUser());
                        for(DataSnapshot child : dataSnapshot.getChildren())
                            Log.i("Checking",  child + "");
                        nameField = userDataMap.get("name") + "";
                        if(nameField.equals("null")) errorKill();
                        updateView();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
        TextView title = (TextView) findViewById(R.id.titleText);
        TextView desc = (TextView) findViewById(R.id.descText);
        /*
        TextView datePosted = (TextView) findViewById(R.id.postedDateText);
        TextView dateDoneBy = (TextView) findViewById(R.id.completedDateText);*/
        TextView compensation = (TextView) findViewById(R.id.compText);

        title.setText(fm.getTitle());
        /*
        dateDoneBy.setText("Do Favor/Accept Offer by "+fm.getDateDoneBy());
        datePosted.setText("Posted on "+fm.getDatePosted()); */
        compensation.setText("Compensation: " + fm.getCompensation());

        desc.setText(fm.getDesc());

//        TextView userAccepted = (TextView) findViewById(R.id.userAcceptedText);
        // only change the userAcceptedText if we don't get "",
        // which means no one has accepted to do this yet
        /*
        Log.i(LOG_TAG, "fm.getUserAccepted() =" + fm.getUserAccepted());
        String userAcceptedString = fm.getUserAccepted();
        if ( userAcceptedString.length() > 0){
            userAccepted.setText("User "+ userAcceptedString+" has accepted this favor offer/request");
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favor_spec, menu);
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
    private void updateView(){
        View V = this.findViewById(android.R.id.content);
        if(V == null) return;
        TextView nameView = (TextView) V.findViewById(R.id.nameText);
        nameView.setText(nameField);
    }
    private void errorKill() {
        Context context = getApplicationContext();
        CharSequence text = "Error retrieving favor data";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        finish();
    }
}
