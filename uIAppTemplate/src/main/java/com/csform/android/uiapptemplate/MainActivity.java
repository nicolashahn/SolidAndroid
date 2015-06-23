package com.csform.android.uiapptemplate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.csform.android.uiapptemplate.fragment.HomeFragment;
import com.csform.android.uiapptemplate.fragment.ReqOffListFragment;
import com.csform.android.uiapptemplate.fragment.UserProfileFragment;
import com.csform.android.uiapptemplate.model.DrawerItem;
import com.csform.android.uiapptemplate.model.FavorModel;
import com.csform.android.uiapptemplate.model.UserModel;
import com.csform.android.uiapptemplate.util.FirebaseUtil;
import com.csform.android.uiapptemplate.util.ImageUtil;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity
		implements ReqOffListFragment.OnFragmentInteractionListener,
		UserProfileFragment.OnFragmentInteractionListener{

	private static final int REG_REQUEST = 1;
	private static final UserModel USER_DATA = new UserModel();
	private static String url;
	private static Firebase ref;
	private static Context context;
	private ListView mDrawerList;
	private List<DrawerItem> mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String LOG_TAG = "MainActivity";

	private Handler mHandler;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		url = FirebaseUtil.getUrl();
		ref = new Firebase(url);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mHandler = new Handler();
		context = this;

		Intent intent = getIntent();
		if(!intent.getExtras().containsKey("email")){
			errorKill();
		}
		USER_DATA.setField(context, "email", intent.getStringExtra("email"));
		ref.child("users").child(FirebaseUtil.emailToKey(UserModel.getField(context, "email")))
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if (!dataSnapshot.exists()) errorKill();
						Log.i(LOG_TAG, dataSnapshot.toString());
						Map<?, ?> userDataMap = (Map<?, ?>) dataSnapshot.getValue();
						if (userDataMap == null) {
							errorKill();
						}
						USER_DATA.setField(context, "name", userDataMap.get("name") + "");
						USER_DATA.setField(context, "phone", userDataMap.get("phone") + "");
						if (userDataMap.get("avatar") != null) {
							Log.i("Avatar not null", userDataMap.get("avatar").toString());
							USER_DATA.setField(context, "avatar", userDataMap.get("avatar").toString());
						} else USER_DATA.setField(context, "avatar", "");
						updateHeader();
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
/*
		if (savedInstanceState == null) {
			mDrawerLayout.openDrawer(mDrawerList);
		}
		*/
		commitFragment(new HomeFragment());
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
		boolean isFirstType = true;

        View headerView = prepareHeaderView(R.layout.header_navigation_drawer_1,
                "http://pengaja.com/uiapptemplate/avatars/0.jpg",
                full_name + "");

		BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems);

		mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
		mDrawerList.setAdapter(adapter);
	}

	private View prepareHeaderView(int layoutRes, String url, String email) {
		View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
		ImageView iv = (ImageView) headerView.findViewById(R.id.image);
		TextView tv = (TextView) headerView.findViewById(R.id.email);


		Log.i("Name:", UserModel.getField(this, "name"));
		Log.i("Image:", UserModel.getField(this, "avatar"));

		ImageUtil.displayImage(iv, UserModel.getField(this, "avatar"), null);
//		ImageUtil.displayRoundImage(iv, url, null);
		tv.setText(email);

		return headerView;
	}

	private void prepareNavigationDrawerItems() {
		mDrawerItems = new ArrayList<>();
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_home,
						R.string.drawer_title_home,
						DrawerItem.DRAWER_ITEM_TAG_HOME));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_browse_requests,
						R.string.drawer_title_requests,
						DrawerItem.DRAWER_ITEM_TAG_BROWSE_REQUESTS));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_browse_offers,
						R.string.drawer_title_offers,
						DrawerItem.DRAWER_ITEM_TAG_BROWSE_OFFERS));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_post_request,
						R.string.drawer_title_make_request,
						DrawerItem.DRAWER_ITEM_TAG_MAKE_REQUEST));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_post_offer,
						R.string.drawer_title_make_offer,
						DrawerItem.DRAWER_ITEM_TAG_MAKE_OFFER));
		mDrawerItems.add(
				new DrawerItem(
						R.string.drawer_icon_profile,
						R.string.drawer_title_profile,
						DrawerItem.DRAWER_ITEM_TAG_PROFILE));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
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

		switch (item.getItemId()) {
			case R.id.action_logout:
				USER_DATA.destroy();
				Intent myIntent = new Intent(MainActivity.this, LogInPageActivity.class);
				myIntent.putExtra("email", UserModel.getField(this, "email"));
				startActivity(myIntent);
				finish();
				break;
			case R.id.action_search:
				Intent intent = new Intent(MainActivity.this, ChatActivity.class);
				intent.putExtra("key", USER_DATA.getField(this,"name")); //Optional parameters
				MainActivity.this.startActivity(intent);
				break;



			default:
				return super.onOptionsItemSelected(item);

		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			selectItem(position, mDrawerItems.get(position - 1).getTag());
		}
	}

	private void selectItem(int position, int drawerTag) {
		Fragment fragment = getFragmentByDrawerTag(drawerTag);
		commitFragment(fragment);

		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItems.get(position - 1).getTitle());
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private Fragment getFragmentByDrawerTag(int drawerTag){
		Fragment fragment;
		if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_HOME) {
			fragment = HomeFragment.newInstance();
		}
		else if(drawerTag == DrawerItem.DRAWER_ITEM_TAG_BROWSE_REQUESTS){
			fragment = ReqOffListFragment.newInstance("requests");
		}
		else if(drawerTag == DrawerItem.DRAWER_ITEM_TAG_BROWSE_OFFERS){
			fragment = ReqOffListFragment.newInstance("offers");
		}
		else if(drawerTag == DrawerItem.DRAWER_ITEM_TAG_MAKE_REQUEST){
			fragment = FavorFormFragment.newInstance("requests");
		}
		else if(drawerTag == DrawerItem.DRAWER_ITEM_TAG_MAKE_OFFER){
			fragment = FavorFormFragment.newInstance("offers");
		}
		else if(drawerTag == DrawerItem.DRAWER_ITEM_TAG_PROFILE){
			fragment = UserProfileFragment.newInstance();

		}
		else{
			fragment = HomeFragment.newInstance();
		}
		return fragment;
	}

	private class CommitFragmentRunnable implements Runnable {

		private Fragment fragment;

		public CommitFragmentRunnable(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void run() {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment)
					.commit();
		}
	}
	public void commitFragment(Fragment fragment){
		mHandler.post(new CommitFragmentRunnable(fragment));
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

	// Listener from ReqOffListView
	@Override
	public void onFragmentInteraction(FavorModel fm) {
		Intent intent = new Intent(MainActivity.this, FavorSpecActivity.class);
		intent.putExtra("fm", fm);
		this.startActivity(intent);
	}

	// Listener from UserProfileFragment
	@Override
	public void onFragmentInteraction() {
		Intent myIntent = new Intent(MainActivity.this, UserProfileEditActivity.class);
		startActivity(myIntent);
//        startActivityForResult(myIntent, REG_REQUEST);
	}
	@Override
	// Gets result from RegistrationPageActivity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REG_REQUEST) errorKill(); // Check which request we're responding to
		if (resultCode != RESULT_OK) errorKill(); // Make sure the request was successful

		TextView emailview = (TextView)findViewById(R.id.emailview);
		emailview.setText(data.getStringExtra("email"));
		TextView passview = (TextView)findViewById(R.id.passview);
		passview.requestFocus();

	}

	private void errorKill(){
		Context context = getApplicationContext();
		CharSequence text = "Error retrieving user data";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		Intent myIntent = new Intent(this, LogInPageActivity.class);
		startActivity(myIntent);
		finish();
	}

	@Override
	protected void onDestroy() {
		USER_DATA.destroy();
		super.onDestroy();
	}
	private void updateHeader(){
		View V = this.findViewById(android.R.id.content);
		if(V == null) return;
		ImageView avatarView = (ImageView) V.findViewById(R.id.image);
		TextView nameView = (TextView) V.findViewById(R.id.name);
		TextView emailView = (TextView) V.findViewById(R.id.email);
		ImageUtil.displayImage(avatarView, UserModel.getField(this, "avatar"), null);
		nameView.setText(UserModel.getField(this, "name"));
		emailView.setText(UserModel.getField(this, "email"));
	}
}
