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
	private String[] mLibraryListItems = { "Reviews", "Books", "Shop" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library);

		arrayList = new ArrayAdapter<String>(this, R.layout.list_item_custom,
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
			openReviews();
			break;
		case 1:
			openBooks();
			break;
		case 2:
			openShop();
			break;
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.library, menu);
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
