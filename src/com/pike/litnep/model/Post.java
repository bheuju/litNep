package com.pike.litnep.model;

import android.util.Log;

public class Post {
	private String userName;
	private String title, contents;
	private String date, time;

	private String thumbnailUrl;

	public Post() {
	}

	public Post(String userName, String title, String contents) {
		this.userName = userName;
		this.title = title;
		this.contents = contents;
	}

	/**
	 * Setters
	 */
	public void setUserName(String userName) {
		this.userName = userName;
		Log.d("Receiving:", userName);
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * Getters
	 */
	public String getUserName() {
		return userName;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

}
