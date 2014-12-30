package com.pike.litnep;

import com.pike.litnep.model.User;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AccountActivity extends ActionBarActivity {

	private ImageButton btnImage;
	private TextView tvName, tvEmail, tvPhone, tvAddress;
	private Button btnPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		btnImage = (ImageButton) findViewById(R.id.btnImage);
		btnPass = (Button) findViewById(R.id.btnPass);

		tvName = (TextView) findViewById(R.id.tvName);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvAddress = (TextView) findViewById(R.id.tvAddress);

		tvName.setText(User.getInstance().getFirstName() + " "
				+ User.getInstance().getLastName());
		tvEmail.setText(User.getInstance().getEmail());

		if (User.getInstance().getPhone().equals("")) {
			tvPhone.setVisibility(View.GONE);
		}

		if (User.getInstance().getAddress().equals("")) {
			tvAddress.setVisibility(View.GONE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
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
