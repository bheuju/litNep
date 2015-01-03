package com.pike.litnep;

import com.pike.litnep.model.User;
import com.pike.litnep.util.GeneralFunctions;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
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
