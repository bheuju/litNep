package com.pike.litnep;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pike.litnep.app.AppController;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NotificationReceiver extends ActionBarActivity {

	private static String TAG = MainActivity.class.getSimpleName();

	private String url = "http://pike.comlu.com/notification/info";

	private TextView tvNotificationTitle, tvNotificationContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_receiver);

		tvNotificationTitle = (TextView) findViewById(R.id.tvNotificationTitle);
		tvNotificationContent = (TextView) findViewById(R.id.tvNotificationContent);

		getNotificationInfo();

	}

	private void getNotificationInfo() {
		// starts with {
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						//Log.d(TAG, response.toString());
						try {
							// parsing json obj response
							// response will be a json object
							String title = response.getString("title");
							String content = response.getString("content");

							tvNotificationTitle.setText(title);
							tvNotificationContent.setText(content);

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
						//VolleyLog.d(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(context,
						// error.getMessage());
					}

				});
		// add request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.notification_receiver, menu);
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
}
