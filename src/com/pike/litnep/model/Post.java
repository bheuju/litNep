package com.pike.litnep.model;

public class Post {
	private String userName;
	private String title, content;
	private String date, time;

	private String thumbnailUrl;

	public Post() {
	}

	public Post(String userName, String title, String content) {
		this.userName = userName;
		this.title = title;
		this.content = content;
	}

	/**
	 * Setters
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getContent() {
		return content;
	}

}
