package com.csform.android.uiapptemplate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.csform.android.uiapptemplate.fragment.FavorFormFragment;
import com.csform.android.uiapptemplate.fragment.ParallaxEffectsFragment;
import com.csform.android.uiapptemplate.fragment.ReqOffListFragment;
import com.csform.android.uiapptemplate.fragment.UserProfileFragment;
import com.csform.android.uiapptemplate.model.DrawerItem;
import com.csform.android.uiapptemplate.model.FavorModel;
import com.csform.android.uiapptemplate.model.UserModel;
import com.csform.android.uiapptemplate.util.ImageUtil;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeftMenusActivity extends ActionBarActivity
		implements ReqOffListFragment.OnFragmentInteractionListener {

	public static final String LEFT_MENU_OPTION = "com.csform.android.uiapptemplate.LeftMenusActivity";
	public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
	public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";
	private static final String FIREBASE_URL = "https://crackling-torch-5178.firebaseio.com/";
	private static final UserModel USER_DATA = new UserModel();
	private static Firebase ref;
	private ListView mDrawerList;
	private List<DrawerItem> mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String LOG_TAG = "LeftMenusActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ref = new Firebase("https://crackling-torch-5178.firebaseio.com");
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final Context context = this;

		Intent intent = getIntent();
		if (!intent.getExtras().containsKey("email")) {
			errorKill();
		}
		USER_DATA.setField(context, "email", intent.getStringExtra("email"));
		Log.i(LOG_TAG, "getField call = " + USER_DATA.getField(context, "email"));
		ref.child("users").child(emailToKey(USER_DATA.getField(context, "email")))
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if (!dataSnapshot.exists()) errorKill();
						Log.i(LOG_TAG, dataSnapshot.toString());
						Map<?, ?> userDataMap = (Map<?, ?>) dataSnapshot.getValue();


						if (userDataMap == null) {
							errorKill();
						}
						USER_DATA.setField(context, "name", userDataMap.get("name").toString());
						USER_DATA.setField(context, "phone", userDataMap.get("phone").toString());
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

	LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			//do something with location received
			displayLocation(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}


	};
	double _lat = 0.0;
	double _long = 0.0;
	double _acc = 0.0;
	boolean locationgiven = false;

	private void displayLocation(Location location) {
		if (location == null) {
			locationgiven = false;
		} else {
			locationgiven = true;

			_lat = location.getLatitude();


			_long = location.getLongitude();

			_acc = location.getAccuracy();

			//can choose to color the accuracy here, but naaah

		}
	}

	String full_name = "";

	private void setAdapter() {
		String option = LEFT_MENU_OPTION_1;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LEFT_MENU_OPTION)) {
			option = extras.getString(LEFT_MENU_OPTION, LEFT_MENU_OPTION_1);
		}

		boolean isFirstType = true;

		View headerView = null;

/*
		Firebase ref = new Firebase("https://crackling-torch-5178.firebaseio.com");
		ref.child(AUTH_USER_ID).child("FullName").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot datasnap) {
				full_name = datasnap.getKey();
				String aud = datasnap.getKey();
				Context context = getApplicationContext();
				CharSequence text = "Name:" + full_name + " UID =" + aud;
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();


			}

			@Override
			public void onCancelled(FirebaseError error) {
				Context context = getApplicationContext();
				CharSequence text = "Error";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

			}
		});
*/
		if (option.equals(LEFT_MENU_OPTION_1)) {
			headerView = prepareHeaderView(R.layout.header_navigation_drawer_1,
					"http://pengaja.com/uiapptemplate/avatars/0.jpg",
					full_name + "");
		} else if (option.equals(LEFT_MENU_OPTION_2)) {
			headerView = prepareHeaderView(R.layout.header_navigation_drawer_2,
					"http://pengaja.com/uiapptemplate/avatars/0.jpg",
					full_name + "");
			isFirstType = false;
		}

		BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);

		mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
		mDrawerList.setAdapter(adapter);
	}

	private View prepareHeaderView(int layoutRes, String url, String email) {
		View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
		ImageView iv = (ImageView) headerView.findViewById(R.id.image);
		TextView tv = (TextView) headerView.findViewById(R.id.email);

		ImageUtil.displayRoundImage(iv, url, null);
		tv.setText(email);

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
			Fragment newFragment = ParallaxEffectsFragment.newInstance();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
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
			Fragment newFragment = ReqOffListFragment.newInstance(FIREBASE_URL, "requests");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (position == 3) {
			Fragment newFragment = ReqOffListFragment.newInstance(FIREBASE_URL, "offers");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (position == 4) {
			Fragment newFragment = FavorFormFragment.newInstance(FIREBASE_URL, "requests");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (position == 5) {
			Fragment newFragment = FavorFormFragment.newInstance(FIREBASE_URL, "offers");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		if (position == 6) {
			Fragment newFragment = UserProfileFragment.newInstance(FIREBASE_URL + "user_database", USER_DATA.getField(this, "name"));
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.fragment, newFragment);
			transaction.addToBackStack(null);
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

	@Override
	public void onFragmentInteraction(FavorModel fm) {
		Intent intent = new Intent(LeftMenusActivity.this, FavorSpecActivity.class);
		intent.putExtra("fm", fm);
		this.startActivity(intent);
	}

	/**
	 * Firebase keys cannot have a period (.) in them, so this converts the emails to valid keys
	 */
	public String emailToKey(String emailAddress) {
		return emailAddress.replace('.', ',');
	}

	// If user data cannot be retrieved, display error and kill activity
	private void errorKill() {
		Context context = getApplicationContext();
		CharSequence text = "Error retrieving user data";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		Intent myIntent = new Intent(this, LogInPageActivity.class);
		startActivity(myIntent);
		finish();

	}
}