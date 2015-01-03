package com.pike.litnep;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SinglePage extends ActionBarActivity {

	private TextView tvTitle, tvContent, tvCreatedAt;
	private TextView tvLikeValue;
	private ImageButton btnLike;

	private String url = "http://pike.comlu.com/extra/incLike.php";

	private int postId;
	private int likeValue = 0;

	// private static boolean liked = false;

	private static String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_page);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvContent = (TextView) findViewById(R.id.tvContent);
		tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);

		tvLikeValue = (TextView) findViewById(R.id.tvLikeValue);
		btnLike = (ImageButton) findViewById(R.id.btnLike);

		Bundle extras = getIntent().getExtras();

		String firstName = extras.getString("firstName");
		String lastName = extras.getString("lastName");
		String title = extras.getString("title");
		String content = extras.getString("content");
		String created_at = extras.getString("created_at");

		postId = extras.getInt("post_id");
		likeValue = extras.getInt("like_value");

		// parsing date to suitable formats
		created_at = GeneralFunctions.getInstance().dateParser(created_at,
				"MMM dd, yyyy");

		getSupportActionBar().setTitle(title);

		tvTitle.setText(title);
		tvCreatedAt.setText(created_at);
		tvContent.setText(content);

		tvLikeValue.setText((likeValue != 0) ? String.valueOf(likeValue) : "");

		btnLike.setEnabled(MainActivity.isLoggedIn);

		btnLike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnLike.setEnabled(false);
				incLike();
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
	 * Methods to make json post request
	 */
	public void incLike() {
		StringRequest req = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.d("Response: ", response.toString());

						GeneralFunctions.getInstance().toast(
								getApplicationContext(), "Success");
						likeValue++;
						tvLikeValue.setText(String.valueOf(likeValue));
						// liked = true;
						btnLike.setEnabled(false);
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
						btnLike.setEnabled(true);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("post_id", String.valueOf(postId));
				params.put("like_value", String.valueOf(likeValue));
				//Log.e("DATA", "postid: " + postId + " likevalue:" + likeValue);
				return params;
			}
		};
		// add to request queue
		AppController.getInstance().addToRequestQueue(req);
	}
}
