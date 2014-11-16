package com.pike.litnep;

import com.pike.litnep.adapter.TabsPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private Button hideActionBar;
	// private TextView tvStatus;
	private boolean statusActionBar = false;

	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializations
		mViewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set 3 tabs icons and/or titles and setting tabListeners
		Tab tab1 = actionBar.newTab().setText("Tab1").setTabListener(this);
		Tab tab2 = actionBar.newTab().setText("Tab2").setTabListener(this);
		Tab tab3 = actionBar.newTab().setText("Tab3").setTabListener(this);

		// Add tabs to actionBar
		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);

		/** Select appropriate tab on swiping
		 ********************************************* */
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
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
		
		/**
		 * Listener for Hiding and Displaying ActionBar
		 *************************************************/
		{
			hideActionBar = (Button) findViewById(R.id.btnHideActionBar);
			// tvStatus = (TextView) findViewById(R.id.tvStatus);
			hideActionBar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					statusActionBar = !statusActionBar;

					ActionBar actionBar = getSupportActionBar();
					if (statusActionBar) {
						actionBar.hide();
						// tvStatus.setText("Hidden");
						hideActionBar.setText("Show");
					} else {
						actionBar.show();
						// tvStatus.setText("Displayed");
						hideActionBar.setText("Hide");
					}
				}
			});
		}// end buttonListener

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_compose:
			openCompose();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_signin:
			openSignin();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * ActionBar functions
	 *******************************/
	private void openCompose() {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}

	private void openSignin() {
		Intent intent = new Intent(this, SigninActivity.class);
		startActivity(intent);
	}

	private void openSettings() {

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

}
