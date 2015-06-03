package com.csform.android.uiapptemplate;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.adapter.DrawerAdapter;
import com.csform.android.uiapptemplate.fragment.ExpandableListViewFragment;
import com.csform.android.uiapptemplate.fragment.FavorFormFragment;
import com.csform.android.uiapptemplate.fragment.ParallaxEffectsFragment;
import com.csform.android.uiapptemplate.fragment.UserProfileFragment;
import com.csform.android.uiapptemplate.model.DrawerItem;
import com.csform.android.uiapptemplate.util.ImageUtil;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

public class LeftMenusActivity extends ActionBarActivity {

	public static final String LEFT_MENU_OPTION = "com.csform.android.uiapptemplate.LeftMenusActivity";
	public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
	public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";
    private static final String FIREBASE_URL = "https://crackling-torch-5178.firebaseio.com/";
	private static String AUTH_USER_ID = "default_id";
	private ListView mDrawerList;
	private List<DrawerItem> mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

        Intent intent = getIntent();
        AUTH_USER_ID = intent.getStringExtra("key");
        Toast.makeText(this, "USERID = " + AUTH_USER_ID, Toast.LENGTH_LONG).show();
        Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com/user_database");

        Query queryRef = ref.orderByChild("uid").equalTo(AUTH_USER_ID);


            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    System.out.println(dataSnapshot.getKey());
                    System.out.println(dataSnapshot.child("FullName").getValue().toString());
                    System.out.println(dataSnapshot.child("uid").getChildrenCount());
                    full_name = dataSnapshot.child("FullName").getValue().toString();

                    View headerView = getLayoutInflater().inflate(R.layout.header_navigation_drawer_1, mDrawerList, false);
                    TextView tv = (TextView) headerView.findViewById(R.id.email);
                    System.out.println("Right before the full-Name : " + full_name);
                    tv.setText(full_name);
                    mDrawerList.addHeaderView(headerView);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_view);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		prepareNavigationDrawerItems();
		setAdapter();
		//mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null) {
			mDrawerLayout.openDrawer(mDrawerList);
		}
/*
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);*/

    }
    LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location){
            //do something with location received
            displayLocation(location);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}

        @Override
        public void onProviderEnabled(String provider){}

        @Override
        public void onProviderDisabled(String provider){}



    };
    double _lat = 0.0;
    double _long = 0.0;
    double _acc = 0.0;
    boolean locationgiven = false;
    private void displayLocation(Location location){
        if(location == null){
            locationgiven = false;
        }else{
            locationgiven = true;

            _lat = location.getLatitude();


            _long = location.getLongitude();

            _acc = location.getAccuracy();

            //can choose to color the accuracy here, but naaah

        }
    }
    boolean isFirstType = true;
    View headerView = null;
    String full_name = "";
	private void setAdapter() {
		String option = LEFT_MENU_OPTION_1;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LEFT_MENU_OPTION)) {
			option = extras.getString(LEFT_MENU_OPTION, LEFT_MENU_OPTION_1);
		}
            System.out.println("fullname = " + full_name);

            System.out.println("Afteward");
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_1,
                    "http://pengaja.com/uiapptemplate/avatars/0.jpg",
                    full_name + "test");
            BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);
            //mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
            mDrawerList.setAdapter(adapter);
	}

	private View prepareHeaderView(int layoutRes, String url, String email) {
		View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
		ImageView iv = (ImageView) headerView.findViewById(R.id.image);
		//TextView tv = (TextView) headerView.findViewById(R.id.email);

		ImageUtil.displayRoundImage(iv, url, null);
		//tv.setText(email);

		return headerView;
	}

	private void prepareNavigationDrawerItems() {
		mDrawerItems = new ArrayList<>();
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_linked_in,
						R.string.drawer_title_home,
						DrawerItem.DRAWER_ITEM_TAG_LINKED_IN));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_blog,
						R.string.drawer_title_requests,
						DrawerItem.DRAWER_ITEM_TAG_BLOG));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_git_hub,
						R.string.drawer_title_offers,
						DrawerItem.DRAWER_ITEM_TAG_GIT_HUB));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_instagram,
						R.string.drawer_title_form,
						DrawerItem.DRAWER_ITEM_TAG_INSTAGRAM));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_instagram,
						R.string.drawer_title_profile,
						DrawerItem.DRAWER_ITEM_TAG_INSTAGRAM));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position/*, mDrawerItems.get(position - 1).getTag()*/);
		}
	}

	private void selectItem(int position/*, int drawerTag*/) {
		// minus 1 because we have header that has 0 position
		if (position < 1) { //because we have header, we skip clicking on it
			return;
		}
		String drawerTitle = getString(mDrawerItems.get(position - 1).getTitle());
		Toast.makeText(this, "You selected " + drawerTitle + " at position: " + position, Toast.LENGTH_SHORT).show();

		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItems.get(position - 1).getTitle());
		mDrawerLayout.closeDrawer(mDrawerList);
		if (position == 1) {
			Fragment newFragment = ParallaxEffectsFragment.newInstance();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (position == 2) {
			Fragment newFragment = ExpandableListViewFragment.newInstance(FIREBASE_URL + "requests");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (position == 3) {
			Fragment newFragment = ExpandableListViewFragment.newInstance(FIREBASE_URL + "offers");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
        if(position == 4) {
			// Create new fragment and transaction
			Fragment newFragment = new FavorFormFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment view with this fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
        }
		if(position == 5) {
            Fragment newFragment = UserProfileFragment.newInstance(FIREBASE_URL + "user_database", AUTH_USER_ID);
			// Create new fragment and transaction
			//Fragment newFragment = new UserProfileFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment view with this fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}

        //nick was here
	}
	
	@Override
	public void setTitle(int titleId) {
		setTitle(getString(titleId));
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
