package com.pike.litnep;

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

}
