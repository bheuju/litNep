package com.pike.litnep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pike.litnep.adapter.TabsPagerAdapter;
import com.pike.litnep.util.GeneralFunctions;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	// actionBar
	private boolean statusActionBar = false;

	// for fragments
	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	// for drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayAdapter<String> arrayAdapter;
	private String[] mDrawerListTitles = { "SignIn", "Library", "Settings" };
	private ArrayList<String> arrayList;

	// if signIn
	boolean isLoggedIn = false;
	private String userName = null;
	
	private int userId; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isLoggedIn = extras.getBoolean("login");
			userId = extras.getInt("userId");
			userName = extras.getString("firstName") + " "
					+ extras.getString("lastName");
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set the adapter for the list view
		arrayList = new ArrayList<String>(Arrays.asList(mDrawerListTitles));
		arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, arrayList);
		mDrawerList.setAdapter(arrayAdapter);

		// set the list's click listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle("litNep");
				supportInvalidateOptionsMenu(); // create call to
												// onPrepareOptionsMenu()
			};

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle("Options");
				supportInvalidateOptionsMenu(); // create call to
												// onPrepareOptionsMenu()

			};
		};

		// set the drawer toggle as the drawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// left_drawer = (ListView) findViewById(R.id.left_drawer);
		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						selectItem(position);
					}
				});

		/** arrange display according to login / logout status */
		changeOnLogInStatus(isLoggedIn);

		// Initializations
		mViewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// actionBar.setDisplayShowHomeEnabled(false); // to hide the title bar
		// actionBar.setDisplayShowTitleEnabled(false); // of the action bar and
		// show only tabs
		// Set 3 tabs icons and/or titles and setting tabListeners
		// Tab tab1 =
		// actionBar.newTab().setText("Writings").setTabListener(this);
		Tab tab2 = actionBar.newTab().setText("Writings").setTabListener(this);
		// Tab tab3 = actionBar.newTab().setText("Tab3").setTabListener(this);

		// Add tabs to actionBar
		// actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		// actionBar.addTab(tab3);

		/**
		 * Select appropriate tab on swiping
		 ********************************************* */
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						actionBar.setSelectedNavigationItem(position);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * General functions
	 *******************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		// Handle your other action bar items...
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_compose:
			openCompose();
			return true;
		case R.id.action_refresh:
			openRefresh();
			return true;
		case R.id.action_signin:
			openSignin();
			return true;
		case R.id.action_signup:
			openSignup();
			return true;
		case R.id.action_library:
			openLibrary();
			return true;
		case R.id.action_feedback:
			openFeedback();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * ActionBar functions
	 *******************************/
	private void openAccount() {
		Intent accountActivity = new Intent(this, AccountActivity.class);
		startActivity(accountActivity);
	}

	private void openCompose() {
		Intent composeActivity = new Intent(this, ComposeActivity.class);
		composeActivity.putExtra("userId", userId);
		startActivity(composeActivity);
	}

	private void openRefresh() {
		// mViewPager.setCurrentItem(getPosition());
		// mViewPager.setCurrentItem(0);
		List<Fragment> allFragments = getSupportFragmentManager()
				.getFragments();
		if (allFragments != null) {
			for (Fragment fragment : allFragments) {
				FragmentTab2 f = (FragmentTab2) fragment;
				if (f.fragmentId == 0)
					f.refresh();
			}
		}

	}

	private void openSignin() {
		Intent intent = new Intent(this, SigninActivity.class);
		startActivity(intent);
	}

	private void openSignup() {
		startActivity(new Intent(this, SignupActivity.class));
	}

	private void openLibrary() {
		startActivity(new Intent(this, LibraryActivity.class));
	}

	private void openFeedback() {
		GeneralFunctions.getInstance().toast(getApplicationContext(),
				"Under Construction");
	}

	private void openSettings() {
		GeneralFunctions.getInstance().toast(getApplicationContext(),
				"Under Construction");
	}

	/**
	 * Navigation Drawer functions
	 *******************************/
	private void selectItem(int position) {
		// load new activity on basis of position
		// TODO:
		switch (position) {
		case 0:
			if (isLoggedIn) { // open only if logged in
				openAccount();
			} else {
				openSignin();
			}
			break;
		case 1:
			openLibrary();
			break;
		case 2:
			openSettings();
			break;
		case 3:
			changeOnLogInStatus(false);
			break;
		}

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * ActionBar Tabs functions
	 *******************************/
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// show
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// remove
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// ignore
	}

	/**
	 * Contents management on basis of logIn status
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// if the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (drawerOpen) {
			menu.findItem(R.id.action_signin).setVisible(false);
			menu.findItem(R.id.action_compose).setVisible(false);
			// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		} else {
			// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}

		if (isLoggedIn) {
			menu.findItem(R.id.action_signin).setVisible(false);
			menu.findItem(R.id.action_signup).setVisible(false);
		} else {
			menu.findItem(R.id.action_compose).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	private void changeOnLogInStatus(boolean status) {
		if (status) {
			/**
			 * Case: signed IN
			 * 
			 * @UserName
			 * @Library
			 * @Settings
			 */
			arrayList.set(0, userName);
			arrayList.add("SignOut");

		} else if (!status) {
			/**
			 * Case: signed OUT
			 * 
			 * @SignIn
			 * @Library
			 * @Settings
			 * @SignOut
			 */
			arrayList.set(0, "SignIn");
			if (arrayList.size() >= 4) {
				arrayList.remove(3);
			}
			isLoggedIn = false;
		}
		arrayAdapter.notifyDataSetChanged();
		// menu items are managed from onPrepareOptionsMenu() function
		supportInvalidateOptionsMenu(); // create call to onPrepareOptionsMenu()

	}

}
