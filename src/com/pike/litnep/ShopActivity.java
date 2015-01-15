package com.pike.litnep;

import java.net.URISyntaxException;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class ShopActivity extends ActionBarActivity {

	private Button btnBrowse;
	private TextView tvText;
	private Button btnUpload;
	private TextView tvFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);

		btnBrowse = (Button) findViewById(R.id.btnBrowse);
		tvText = (TextView) findViewById(R.id.tvText);
		btnUpload = (Button) findViewById(R.id.btnUpload);
		tvFile = (TextView) findViewById(R.id.tvFile);

		btnBrowse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent fileIntent = new Intent(getApplicationContext(),
						FileChooserActivity.class);
				// Intent getContentIntent = FileUtils.createGetContentIntent();
				// Intent fileIntent = Intent.createChooser(getContentIntent,
				// "Select a file");
				try {
					startActivityForResult(fileIntent, 1);
				} catch (ActivityNotFoundException e) {
					Log.e("File Selector",
							"No activity can handle picking a file.");
				}
			}
		});

		btnUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				final Uri uri = data.getData();
				String path;
				try {
					path = FileUtils.getPath(this, uri);
					tvText.setText(path);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shop, menu);
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
