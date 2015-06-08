package com.csform.android.uiapptemplate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.common.Log;
import com.csform.android.uiapptemplate.model.FavorModel;


public class FavorSpecActivity extends ActionBarActivity {

    private FavorModel fm;
    private String LOG_TAG = "FavorSpecActivity";

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

        TextView title = (TextView) findViewById(R.id.titleText);
        TextView desc = (TextView) findViewById(R.id.descText);
        TextView user = (TextView) findViewById(R.id.userText);
        TextView datePosted = (TextView) findViewById(R.id.postedDateText);
        TextView dateDoneBy = (TextView) findViewById(R.id.completedDateText);
        TextView compensation = (TextView) findViewById(R.id.compText);
        title.setText(fm.getTitle());
        user.setText(fm.getUser());
        dateDoneBy.setText("Do Favor/Accept Offer by "+fm.getDateDoneBy());
        datePosted.setText("Posted on "+fm.getDatePosted());
        compensation.setText("Compensation: " + fm.getCompensation());
        desc.setText(fm.getDesc());

        TextView userAccepted = (TextView) findViewById(R.id.userAcceptedText);
        // only change the userAcceptedText if we don't get "",
        // which means no one has accepted to do this yet
        Log.i(LOG_TAG, "fm.getUserAccepted() =" + fm.getUserAccepted());
        String userAcceptedString = fm.getUserAccepted();
        if ( userAcceptedString.length() > 0){
            userAccepted.setText("User "+userAcceptedString+" has accepted this favor offer/request");
        }
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
}
