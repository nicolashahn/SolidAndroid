package com.csform.android.uiapptemplate.model;

public class FavorModel {

	private String mKey;
	private String mImageURL;
	private String mTitle;
	private String mDesc;

	public FavorModel() {
	}

	public FavorModel(String key, String imageURL, String title, String desc) {
		mKey = key;
		mImageURL = imageURL;
		mTitle = title;
		mDesc = desc;
	}

	public String getKey() {
		return mKey;
	}

	public void setKey(String key) {
		mKey = key;
	}

	public String getImageURL() {
		return mImageURL;
	}

	public void setImageURL(String imageURL) {
		mImageURL = imageURL;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDesc() {
		return mDesc;
	}

	public void setDesc(String desc) {
		mDesc = desc;
	}

	@Override
	public String toString() {
		return mTitle;
	}

	// no purpose other than to satisfy the abstract method Adapter
	public long getId() {
		return 0;
	}
}
