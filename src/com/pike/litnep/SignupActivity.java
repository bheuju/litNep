package com.pike.litnep;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.library.UserFunctions;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends ActionBarActivity {

	private Button btnSignUp;
	private Button btnSignIn;
	private TextView tvRegisterError;
	private EditText etFirstName, etLastName, etEmail, etPass, etPassRe;
	private String firstName, lastName, email, password, repassword;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_ID = "id";
	private static String KEY_FIRSTNAME = "firstName";
	private static String KEY_LASTNAME = "lastName";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	private static String url = "http://pike.comlu.com/users/";

	private ProgressDialog pDialog;
	private JSONObject jsonObj;

	private static String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Registering...");
		pDialog.setCancelable(false);

		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		tvRegisterError = (TextView) findViewById(R.id.tvRegisterError);

		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etLastName = (EditText) findViewById(R.id.etLastName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPass = (EditText) findViewById(R.id.etPass);
		etPassRe = (EditText) findViewById(R.id.etPassRe);

		btnSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				firstName = etFirstName.getText().toString();
				lastName = etLastName.getText().toString();
				email = etEmail.getText().toString();
				password = etPass.getText().toString();
				repassword = etPassRe.getText().toString();

				//Log.d("Sign Up", firstName);
				//Log.d("Sign Up", lastName);
				//Log.d("Sign Up", email);
				//Log.d("Sign Up", password);

				// check validation of input fields
				if (validateInput()) {
					// try to sign up
					UserFunctions userFunction = new UserFunctions();
					Map<String, String> params = userFunction.registerUser(
							firstName, lastName, email, password);
					getJSONFromUrl(url, params);
				}

			}
		});

		btnSignIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						SigninActivity.class));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		startActivity(new Intent(this, Prefs.class));
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateInput() {
		// check empty fields
		// TODO: check email validation (is valid) or //leave it for now
		if (!email.equals("") && !password.equals("")) {
			// password validation
			if (password.equals(repassword)) {
				return true;
			} else {
				GeneralFunctions.getInstance().toast(getApplicationContext(),
						"Passwords do not match!");
			}
		} else {
			GeneralFunctions.getInstance().toast(getApplicationContext(),
					"Empty fields!");
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 * @param DialogMsg
	 */

	public void getJSONFromUrl(String url, final Map<String, String> param) {
		// starts with {
		// initProgressDialog();
		showpDialog();
		StringRequest req = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.d("JSON Parser: ", response.toString());
						String jsonString = response.toString();

						try {
							jsonObj = new JSONObject(jsonString);
							//Log.d("JSON =>", jsonObj.toString());

						} catch (Throwable t) {
							//Log.e("JSON =>",
							//		"Could not parse malformed JSON: \""
							//				+ jsonString + "\"");
						}
						hidepDialog();
						// call callback
						checkRegisterResponse(jsonObj);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//VolleyLog.d(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(getApplicationContext(),
						// error.toString());
						hidepDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				// params.put("tag", param.get("tag"));
				params.putAll(param);
				return params;
			}
		};
		// add request to request queue
		AppController.getInstance().addToRequestQueue(req);
	}

	public void checkRegisterResponse(JSONObject json) {
		// check login response
		//Log.d("Response After sign in: ", json.toString());
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				tvRegisterError.setText("");
				String res = json.getString(KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					// user successfully logged in
					// TODO: store user details in local database

					// show successful message
					GeneralFunctions.getInstance().toast(
							getApplicationContext(), "Log In, Successful");
					Intent mainActivity = new Intent(getApplicationContext(),
							MainActivity.class);
					mainActivity.putExtra("login", true);
					mainActivity.putExtra("userId", json.getInt(KEY_ID));
					mainActivity.putExtra("firstName",
							json.getString(KEY_FIRSTNAME));
					mainActivity.putExtra("lastName",
							json.getString(KEY_LASTNAME));
					mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(mainActivity);
					// close signinActivity
					finish();
				} else {
					// Error in login
					tvRegisterError.setText(json.getString(KEY_ERROR_MSG));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

}
