package com.pike.litnep.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class GeneralFunctions {
	/**
	 * This class consists of general functions used frequently everywhere
	 * */

	private static GeneralFunctions mInstance = new GeneralFunctions();

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

}
