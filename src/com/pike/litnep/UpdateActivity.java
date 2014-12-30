package com.pike.litnep;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.app.AppController;
import com.pike.litnep.util.GeneralFunctions;

public class UpdateActivity extends ActionBarActivity {

	private Button btnCheckUpdate;
	private Button btnInstallUpdate;
	private TextView tvProgressStatus;
	private TextView tvError;
	private static ProgressBar progressBar;
	private static int progressStatus;

	SharedPreferences prefs;
	private static final String PREF_DOWNLOAD_COMPLETE = "downloadComplete";

	private static boolean error = false;
	private static String errorTxt;

	public static boolean downloadComplete = false;

	private Handler handler = new Handler();

	private String urlSrc = "http://pike.comlu.com/src/akshyar.apk";
	private String urlSrcVersion = "http://pike.comlu.com/src/version";

	private BroadcastReceiver onEvent = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			GeneralFunctions.getInstance().toast(getApplicationContext(),
					"Download Complete");
			install();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);

		btnCheckUpdate = (Button) findViewById(R.id.btnCheckUpdate);
		btnInstallUpdate = (Button) findViewById(R.id.btnInstallUpdate);
		tvProgressStatus = (TextView) findViewById(R.id.tvProgressStatus);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		tvError = (TextView) findViewById(R.id.tvError);

		btnInstallUpdate.setVisibility(View.GONE);

		// update progress bar and progress text
		new Thread(new Runnable() {
			public void run() {
				while (progressStatus < 100) {
					// Update the progress bar and display the

					// current value in the text view
					handler.post(new Runnable() {
						public void run() {
							progressBar.setProgress(progressStatus);
							tvProgressStatus.setText(progressStatus + "/"
									+ progressBar.getMax());
						}
					});
					try {
						// Sleep for 200 milliseconds.

						// Just to display the progress slowly
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		btnCheckUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tvError.setText("");
				btnCheckUpdate.setText("Retry");

				error = false;
				if (!downloadComplete) {
					checkVersion();
				} else {
					progressStatus = 100;
					progressBar.setProgress(progressStatus);
					tvProgressStatus.setText("100/100");
					btnInstallUpdate.setVisibility(View.VISIBLE);
				}

				btnCheckUpdate.setEnabled(false);
			}
		});

		btnInstallUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				install();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		IntentFilter f = new IntentFilter(Updater.ACTION_COMPLETE);
		registerReceiver(onEvent, f);
	}

	@Override
	public void onPause() {
		unregisterReceiver(onEvent);
		if (prefs != null) {
			prefs.edit().putBoolean(PREF_DOWNLOAD_COMPLETE, downloadComplete)
					.commit();
		}
		super.onPause();
	}

	public void install() {
		// begin installation by opening new file
		if (error) {
			tvError = (TextView) findViewById(R.id.tvError);
			tvError.setText(errorTxt);
			btnCheckUpdate.setEnabled(true);
			return;
		}

		progressStatus = 100;
		File root = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String path = root.toString() + "/litNep.apk";

		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		Log.e("Installing", "About to install new .apk from " + path);
		startActivity(i);
	}

	public boolean checkVersion() {
		try {
			PackageInfo pkgInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);

			final int currentVersionCode = pkgInfo.versionCode;
			GeneralFunctions.getInstance().toast(this,
					"Version Code: " + currentVersionCode);

			StringRequest req = new StringRequest(Method.POST, urlSrcVersion,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.d("Response: ", response);

							int newVersionCode = Integer.parseInt(response);

							GeneralFunctions.getInstance().toast(
									getApplicationContext(),
									"Version Retrived: " + response);

							if (newVersionCode > currentVersionCode) {
								// new version found on server
								// download updated app
								updateApp();
							} else {
								GeneralFunctions.getInstance().toast(
										getApplicationContext(),
										"Up-to-date: No updates found");
								btnCheckUpdate.setEnabled(true);
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// GeneralFunctions.getInstance().toast(getApplicationContext(),
							// error.toString());
							btnCheckUpdate.setEnabled(true);
							if (error instanceof NoConnectionError) {
								GeneralFunctions.getInstance().toast(
										getApplicationContext(),
										"Connection Error !");
							}
						}
					});
			AppController.getInstance().addToRequestQueue(req);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			btnCheckUpdate.setEnabled(true);
		}

		return false;
	}

	public void updateApp() {
		// TODO: add update instructions
		if (downloadComplete) {
			return;
		}
		Intent intent = new Intent(this, Updater.class);
		intent.setData(Uri.parse(urlSrc));
		startService(intent);
		GeneralFunctions.getInstance().toast(this, "Update in progress...");
	}

	public static void publishProgress(int progress) {
		progressStatus = progress;
	}

	public void setError(String errorText) {
		errorTxt = errorText;
		error = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
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
