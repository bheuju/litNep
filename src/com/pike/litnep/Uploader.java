package com.pike.litnep;

import java.io.File;
import java.net.URISyntaxException;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.pike.litnep.app.AppController;
import com.pike.litnep.util.GeneralFunctions;
import com.pike.litnep.util.PhotoMultipartRequest;

import android.support.v7.app.ActionBarActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Uploader extends ActionBarActivity {

	private Button btnUpload;
	private TextView tvFile;

	private String url = "http://pike.comlu.com/v1.0.1/uploadImage.php";

	private String filePath;
	private File file;

	private boolean uploadReady = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploader);

		btnUpload = (Button) findViewById(R.id.btnUpload);
		tvFile = (TextView) findViewById(R.id.tvFile);

		/********** Browse **********/
		btnUpload.setText("Browse");
		btnUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!uploadReady) {
					browseFile();
				} else {
					file = new File(filePath);
					uploadFile();
				}

			}
		});

	}

	public void browseFile() {
		Intent fileIntent = new Intent(getApplicationContext(),
				FileChooserActivity.class);
		try {
			startActivityForResult(fileIntent, 1);
		} catch (ActivityNotFoundException e) {
			Log.e("File Selector", "No activity can handle picking a file.");
		}
	}

	public void uploadFile() {

		PhotoMultipartRequest<String> req = new PhotoMultipartRequest<String>(
				url, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.e("File upload", "Success: " + response);
						GeneralFunctions.getInstance().toast(
								getApplicationContext(), "Upload Success");
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// Toast error
						Log.e("File upload", "Failed: " + error);
						GeneralFunctions.getInstance().toast(
								getApplicationContext(), "Failed Upload");
					}
				}, file);
		AppController.getInstance().addToRequestQueue(req);
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
				try {
					filePath = FileUtils.getPath(this, uri);
					tvFile.setText(filePath);
					btnUpload.setText("Upload");
					uploadReady = true;
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
	}

	/*********************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uploader, menu);
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
