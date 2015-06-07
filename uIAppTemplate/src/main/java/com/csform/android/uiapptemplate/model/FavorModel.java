package com.csform.android.uiapptemplate.model;

import java.io.Serializable;

// Serializable allows FavorModel to be passed through Intent
public class FavorModel implements Serializable {

	private String mKey;
	private String mUser;
	private String mUserImage;
	private String mTitle;
	private String mDesc;
	private String mDatePosted;
	private String mDateDoneBy;
	private String mCompensation;

	public FavorModel() {
		mKey = "";
		mUser = "";
		mUserImage = "";
		mTitle = "";
		mDesc = "";
		mDateDoneBy="";
		mDatePosted="";
		mCompensation="";

	}

	public FavorModel(String key, String user, String userImage, String title, String desc, String dateDoneBy, String datePosted, String compensation) {
		mKey = key;
		mUser = user;
		mUserImage = userImage;
		mTitle = title;
		mDesc = desc;
		mDateDoneBy=dateDoneBy;
		mDatePosted=datePosted;
		mCompensation=compensation;

	}

	public String getKey() {
		return mKey;
	}

	public void setKey(String key) {
		mKey = key;
	}

	public String getUserImage() {
		return mUserImage;
	}

	public void setUserImage(String userImage) {
		mUserImage = userImage;
	}

	public String getUser() {
		return mUser;
	}

	public void setUser(String user) {
		mUser = user;
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

	public String getDatePosted(){return mDatePosted;}

	public void setDatePosted(String datePosted){mDatePosted=datePosted;}

	public String getDateDoneBy(){return mDateDoneBy;}

	public void setDateDoneBy(String dateDoneBy){mDateDoneBy=dateDoneBy;}

	public String getCompensation(){return mCompensation;}

	public void setCompensation(String compensation){mCompensation=compensation;}

	@Override
	public String toString() {
		return mTitle;
	}

	// no purpose other than to satisfy the abstract method Adapter
	public long getId() {
		return 0;
	}

}
