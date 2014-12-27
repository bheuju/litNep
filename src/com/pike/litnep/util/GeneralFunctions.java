package com.pike.litnep.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pike.litnep.Updater;
import com.pike.litnep.app.AppController;

public class GeneralFunctions {
	/**
	 * This class consists of general functions used frequently everywhere
	 * */

	private static GeneralFunctions mInstance = new GeneralFunctions();
	private Context context;

	private String urlSrc = "http://pike.comlu.com/src/litNep.apk";
	private String urlSrcVersion = "http://pike.comlu.com/src/version";

	private GeneralFunctions() {
	}

	public static synchronized GeneralFunctions getInstance() {
		return mInstance;
	}

	public void toast(Context context, CharSequence text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * parsing created_at date to display in appropriate format
	 */
	public String dateParser(String created_at, String format) {
		SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d1;
		try {
			d1 = mdyFormat.parse(created_at);
			String mdx = sdf.format(d1);
			return mdx;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public boolean checkUpdate(final Context context) {
		this.context = context;
		try {
			PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);

			final int currentVersionCode = pkgInfo.versionCode;
			toast(context, "Version Code: " + currentVersionCode);

			StringRequest req = new StringRequest(Method.POST, urlSrcVersion,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.d("Response: ", response);

							int newVersionCode = Integer.parseInt(response);

							toast(context, "Version Retrived: " + response);

							if (newVersionCode > currentVersionCode) {
								updateApp();
							} else {
								toast(context, "Up-to-date: No updates found");
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// GeneralFunctions.getInstance().toast(getApplicationContext(),
							// error.toString());
							if (error instanceof NoConnectionError) {
								GeneralFunctions.getInstance().toast(context,
										"Connection Error !");
							}
						}
					});
			AppController.getInstance().addToRequestQueue(req);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public void updateApp() {
		// TODO: add update instructions
		Intent intent = new Intent(context, Updater.class);
		intent.setData(Uri.parse(urlSrc));
		context.startService(intent);
		toast(context, "Update in progress...");
	}

}
