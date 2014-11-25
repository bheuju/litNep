package com.pike.litnep;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FragmentTab1 extends Fragment {

	private String url = "http://pike.comlu.com/read.php";
	private static String TAG = MainActivity.class.getSimpleName();
	private Button btnArrayRequest;
	// progress dialog
	private ProgressDialog pDialog;
	private ListView list;
	ArrayList<String> mContentsList;
	ArrayAdapter<String> dataAdapter;
	// temp string
	private String jsonResponse;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// called before onCreateView
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment1, container, false);

		// GeneralFunctions.getInstance().toast(getActivity(),"Hello Fragment!");

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		btnArrayRequest = (Button) v.findViewById(R.id.btnArrRequest);
		list = (ListView) v.findViewById(R.id.listPosts);
		
		//create array adapter
		mContentsList = new ArrayList<String>();
		dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_contents_main, mContentsList);
		list.setAdapter(dataAdapter);
		
		btnArrayRequest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				jsonArrRequest();
			}
		});

		return v;
	}

	/**
	 * Methods to make JSON array request
	 */

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
							for (int i = 0; i < response.length(); i++) {
								JSONObject post = (JSONObject) response.get(i);

								String sn = post.getString("sn");
								String title = post.getString("title");
								String content = post.getString("content");

								jsonResponse = "";
								jsonResponse += title + "\n\n";
								jsonResponse += content;
								
								mContentsList.add(jsonResponse);
								dataAdapter.notifyDataSetChanged();
							}
							// tvResponse.setText(jsonResponse);
						} catch (JSONException e) {
							e.printStackTrace();
							// GeneralFunctions.getInstance().toast(context,
							// "Error: " + e.getMessage());
						}
						hidepDialog();
					};
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(context,
						// error.getMessage());
						hidepDialog();
					}

				});
		// add request to request queue
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
