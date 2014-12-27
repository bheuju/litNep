package com.pike.litnep;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.adapter.CustomListAdapterPost;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Post;
import com.pike.litnep.util.GeneralFunctions;

public class FragmentTab2 extends Fragment {

	public int fragmentId;

	private int postSn = 0;
	private MainActivity act = new MainActivity();

	private boolean delete = false;
	private boolean edit = false;

	private ListView list;
	private LinearLayout noConMsg;
	private Button btnTryAgain;
	private ArrayList<Post> mContentsList = new ArrayList<Post>();
	private CustomListAdapterPost dataAdapter;

	private Map<String, String> item = new HashMap<String, String>();

	private int start = 0;
	private int count = 10;
	private int maxPost = 0;

	private String urlBase = "http://pike.comlu.com/extra/read.php";
	private String url = "";
	private String urlDelete = "http://pike.comlu.com/extra/delete.php";

	private static String TAG = MainActivity.class.getSimpleName();
	// progress dialog
	private ProgressDialog pDialog;
	// temp string
	private String jsonResponse;
	private JSONArray jsonArr;

	private boolean dataReceived = false;
	private boolean loadingMore = true;
	private boolean firstRun = true;

	private View loadMoreView;

	private BroadcastReceiver onEvent = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			GeneralFunctions.getInstance().toast(getActivity(),
					"Download Complete");
			install();
		}
	};

	public void install() {
		// begin installation by opening new file
		GeneralFunctions.getInstance().toast(getActivity(),
				"Trying to install new file from downloads..");
		File root = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String path = root.toString() + "/litNep.apk";
		
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		Log.e("Installing", "About to install new .apk from " + path);
		getActivity().startActivity(i);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment2, container, false);

		list = (ListView) v.findViewById(R.id.listPosts);
		noConMsg = (LinearLayout) v.findViewById(R.id.noConMsg);
		btnTryAgain = (Button) v.findViewById(R.id.btnTryAgain);

		noConMsg.setVisibility(View.GONE);

		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Please wait... \nLoading contents");
		pDialog.setCancelable(true);

		// load more footer for list view
		loadMoreView = ((LayoutInflater) getActivity().getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.list_footer_loadmore, null, false);
		list.addFooterView(loadMoreView, null, false);

		dataAdapter = new CustomListAdapterPost(getActivity(), mContentsList);
		list.setAdapter(dataAdapter);
		jsonArrRequest();

		registerForContextMenu(list); // for long press

		// list.setTextFilterEnabled(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Post post = (Post) parent.getItemAtPosition(position);
				Post post = (Post) dataAdapter.getItem(position);
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

					// prevent loadMore when nothing left to load
					if (maxPost >= (start + count)) {
						// Load more
						start += count;
						jsonArrRequest();
					} else {
						// No more to load
						list.removeFooterView(loadMoreView);
					}

				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

		});

		btnTryAgain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				refresh();

			}
		});

		// TODO: load data from local database
		// TODO: and add to mContentsList

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter f = new IntentFilter(Updater.ACTION_COMPLETE);
		getActivity().registerReceiver(onEvent, f);
	}

	@Override
	public void onPause() {
		getActivity().unregisterReceiver(onEvent);
		super.onPause();
	}

	// Automatically called when user long-clicks ListView items
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.listPosts) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Post post = (Post) dataAdapter.getItem(info.position);
			menu.setHeaderTitle(post.getTitle());

			int userId = post.getUserId();
			postSn = post.getSn();

			item.put("edit", "Edit");
			item.put("delete", "Delete");
			item.put("share", "Share");
			item.put("save", "Save");
			item.put("fav", "Favourite");

			ArrayList<String> menuItems = new ArrayList<String>();
			menuItems.add("");
			menuItems.add("Save");
			menuItems.add("Favourite");

			// if the (logged-in user) is (owner of the post) then provide
			// permission to delete or edit
			if (act.getUserId() == userId) {
				Log.e("Testing long press:", "Validation Success");
				// menu.findItem(0).setVisible(false);
				menuItems.set(0, "Edit");
				menuItems.set(1, "Delete");
				edit = true;
				delete = true;
			} else {
				Log.e("Testing long press:", act.getUserId() + " User IDs "
						+ userId);
				menuItems.set(0, "Share");
				edit = false;
				delete = false;
			}

			for (int i = 0; i < menuItems.size(); i++) {
				menu.add(Menu.NONE, i, i, menuItems.get(i));
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			if (edit == true) {
				edit = false;
				doEdit();
			} else {
				doShare();
			}
			return true;
		case 1:
			if (delete == true) {
				delete = false;
				doDelete();
			} else {
				doSave();
			}
			return true;
		case 2:
			doFav();
			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}

	/**
	 * General Post Functions
	 */
	public void doEdit() {
		GeneralFunctions.getInstance().toast(getActivity(), "Edit");
		// TODO: Edit function: load to compose
	}

	public void doDelete() {
		GeneralFunctions.getInstance().toast(getActivity(), "Delete");

		final int userId = act.getUserId();

		Log.e("Check Delete data", "UserID: " + userId + ", sn: " + postSn);

		StringRequest req = new StringRequest(Method.POST, urlDelete,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("Response: ", response.toString());

						GeneralFunctions.getInstance().toast(getActivity(),
								"Deleted Successfully");
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// GeneralFunctions.getInstance().toast(getApplicationContext(),
						// error.toString());
						if (error instanceof NoConnectionError) {
							GeneralFunctions.getInstance().toast(getActivity(),
									"Connection Error !");
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", String.valueOf(userId));
				params.put("sn", String.valueOf(postSn));
				return params;
			}
		};
		// add to request queue
		AppController.getInstance().addToRequestQueue(req);

	}

	public void doShare() {
		GeneralFunctions.getInstance().toast(getActivity(), "Share");
	}

	public void doSave() {
		GeneralFunctions.getInstance().toast(getActivity(), "Save");
	}

	public void doFav() {
		GeneralFunctions.getInstance().toast(getActivity(), "Fav");
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
								post.setThumbnailUrl(obj
										.getString("thumbnailUrl"));
								post.setTitle(obj.getString("title"));
								post.setContent(obj.getString("content"));
								post.setCreatedAt(obj.getString("created_at"));

								mContentsList.add(post);
								maxPost++;
							}
							dataAdapter.notifyDataSetChanged();
							// show progressDialog on first run only
							if (!firstRun) {
								// GeneralFunctions.getInstance().toast(
								// getActivity(), "Success");
							}
							firstRun = false;
							loadingMore = false;
						} catch (JSONException e) {
							e.printStackTrace();
							GeneralFunctions.getInstance().toast(getActivity(),
									"Error: " + e.getMessage());
						}
						noConMsg.setVisibility(View.GONE);
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
						// tvNoConMsg.setVisibility(View.VISIBLE);
						noConMsg.setVisibility(View.VISIBLE);
						hidepDialog();
					}

				});

		// add request to request queue, and add tag for cancel if necessary
		AppController.getInstance().addToRequestQueue(req, "req");
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
		AppController.getInstance().cancelPendingRequests("req");
		start = 0;
		maxPost = 0;
		loadingMore = true;
		mContentsList.clear();
		GeneralFunctions.getInstance().toast(getActivity(), "Refreshing...");
		jsonArrRequest();
		list.removeFooterView(loadMoreView);
		list.addFooterView(loadMoreView, null, false);
		dataAdapter.notifyDataSetChanged();
	}
}
