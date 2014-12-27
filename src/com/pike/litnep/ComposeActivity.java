package com.pike.litnep;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeActivity extends ActionBarActivity {

	private String tag;

	private EditText etTitle, etContent;
	private Button btnSave, btnPost;
	private TextView tvTitle;

	private String url = "http://pike.comlu.com/extra/compose.php";
	private String urlEdit = "http://pike.comlu.com/extra/update.php";
	private ProgressDialog pDialog;

	private int userId;
	private int postSn;
	private String title = "";
	private String content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		etTitle = (EditText) findViewById(R.id.etTitle);
		etContent = (EditText) findViewById(R.id.etContent);
		btnPost = (Button) findViewById(R.id.btnPost);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// userId always needed
			userId = extras.getInt("userId");
			if ((tag = extras.getString("tag")).equals("edit")) {
				// This is edit
				getSupportActionBar().setTitle("Edit");
				tvTitle.setText("Edit");
				postSn = extras.getInt("sn");
				title = extras.getString("title");
				content = extras.getString("content");
				etTitle.setText(title);
				etContent.setText(content);
			} else if ((tag = extras.getString("tag")).equals("compose")) {
				// This is compose
				getSupportActionBar().setTitle("Compose");
				tvTitle.setText("Compose");
			}
			Log.d("GOT", "GOT");
			Log.d("sn", "" + postSn);
			Log.d("userid", "" + userId);
			Log.d("title", title);
			Log.d("content", content);
		}

		btnPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// get title and contents
				title = etTitle.getText().toString();
				content = etContent.getText().toString();
				if (validateInput()) {
					if (tag.equals("edit")) {
						editContents();
					} else if (tag.equals("compose")) {
						postContents();
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
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
	 * General Functions
	 */

	public boolean validateInput() {
		// check empty fields
		// TODO: check email validation (is valid) or //leave it for now
		if (!title.equals("") && !content.equals("")) {
			return true;
		} else {
			GeneralFunctions.getInstance().toast(getApplicationContext(),
					"Empty fields!");
		}
		return false;
	}

	public void postContents() {
		showpDialog();
		StringRequest req = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("Response: ", response.toString());

						GeneralFunctions.getInstance().toast(
								getApplicationContext(), "Success");

						// goto mainActvity
						// Intent mainActivity = new
						// Intent(getApplicationContext(), MainActivity.class);
						// close all views before launching HOME
						// mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// startActivity(mainActivity);
						hidepDialog();
						// close signinActivity
						finish();
						// MainActivity main = new MainActivity();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// GeneralFunctions.getInstance().toast(getApplicationContext(),
						// error.toString());
						if (error instanceof NoConnectionError) {
							GeneralFunctions.getInstance().toast(
									getApplicationContext(),
									"Connection Error !");
						}
						hidepDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", String.valueOf(userId));
				params.put("title", title);
				params.put("content", content);
				return params;
			}
		};
		// add to request queue
		AppController.getInstance().addToRequestQueue(req);
	}

	public void editContents() {
		showpDialog();
		StringRequest req = new StringRequest(Method.POST, urlEdit,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("Response: ", response.toString());

						GeneralFunctions.getInstance().toast(
								getApplicationContext(), "Success");

						hidepDialog();
						finish();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// GeneralFunctions.getInstance().toast(getApplicationContext(),
						// error.toString());
						if (error instanceof NoConnectionError) {
							GeneralFunctions.getInstance().toast(
									getApplicationContext(),
									"Connection Error !");
						}
						hidepDialog();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				Log.d("PARAMS", "PARAMS");
				Log.d("sn", "" + postSn);
				Log.d("userid", "" + userId);
				Log.d("title", title);
				Log.d("content", content);
				params.put("sn", String.valueOf(postSn));
				params.put("user_id", String.valueOf(userId));
				params.put("title", title);
				params.put("content", content);
				return params;
			}
		};
		// add to request queue
		AppController.getInstance().addToRequestQueue(req);
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
