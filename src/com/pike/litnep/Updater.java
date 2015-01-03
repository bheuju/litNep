package com.pike.litnep;

import java.io.BufferedInputStream;
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

	UpdateActivity act = new UpdateActivity();

	public Updater() {
		super("UpdateActivity");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			int downloadedSize = 0;

			File root = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

			root.mkdirs(); // create directory it may not exist

			File output = new File(root, intent.getData().getLastPathSegment());
			//Log.e("Testing output file path", output.getPath());

			URL url = new URL(intent.getData().toString());
			HttpURLConnection c = (HttpURLConnection) url.openConnection();

			// if (output.exists()) {
			// output.delete();
			downloadedSize = (int) output.length();

			c.setRequestProperty("Range", "bytes=" + (output.length()) + "-");
			// }

			c.setDoInput(true);
			c.setDoOutput(true);

			c.setRequestMethod("GET");
			c.setReadTimeout(15000);
			c.connect();

			int fileLength = c.getContentLength() + downloadedSize;

			//Log.e("Download File Size", "DL: " + downloadedSize + " TL:"
			//		+ fileLength);

			// FileOutputStream fos = new FileOutputStream(output.getPath());
			// BufferedOutputStream out = new BufferedOutputStream(fos);

			FileOutputStream fos = (downloadedSize == 0) ? new FileOutputStream(
					output.getPath()) : new FileOutputStream(output.getPath(),
					true);
			BufferedOutputStream out = new BufferedOutputStream(fos);

			try {
				InputStream in = new BufferedInputStream(c.getInputStream());
				// InputStream in = c.getInputStream();
				byte[] buffer = new byte[8192]; // stream results 1kb at a time
				int len = 0;

				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
					downloadedSize += len;
					UpdateActivity
							.publishProgress((int) ((downloadedSize / (float) (fileLength)) * 100));
					//Log.e("Downloaded", "Total: " + fileLength
					//		+ ", Downloaded: " + downloadedSize
					//		+ ", Remaining: " + (fileLength - downloadedSize));
				}
				out.flush();
			} finally {
				fos.getFD().sync();
				out.close();
			}
			//Log.e("Updater Service", "Download complete...");
			//Log.e("Updater Service", "" + "-1");

			if (downloadedSize != fileLength) {
				act.setError("Connection failed...");
			} else {
				UpdateActivity.downloadComplete = true;
			}

			sendBroadcast(new Intent(ACTION_COMPLETE));

		} catch (IOException e2) {
			//Log.e(getClass().getName(), "Exception in download", e2);
			act.setError("Connection timeout...");
			sendBroadcast(new Intent(ACTION_COMPLETE));
		}
	}
}
