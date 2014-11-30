package com.pike.litnep;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Parser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.adapter.CustomListAdapter;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Post;
import com.pike.litnep.util.GeneralFunctions;

public class FragmentTab2 extends Fragment {

	public int fragmentId;

	private ListView list;
	private ArrayList<Post> mContentsList = new ArrayList<Post>();
	private CustomListAdapter dataAdapter;

	private int start = 0;
	private int count = 10;
	private int maxPost = 0;

	private String urlBase = "http://pike.comlu.com/extra/read.php";
	private String url = "";

	private static String TAG = MainActivity.class.getSimpleName();
	// progress dialog
	private ProgressDialog pDialog;
	// temp string
	private String jsonResponse;
	private JSONArray jsonArr;

	private boolean dataReceived = false;
	private boolean loadingMore = true;
	private boolean firstRun = true;

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

		// list.setTextFilterEnabled(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Post post = (Post) parent.getItemAtPosition(position);
				GeneralFunctions.getInstance().toast(getActivity(),
						post.getTitle());
				// TODO: call singlepostActivity with passing post id
				Intent singlePostActivity = new Intent(getActivity(),
						SinglePage.class);
				singlePostActivity.putExtra("firstName", post.getfirstName());
				singlePostActivity.putExtra("lastName", post.getlastName());
				singlePostActivity.putExtra("title", post.getTitle());
				singlePostActivity.putExtra("content", post.getContent());
				singlePostActivity.putExtra("created_at", post.getCreatedAt());
				startActivity(singlePostActivity);
			}

		});

		// Load more on scroll
		list.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					// End of listView reached
					// display loading more
					Log.e("Loading..", "End reached... " + maxPost + " And "
							+ (start + count));
					loadingMore = true;

					// load more
					// prevent loadMore when nothing left to load
					if (maxPost >= (start + count)) {
						GeneralFunctions.getInstance().toast(getActivity(),
								"Loading more...");
						start += count;
						jsonArrRequest();
					} else {
						GeneralFunctions.getInstance().toast(getActivity(),
								"No more contents to load");
					}

				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

		});

		// TODO: load data from local database
		// TODO: and add to mContentsList

		return v;
	}

	/**
	 * set url get parameters for fetching json array of posts eg:
	 * http://pike.comlu.com/extra/read.php?start=20&count=5
	 */
	private void setUrl() {
		url = urlBase
				+ ("?start=" + String.valueOf(start) + "&count=" + String
						.valueOf(count));
	}

	/**
	 * Methods to make JSON array request
	 */
	private void jsonArrRequest() {
		// starts with [
		setUrl();
		if (firstRun) {
			showpDialog();
		}
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
								post.setUserId(obj.getInt("user_id"));
								post.setfirstName(obj.getString("firstName"));
								post.setlastName(obj.getString("lastName"));
								post.setTitle(obj.getString("title"));
								post.setContent(obj.getString("content"));
								post.setCreatedAt(obj.getString("created_at"));
								mContentsList.add(post);
								maxPost++;
							}
							dataAdapter.notifyDataSetChanged();
							// show progressDialog on first run only
							if (!firstRun) {
								GeneralFunctions.getInstance().toast(
										getActivity(), "Success");
							}
							firstRun = false;
							loadingMore = false;
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
									"Connection Error !");
						}
						hidepDialog();
					}

				}) {
		};

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

	public void refresh() {
		start = 0;
		maxPost = 0;
		loadingMore = true;
		mContentsList.clear();
		GeneralFunctions.getInstance().toast(getActivity(), "Refreshing...");
		jsonArrRequest();
		dataAdapter.notifyDataSetChanged();
	}
}
