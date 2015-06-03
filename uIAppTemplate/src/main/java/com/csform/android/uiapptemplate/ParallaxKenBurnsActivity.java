package com.csform.android.uiapptemplate;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.adapter.DefaultAdapter;
import com.csform.android.uiapptemplate.model.DummyModel;
import com.csform.android.uiapptemplate.view.AlphaForegroundColorSpan;
import com.csform.android.uiapptemplate.view.ChatActivity;
import com.csform.android.uiapptemplate.view.kbv.KenBurnsView;

import java.util.ArrayList;

class messaging_list{
public static ArrayList<DummyModel> message_list() {
        ArrayList<DummyModel> list = new ArrayList<>();

        list.add(new DummyModel(0, "http://pengaja.com/uiapptemplate/avatars/0.jpg", "Isaac Reid", R.string.fontello_heart_empty));
        list.add(new DummyModel(1, "http://pengaja.com/uiapptemplate/avatars/1.jpg", "Jason Graham", R.string.fontello_heart_empty));
        list.add(new DummyModel(2, "http://pengaja.com/uiapptemplate/avatars/2.jpg", "Abigail Ross", R.string.fontello_heart_empty));
        list.add(new DummyModel(3, "http://pengaja.com/uiapptemplate/avatars/3.jpg", "Justin Rutherford", R.string.fontello_heart_empty));
        list.add(new DummyModel(4, "http://pengaja.com/uiapptemplate/avatars/4.jpg", "Nicholas Henderson", R.string.fontello_heart_empty));
        list.add(new DummyModel(5, "http://pengaja.com/uiapptemplate/avatars/5.jpg", "Elizabeth Mackenzie", R.string.fontello_heart_empty));
        list.add(new DummyModel(6, "http://pengaja.com/uiapptemplate/avatars/6.jpg", "Melanie Ferguson", R.string.fontello_heart_empty));
        list.add(new DummyModel(7, "http://pengaja.com/uiapptemplate/avatars/7.jpg", "Fiona Kelly", R.string.fontello_heart_empty));
        list.add(new DummyModel(8, "http://pengaja.com/uiapptemplate/avatars/8.jpg", "Nicholas King", R.string.fontello_heart_empty));
        list.add(new DummyModel(9, "http://pengaja.com/uiapptemplate/avatars/9.jpg", "Victoria Mitchell", R.string.fontello_heart_empty));
        list.add(new DummyModel(10, "http://pengaja.com/uiapptemplate/avatars/10.jpg", "Sophie Lyman", R.string.fontello_heart_empty));
        list.add(new DummyModel(11, "http://pengaja.com/uiapptemplate/avatars/11.jpg", "Carl Ince", R.string.fontello_heart_empty));
        list.add(new DummyModel(12, "http://pengaja.com/uiapptemplate/avatars/12.jpg", "Michelle Slater", R.string.fontello_heart_empty));
        list.add(new DummyModel(13, "http://pengaja.com/uiapptemplate/avatars/13.jpg", "Ryan Mathis", R.string.fontello_heart_empty));
        list.add(new DummyModel(14, "http://pengaja.com/uiapptemplate/avatars/14.jpg", "Julia Grant", R.string.fontello_heart_empty));
        list.add(new DummyModel(15, "http://pengaja.com/uiapptemplate/avatars/15.jpg", "Hannah Martin", R.string.fontello_heart_empty));

        return list;
   }
}


/**
 * Please NOTE: In manifest, set theme for this class to
 * \@style/OverlayActionBar
 * @author MLADJO
 *
 */
public class ParallaxKenBurnsActivity extends Activity {
	
	public static final String TAG = "Parallax with Ken Burns effect";
	
	private int mActionBarTitleColor;
	private int mActionBarHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ListView mListView;
	private KenBurnsView mHeaderPicture;
	private ImageView mHeaderLogo;
	private View mHeader;
	private View mPlaceHolderView;
	private AccelerateDecelerateInterpolator mSmoothInterpolator;
	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;
	private TypedValue mTypedValue = new TypedValue();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSmoothInterpolator = new AccelerateDecelerateInterpolator();
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.ken_burns_header);
		mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();
		setContentView(R.layout.activity_parallax_ken_burns);
		mListView = (ListView) findViewById(R.id.list_view);
		mHeader = findViewById(R.id.header);
		mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
		
		mHeaderPicture.setImageResource(R.drawable.splash_screen_background);
		
		mHeaderPicture.setScaleType(ScaleType.CENTER_CROP);
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		mActionBarTitleColor = Color.WHITE;
		mSpannableString = new SpannableString(
				getString(R.string.app_name));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(
				mActionBarTitleColor);
		setupActionBar();
		setupListView();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupListView() {
		mPlaceHolderView = getLayoutInflater().inflate(
				R.layout.header_fake, mListView, false);
		mListView.addHeaderView(mPlaceHolderView, null, false);
		
		//TODO You'd probably want to set your own adapter
		// >>>>>>>>>>>>>>>
		mListView.setAdapter(new DefaultAdapter(this, messaging_list.message_list(), false));
		//<<<<<<<<<<<<<<<<
		
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				mHeader.setTranslationY(Math.max(-scrollY,
						mMinHeaderTranslation));
				float ratio = clamp(mHeader.getTranslationY()
						/ mMinHeaderTranslation, 0.0f, 1.0f);
				interpolate(mHeaderLogo, getActionBarIconView(),
						mSmoothInterpolator.getInterpolation(ratio));
				setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(ParallaxKenBurnsActivity.this,
						mListView.getAdapter().getItem(position).toString(),
						Toast.LENGTH_SHORT).show();
                String value = mListView.getAdapter().getItem(position).toString();
                Intent myIntent = new Intent(ParallaxKenBurnsActivity.this, ChatActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                ParallaxKenBurnsActivity.this.startActivity(myIntent);
			}
		});
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActionBar().setTitle(mSpannableString);
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private void interpolate(View view1, View view2, float interpolation) {
		setOnScreenRect(mRect1, view1);
		setOnScreenRect(mRect2, view2);
		float scaleX = 1.0F + interpolation
				* (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation
				* (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left
				+ mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top
				+ mRect2.bottom - mRect1.top - mRect1.bottom));
		view1.setTranslationX(translationX);
		view1.setTranslationY(translationY - mHeader.getTranslationY());
		view1.setScaleX(scaleX);
		view1.setScaleY(scaleY);
	}

	private void setOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
	}

	public int getScrollY() {
		View c = mListView.getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = mListView.getFirstVisiblePosition();
		int top = c.getTop();
		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}
		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_transparent);
	}

	private ImageView getActionBarIconView() {
		return (ImageView) findViewById(android.R.id.home);
	}

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
				true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}
}
