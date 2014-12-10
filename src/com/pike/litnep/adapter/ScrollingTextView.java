package com.pike.litnep.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollingTextView extends TextView {

	public ScrollingTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		rotate();
	}

	public ScrollingTextView(Context context, AttributeSet attrs) {
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
