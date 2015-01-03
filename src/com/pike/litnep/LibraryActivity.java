package com.pike.litnep;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LibraryActivity extends ActionBarActivity {

	private ListView mLibraryList;
	private ArrayAdapter<String> arrayList;
	private String[] mLibraryListItems = { "Books" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library);

		arrayList = new ArrayAdapter<String>(this, R.layout.list_item_general,
				mLibraryListItems);
		mLibraryList = (ListView) findViewById(R.id.libraryList);
		mLibraryList.setAdapter(arrayList);

		mLibraryList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						selectItem(position);
					}
				});

	}

	/**
	 * Functions
	 ***********************/
	private void selectItem(int position) {
		// load new activity on basis of position
		// TODO:
		switch (position) {
		case 0:
			openBooks();
			break;
		// case 1:
		// openReviews();
		// break;

		// case 2:
		// openShop();
		// break;
		}
	}

	private void openReviews() {
		startActivity(new Intent(this, ReviewsActivity.class));
	}

	private void openBooks() {
		startActivity(new Intent(this, BooksActivity.class));
	}

	private void openShop() {
		startActivity(new Intent(this, ShopActivity.class));
	}

	/**
	 * General
	 ***********************/
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
}
