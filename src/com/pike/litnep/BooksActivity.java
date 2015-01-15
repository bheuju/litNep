package com.pike.litnep;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pike.litnep.adapter.CustomListAdapterBooks;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Books;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

public class BooksActivity extends ActionBarActivity {

	private static String TAG = MainActivity.class.getSimpleName();

	private String urlBase = "http://pike.comlu.com/extra/books.php";
	private String url = "";
	private int start = 0, count = 10;

	private Button btnTryAgain;

	private ArrayList<Books> mContentsList = new ArrayList<Books>();
	private CustomListAdapterBooks dataAdapter;
	// private View list;
	private ListView list;
	private GridView list1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books);

		list = (ListView) findViewById(R.id.listBooks);
		list1 = (GridView) findViewById(R.id.gridBooks);

		list1.setVisibility(View.GONE);

		dataAdapter = new CustomListAdapterBooks(getApplicationContext(),
				mContentsList);
		list.setAdapter(dataAdapter);
		jsonArrRequest();

		// click listener for items
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Books book = (Books) parent.getItemAtPosition(position);
				Books book = (Books) dataAdapter.getItem(position);
				GeneralFunctions.getInstance().toast(getApplicationContext(),
						book.getTitle());
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.books, menu);
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

	private void setUrl() {
		// url = urlBase
		// + ("?start=" + String.valueOf(start) + "&count=" + String
		// .valueOf(count));
		url = urlBase;
	}

	/**
	 * Methods to make JSON array request
	 */
	private void jsonArrRequest() {
		// starts with [
		setUrl();
		// if (firstRun) {
		// }
		JsonArrayRequest req = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						// Log.d(TAG, response.toString());
						// setContentView(R.layout.activity_books);
						try {
							// parsing json array response
							// loop through each json object
							for (int i = 0; i < response.length(); i++) {
								JSONObject obj = (JSONObject) response.get(i);

								Books book = new Books();

								book.setCoverUrl(obj.getString("coverUrl"));
								book.setTitle(obj.getString("title"));
								book.setAuthor(obj.getString("author"));
								book.setYear(obj.getString("year"));
								book.setReview(obj.getString("review"));

								mContentsList.add(book);
								// maxPost++;
							}
							dataAdapter.notifyDataSetChanged();
							// show progressDialog on first run only
							// if (!firstRun) {
							// GeneralFunctions.getInstance().toast(
							// getActivity(), "Success");
							// }
							// firstRun = false;
							// loadingMore = false;
						} catch (JSONException e) {
							e.printStackTrace();
							GeneralFunctions.getInstance().toast(
									getApplicationContext(),
									"Error: " + e.getMessage());
						}
						// noConMsg.setVisibility(View.GONE);
					};
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// VolleyLog.e(TAG, "Error: " + error.getMessage());
						// GeneralFunctions.getInstance().toast(getActivity(),
						// error.getMessage());
						if (error instanceof NoConnectionError) {
							GeneralFunctions.getInstance().toast(
									getApplicationContext(),
									"Connection Error !");
						}
						// tvNoConMsg.setVisibility(View.VISIBLE);
						// noConMsg.setVisibility(View.VISIBLE);
						// setContentView(R.layout.no_con_msg);
						// btnTryAgain = (Button)
						// findViewById(R.id.btnTryAgain);
					}
				});
		// add request to request queue, and add tag for cancel if necessary
		AppController.getInstance().addToRequestQueue(req, "req");

	}

	public void refresh() {
		AppController.getInstance().cancelPendingRequests("req");
		// start = 0;
		// maxPost = 0;
		// loadingMore = true;
		// mContentsList.clear();
		GeneralFunctions.getInstance().toast(getApplicationContext(),
				"Refreshing...");
		jsonArrRequest();
		// list.removeFooterView(loadMoreView);
		// list.addFooterView(loadMoreView, null, false);
		// dataAdapter.notifyDataSetChanged();
	}

}
