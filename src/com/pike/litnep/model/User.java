package com.pike.litnep.model;

public class User {

	private boolean loggedStatus;

	private int userId;

	private String imgUrl;
	private String thumbnailUrl;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;

	private static User instance;

	public static synchronized User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

	private User() {
		this.loggedStatus = false;
		this.imgUrl = "";
		this.thumbnailUrl = "";
		this.userId = 0;
		this.firstName = "";
		this.lastName = "";
		this.phone = "";
		this.address = "";
	}

	public void setUser(int userId, String firstName, String lastName,
			String email, String phone, String address, String imgUrl, String thumbnailUrl) {
		this.loggedStatus = true;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.imgUrl = imgUrl;
		this.thumbnailUrl = thumbnailUrl;
	}

	/**
	 * Setters
	 */
	public void setLoggedStatus(boolean loggedStatus) {
		this.loggedStatus = loggedStatus;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Getters
	 */
	public boolean getLoggedStatus() {
		return loggedStatus;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public int getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}
}
