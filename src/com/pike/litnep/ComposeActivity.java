package com.pike.litnep;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

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

public class ComposeActivity extends ActionBarActivity {

	private EditText etTitle, etContent;
	private Button btnSave, btnPost;

	private String url = "http://pike.comlu.com/extra/compose.php";
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		etTitle = (EditText) findViewById(R.id.etTitle);
		etContent = (EditText) findViewById(R.id.etContent);
		btnPost = (Button) findViewById(R.id.btnPost);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		btnPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showpDialog();
				StringRequest req = new StringRequest(Method.POST, url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								Log.d("Response: ", response.toString());

								GeneralFunctions.getInstance().toast(
										getApplicationContext(), "Success");

								// goto mainActvity
								Intent mainActivity = new Intent(
										getApplicationContext(),
										MainActivity.class);
								// close all views before launching HOME
								mainActivity
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(mainActivity);
								// close signinActivity
								finish();

							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								GeneralFunctions.getInstance().toast(
										getApplicationContext(),
										error.toString());
							}
						}) {
					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("title", etTitle.getText().toString());
						params.put("content", etContent.getText().toString());
						return params;
					}
				};
				// add to request queue
				AppController.getInstance().addToRequestQueue(req);
				hidepDialog();
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

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
