package com.pike.litnep.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pike.litnep.R;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Books;
import com.pike.litnep.util.GeneralFunctions;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapterBooks extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Books> bookItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapterBooks(Context context, ArrayList<Books> bookItems) {
		this.context = context;
		this.bookItems = bookItems;
	}

	@Override
	public int getCount() {
		return bookItems.size();
	}

	@Override
	public Object getItem(int position) {
		return bookItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_custom_books,
					parent, false);
			imageLoader = AppController.getInstance().getImageLoader();
		}

		NetworkImageView imgCover = (NetworkImageView) convertView
				.findViewById(R.id.imgCover);
		TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView author = (TextView) convertView.findViewById(R.id.tvAuthor);
		TextView year = (TextView) convertView.findViewById(R.id.tvYear);
		TextView review = (TextView) convertView.findViewById(R.id.tvReview);

		Books book = (Books) bookItems.get(position);

		String strCoverUrl = book.getCoverUrl();
		String strTitle = book.getTitle();
		String strAuthor = book.getAuthor();
		String strYear = book.getYear();
		String strReview = book.getReview();

		imgCover.setImageUrl(strCoverUrl, imageLoader);
		title.setText(strTitle);
		author.setText(strAuthor);
		year.setText(strYear);

		if (strReview.equals("")) {
			review.setGravity(Gravity.CENTER);
			review.setText("No Review Available !");
		} else {
			review.setGravity(Gravity.NO_GRAVITY);
			review.setText(strReview);
		}

		return convertView;
	}
}
