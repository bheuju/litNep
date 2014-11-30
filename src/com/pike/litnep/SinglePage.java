package com.pike.litnep;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SinglePage extends ActionBarActivity {

	private TextView tvTitle, tvContent, tvCreatedAt;

	private static String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_page);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvContent = (TextView) findViewById(R.id.tvContent);
		tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);

		Bundle extras = getIntent().getExtras();

		String firstName = extras.getString("firstName");
		String lastName = extras.getString("lastName");
		String title = extras.getString("title");
		String content = extras.getString("content");
		String created_at = extras.getString("created_at");

		// parsing date to suitable formats
		created_at = GeneralFunctions.getInstance().dateParser(created_at, "MMM dd, yyyy");

		getSupportActionBar().setTitle(title);

		tvTitle.setText(title);
		tvCreatedAt.setText(created_at);
		tvContent.setText(content);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_page, menu);
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
	 * Methods to make json request
	 */
	private void jsonObjRequest() {
		// starts with {
		String url = "http://pike.comlu.com";
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
						try {
							// parsing json obj response
							// response will be a json object
							String sn = response.getString("sn");
							String title = response.getString("title");
							String content = response.getString("content");

						} catch (JSONException e) {
							e.printStackTrace();
							// GeneralFunctions.getInstance().toast(context,
							// "Error: " + e.getMessage());
						}
					};
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(context,
						// error.getMessage());
					}

				});
		// add request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}
}
