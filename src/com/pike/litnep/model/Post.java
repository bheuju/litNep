package com.pike.litnep.model;

public class Post {
	private int post_id;
	private int userId;
	private String firstName;
	private String lastName;
	private String title, content;
	private String created_at;
	private int likeValue;

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
	public void setPostId(int post_id) {
		this.post_id = post_id;
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

	public void setLikeValue(int likeValue) {
		this.likeValue = likeValue;
	}

	/**
	 * Getters
	 */
	public int getPostId() {
		return post_id;
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

	public int getLikeValue() {
		return likeValue;
	}
}
