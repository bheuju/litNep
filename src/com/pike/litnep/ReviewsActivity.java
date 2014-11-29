package com.pike.litnep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pike.litnep.app.AppController;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewsActivity extends ActionBarActivity {

	//GeneralFunctions func = new GeneralFunctions(this);

	private String url = "http://pike.comlu.com/extra/read.php";
	private static String TAG = MainActivity.class.getSimpleName();

	private Button btnObjRequest, btnArrayRequest;
	// progress dialog
	private ProgressDialog pDialog;
	private TextView tvResponse;
	// temp string
	private String jsonResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);

		//func.toast("I am inside Reviews !!");
		//GeneralFunctions.getInstance().toast(getApplicationContext(), "Hello Reviews !");

		tvResponse = (TextView) findViewById(R.id.tvResponse);
		btnObjRequest = (Button) findViewById(R.id.btnObjRequest);
		btnArrayRequest = (Button) findViewById(R.id.btnArrRequest);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		btnObjRequest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jsonObjRequest();
			}
		});

		btnArrayRequest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jsonArrRequest();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reviews, menu);
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
		showpDialog();
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

							jsonResponse = "";
							jsonResponse += "SN: " + sn + "\n\n";
							jsonResponse += "Title: " + title + "\n\n";
							jsonResponse += content + "\n\n";
							tvResponse.setText(jsonResponse);
						} catch (JSONException e) {
							e.printStackTrace();
							//GeneralFunctions.getInstance().toast(context, "Error: " + e.getMessage());
						}
						hidepDialog();
					};
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						//GeneralFunctions.getInstance().toast(context, error.getMessage());
						hidepDialog();
					}

				});
		// add request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	}

	private void jsonArrRequest() {
		// starts with [
		showpDialog();
		JsonArrayRequest req = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						try {
							// parsing json array response
							// loop through each json object
							jsonResponse = "";
							for (int i = 0; i < response.length(); i++) {
								JSONObject post = (JSONObject) response.get(i);

								String sn = post.getString("sn");
								String title = post.getString("title");
								String content = post.getString("content");

								jsonResponse += "SN: " + sn + "\n\n";
								jsonResponse += "Title: " + title + "\n\n";
								jsonResponse += content + "\n\n\n";
							}
							tvResponse.setText(jsonResponse);
						} catch (JSONException e) {
							e.printStackTrace();
							//GeneralFunctions.getInstance().toast(context, "Error: " + e.getMessage());
						}
						hidepDialog();
					};
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						//GeneralFunctions.getInstance().toast(context, error.getMessage());
						hidepDialog();
					}

				});
		//add request to request queue
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
