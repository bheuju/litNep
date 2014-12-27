package com.pike.litnep;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pike.litnep.util.GeneralFunctions;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class Updater extends IntentService {
	public static final String ACTION_COMPLETE = "com.pike.litnep.updateactivity.action.COMPLETE";

	public Updater() {
		super("UpdateActivity");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			File root = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

			root.mkdirs(); // create directory it may not exist

			File output = new File(root, intent.getData().getLastPathSegment());
			if (output.exists()) {
				output.delete();
			}

			URL url = new URL(intent.getData().toString());
			HttpURLConnection c = (HttpURLConnection) url.openConnection();

			c.setRequestMethod("GET");
			c.setReadTimeout(15000);
			c.connect();

			FileOutputStream fos = new FileOutputStream(output.getPath());
			BufferedOutputStream out = new BufferedOutputStream(fos);

			try {
				InputStream in = c.getInputStream();
				byte[] buffer = new byte[8192]; // stream results 8kb at a time
				int len = 0;

				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.flush();
			} finally {
				fos.getFD().sync();
				out.close();
			}
			Log.e("Updater Service", "Download complete...");
			sendBroadcast(new Intent(ACTION_COMPLETE));
		} catch (IOException e2) {
			Log.e(getClass().getName(), "Exception in download", e2);
		}
	}
}
