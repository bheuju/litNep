package com.pike.litnep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private Button hideActionBar;
	private TextView tvStatus;
	private boolean statusActionBar = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/** Listener for Hiding and Displaying ActionBar
		 *************************************************/
		{
			hideActionBar = (Button) findViewById(R.id.btnHideActionBar);
			tvStatus = (TextView) findViewById(R.id.tvStatus);
			hideActionBar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					statusActionBar = !statusActionBar;

					ActionBar actionBar = getSupportActionBar();
					if (statusActionBar) {
						actionBar.hide();
						tvStatus.setText("Hidden");
						hideActionBar.setText("Show");
					} else {
						actionBar.show();
						tvStatus.setText("Displayed");
						hideActionBar.setText("Hide");
					}
				}
			});
		}

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

	
	/** ActionBar functions 
	 *******************************/
	private void openCompose() {
		Intent intent = new Intent (this, ComposeActivity.class);
		startActivity(intent);
	}
	private void openSignin() {
		Intent intent = new Intent (this, SigninActivity.class);
		startActivity(intent);
	}
	private void openSettings() {
		
	}

}
