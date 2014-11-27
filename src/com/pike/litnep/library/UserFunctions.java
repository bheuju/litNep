package com.pike.litnep.library;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class UserFunctions {
	/**
	 * Handles user's functions
	 * @category register
	 * @category login
	 * @category logout 
	 */
	private static String login_tag = "login";
	private static String register_tag = "register";

	public UserFunctions() {
	}

	/**
	 * Function to make login request
	 * 
	 * @param email
	 * @param password
	 */
	public Map<String, String> loginUser(String email, String password) {
		// building parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("tag", login_tag);
		params.put("email", email);
		params.put("password", password);
		return params;
	}

	/**
	 * Function to make registration request
	 * 
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Map<String, String> registerUser(String firstName, String lastName,
			String email, String password) {
		// building parameters
		Map<String, String> params = new HashMap<String, String>();
		params.put("tag", register_tag);
		params.put("firstName", firstName);
		params.put("lastName", lastName);
		params.put("email", email);
		params.put("password", password);
		return params;
	}

	/**
	 * Function to get login status
	 */
	public boolean isUserLoggedIn(Context context) {

		return false;
	}

	/**
	 * Function to logout user
	 */
	public boolean logoutUser(Context context) {

		return true;
	}
}
