package com.pike.litnep.library;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.AppController;
import com.pike.litnep.MainActivity;

public class JSONParser {

	private static String TAG = MainActivity.class.getSimpleName();

	private String jsonString;
	private JSONObject jsonObj;

	private Context context;
	private String DialogMsg;

	private static ProgressDialog pDialog;

	public JSONParser(Context context, String DialogMsg) {
		this.context = context;
		this.DialogMsg = DialogMsg;
	}

	public void initProgressDialog() {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage(DialogMsg);
		pDialog.setCancelable(false);
	}

	public JSONObject getJSONFromUrl(String url,
			final Map<String, String> param, final boolean showProgress) {
		// starts with {
		initProgressDialog();
		if (showProgress) {
			showpDialog();
		}
		StringRequest req = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("JSON Parser: ", response.toString());
						jsonString = response.toString();

						try {
							jsonObj = new JSONObject(jsonString);
							Log.d("JSON =>", jsonObj.toString());
						} catch (Throwable t) {
							Log.e("JSON =>",
									"Could not parse malformed JSON: \""
											+ jsonString + "\"");
						}
						if (showProgress) {
							hidepDialog();
						}
						// call callback
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(getApplicationContext(),
						// error.toString());
						if (showProgress) {
							hidepDialog();
						}
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
		return jsonObj;
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
