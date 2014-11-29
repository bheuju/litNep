package com.pike.litnep;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.library.UserFunctions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SigninActivity extends ActionBarActivity {

	private Button btnSignUp, btnSignIn;
	private EditText etEmail, etPass;
	private TextView tvLoginError;

	private String email, password;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_ID = "id";
	private static String KEY_FIRSTNAME = "firstName";
	private static String KEY_LASTNAME = "lastName";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	private ProgressDialog pDialog;
	private JSONObject jsonObj;

	private static String url = "http://pike.comlu.com/users/";
	
	private static String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Logging In...");
		pDialog.setCancelable(false);

		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);

		etEmail = (EditText) findViewById(R.id.etEmail);
		etPass = (EditText) findViewById(R.id.etPass);

		tvLoginError = (TextView) findViewById(R.id.tvLoginError);

		btnSignIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				email = etEmail.getText().toString();
				password = etPass.getText().toString();

				Log.d("Log In", email);
				Log.d("Log In", password);

				if (validateInput()) {
					// try to login
					UserFunctions userFunction = new UserFunctions();
					Map<String, String> params = userFunction.loginUser(email,
							password);
					getJSONFromUrl(url, params);
				}
			}
		});

		// Listener for SignupActivity
		btnSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent ();
				startActivity(new Intent(getApplicationContext(),
						SignupActivity.class));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @return
	 */
	public boolean validateInput() {
		// check empty fields
		// TODO: check email validation (is valid) or //leave it for now
		if (!email.equals("") && !password.equals("")) {
			return true;
		} else {
			GeneralFunctions.getInstance().toast(getApplicationContext(),
					"Empty fields!");
		}
		return false;
	}

	/**
	 * 
	 * @param url
	 * @param param
	 */
	public void getJSONFromUrl(String url, final Map<String, String> param) {
		// starts with {
		showpDialog();
		StringRequest req = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("JSON Parser: ", response.toString());
						String jsonString = response.toString();

						try {
							jsonObj = new JSONObject(jsonString);
							Log.d("JSON =>", jsonObj.toString());

						} catch (Throwable t) {
							Log.e("JSON =>",
									"Could not parse malformed JSON: \""
											+ jsonString + "\"");
						}
						hidepDialog();
						// call callback
						checkLoginResponse(jsonObj);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
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

	public void checkLoginResponse(JSONObject json) {
		// check login response
		Log.d("Response After sign in: ", json.toString());
		try {
			if (json.getString(KEY_SUCCESS) != null) {
				tvLoginError.setText("");
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
					mainActivity.putExtra("firstName", json.getString(KEY_FIRSTNAME));
					mainActivity.putExtra("lastName", json.getString(KEY_LASTNAME));
					mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(mainActivity);
					// close signinActivity
					finish();
				} else {
					// Error in login
					tvLoginError.setText(json.getString(KEY_ERROR_MSG));
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
