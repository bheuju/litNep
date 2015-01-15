package com.pike.litnep;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.auth.MalformedChallengeException;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.User;
import com.pike.litnep.util.GeneralFunctions;
import com.pike.litnep.util.PhotoMultipartRequest;

import android.support.v7.app.ActionBarActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageUploader extends ActionBarActivity {

	private Button btnUpload;
	private TextView tvFile;
	private ImageView img;

	private String uploadUrl = "http://pike.comlu.com/v1.0.1/uploadImage.php";

	private String filePath;
	private File file;

	private boolean uploadReady = false;

	int serverResponseCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_uploader);

		btnUpload = (Button) findViewById(R.id.btnUpload);
		tvFile = (TextView) findViewById(R.id.tvFile);
		img = (ImageView) findViewById(R.id.img);

		/********** Browse **********/
		btnUpload.setText("Browse");
		btnUpload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!uploadReady) {
					browseFile();
				} else {
					file = new File(filePath);

					new Thread(new Runnable() {
						public void run() {
							runOnUiThread(new Runnable() {
								public void run() {
									tvFile.setText("uploading started.....");
								}
							});
							uploadFile();
						}
					}).start();

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

	public int uploadFile() {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(filePath);

		if (!sourceFile.isFile()) {
			Log.e("Upload File", "Source File does not exist: " + filePath);

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tvFile.setText("Source File does not exist: " + filePath);
				}
			});
			return 0;
		} else {
			try {
				// open a URL connection to servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(uploadUrl);

				// open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // allow inputs
				conn.setDoOutput(true); // allow outputs
				conn.setUseCaches(false); // don't used a cached copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				conn.setRequestProperty("uploaded_file", filePath);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ filePath + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart from data necessary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// response from the server (code + message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("Upload File", "HTTP Response: " + serverResponseMessage
						+ ": " + serverResponseCode);

				if (serverResponseCode == 200) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							String msg = "File upload completed: " + filePath;
							tvFile.setText(msg);
							GeneralFunctions.getInstance().toast(
									getApplicationContext(),
									"File upload complete.");
						}
					});
				}

				// close the streams
				fileInputStream.close();
				dos.flush();
				dos.close();
			} catch (MalformedURLException ex) {
				ex.printStackTrace();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						tvFile.setText("MalformedURLException: check script url");
						GeneralFunctions.getInstance().toast(
								getApplicationContext(),
								"MalformedURLException");
					}
				});
				Log.e("Upload file exception", "Error: " + ex.getMessage(), ex);
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						tvFile.setText("Got Exception : see logcat ");
						GeneralFunctions.getInstance().toast(
								getApplicationContext(),
								"Got Exception : see logcat ");
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			return serverResponseCode;
		}
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
					
					Bitmap bitmap = BitmapFactory.decodeFile(filePath);
					img.setImageBitmap(bitmap);
					
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
		getMenuInflater().inflate(R.menu.image_uploader, menu);
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
