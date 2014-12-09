package com.pike.litnep.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		rotate();
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		rotate();

	}

	private void rotate() {
		setSelected(true);
	}

	private void init() {
		if (!isInEditMode()) {

		}
	}
}
