package com.csform.android.uiapptemplate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.model.FavorModel;


public class FavorSpecActivity extends ActionBarActivity {

    private FavorModel fm;

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
        title.setText(fm.getTitle());
        desc.setText(fm.getDesc());
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
