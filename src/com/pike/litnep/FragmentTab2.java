package com.pike.litnep;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pike.litnep.adapter.CustomListAdapter;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Post;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentTab2 extends Fragment {

	private ListView list;
	private ArrayList<Post> mContentsList = new ArrayList<Post>();
	private CustomListAdapter dataAdapter;

	private String url = "http://pike.comlu.com/extra/read.php";
	private static String TAG = MainActivity.class.getSimpleName();
	// progress dialog
	private ProgressDialog pDialog;
	// temp string
	private String jsonResponse;

	private boolean dataReceived = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment2, container, false);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please wait... \nLoading contents");
		pDialog.setCancelable(true);

		list = (ListView) v.findViewById(R.id.listPosts);
		dataAdapter = new CustomListAdapter(getActivity(), mContentsList);
		list.setAdapter(dataAdapter);

		jsonArrRequest();

		// TODO: load data from local database
		// TODO: and add to mContentsList

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
								JSONObject obj = (JSONObject) response.get(i);
								
								Post post = new Post();
								post.setUserName(obj.getString("sn"));
								post.setTitle(obj.getString("title"));
								post.setContents(obj.getString("content"));
								
								Log.d("Received: ", post.getUserName());
								Log.d("Received: ", post.getTitle());
								Log.d("Received: ", post.getContents());
								
								mContentsList.add(0, post);
							}
							// tvResponse.setText(jsonResponse);
							dataAdapter.notifyDataSetChanged();
							dataReceived = true;
						} catch (JSONException e) {
							e.printStackTrace();
							GeneralFunctions.getInstance().toast(getActivity(),
									"Error: " + e.getMessage());
						}
						hidepDialog();
					};
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(getActivity(),
						// error.getMessage());
						if (error instanceof NoConnectionError) {
							GeneralFunctions.getInstance().toast(getActivity(),
									"No Internet Connection!");
						}
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
