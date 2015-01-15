package com.pike.litnep;

import com.pike.litnep.model.User;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditAccount extends ActionBarActivity {

	private int userId;

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;

	private EditText etFirstName, etLastName, etEmail, etPass, etPassRe,
			etPhone, etAddress;
	private Button btnUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_account);

		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etLastName = (EditText) findViewById(R.id.etLastName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPass = (EditText) findViewById(R.id.etPass);
		etPassRe = (EditText) findViewById(R.id.etPassRe);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etAddress = (EditText) findViewById(R.id.etAddress);

		btnUpdate = (Button) findViewById(R.id.btnUpdate);

		User user = User.getInstance();

		userId = user.getUserId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		phone = user.getPhone();
		address = user.getAddress();

		// Set available details in EditTexts
		setDetails();

		btnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: open edit dialog to enter password
			}
		});

	}

	/*************/
	public void setDetails() {
		etFirstName.setText(firstName);
		etLastName.setText(lastName);
		etEmail.setText(email);
		etPhone.setText(phone);
		etAddress.setText(address);
	}

	/*************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_account, menu);
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
