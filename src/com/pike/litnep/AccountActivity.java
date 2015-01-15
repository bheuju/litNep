package com.pike.litnep;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pike.litnep.app.AppController;
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
	private NetworkImageView imgUser;
	private TextView tvName, tvEmail, tvPhone, tvAddress;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		btnImage = (ImageButton) findViewById(R.id.btnImage);
		imgUser = (NetworkImageView) findViewById(R.id.imgUser);

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

		imgUser.setImageUrl(User.getInstance().getImgUrl(), imageLoader);

		btnImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						ImageUploader.class));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			openEditAccount();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openEditAccount() {
		Intent editAccount = new Intent(this, EditAccount.class);
		startActivity(editAccount);
	}

	private void openSettings() {
		startActivity(new Intent(this, Prefs.class));
	}
}
