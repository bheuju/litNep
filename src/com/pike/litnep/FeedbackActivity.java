package com.pike.litnep;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends ActionBarActivity {

	private String url = "http://pike.comlu.com/extra/feedback.php";

	private EditText etSubject, etMessage;
	private Button btnSend;

	private String subject, message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		etSubject = (EditText) findViewById(R.id.etSubject);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btnSend = (Button) findViewById(R.id.btnSend);

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnSend.setEnabled(false);
				subject = etSubject.getText().toString();
				message = etMessage.getText().toString();
				if (validateInput()) {
					sendMessage();
				}

			}
		});

	}

	public boolean validateInput() {
		// check empty fields
		// TODO: check email validation (is valid) or //leave it for now
		if (!subject.equals("") && !message.equals("")) {
			return true;
		} else {
			GeneralFunctions.getInstance().toast(getApplicationContext(),
					"Empty fields!");
		}
		return false;
	}

	public void sendMessage() {
		StringRequest req = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("Response: ", response.toString());

						GeneralFunctions.getInstance().toast(
								getApplicationContext(),
								"Message queued for delivery !");
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
						btnSend.setEnabled(true);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("sub", subject);
				params.put("msg", message);
				return params;
			}
		};
		// add to request queue
		AppController.getInstance().addToRequestQueue(req);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
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
}
