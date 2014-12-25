package com.pike.litnep.model;

public class Post {
	private int sn;
	private int userId;
	private String firstName;
	private String lastName;
	private String title, content;
	private String created_at;

	private String thumbnailUrl;

	public Post() {
	}

	public Post(String firstName, String lastName, String thumbnailUrl,
			String title, String content) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.thumbnailUrl = thumbnailUrl;
		this.title = title;
		this.content = content;
	}

	/**
	 * Setters
	 */
	public void setSn(int sn) {
		this.sn = sn;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setlastName(String lastName) {
		this.lastName = lastName;
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

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	/**
	 * Getters
	 */
	public int getSn() {
		return sn;
	}
	
	public int getUserId() {
		return userId;
	}

	public String getfirstName() {
		return firstName;
	}

	public String getlastName() {
		return lastName;
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

	public String getCreatedAt() {
		return created_at;
	}
}
