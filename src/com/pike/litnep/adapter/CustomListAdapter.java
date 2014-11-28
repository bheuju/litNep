package com.pike.litnep.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pike.litnep.R;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Post> postItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Context context, ArrayList<Post> postItems) {
		this.context = context;
		this.postItems = postItems;
	}

	@Override
	public int getCount() {
		return postItems.size();
	}

	@Override
	public Object getItem(int position) {
		return postItems.get(position);
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
			convertView = inflater.inflate(R.layout.list_item_custom, parent,
					false);
		}
		// ImageView thumbnail = (ImageView) convertView
		// .findViewById(R.id.imgThumbnail);
		TextView userName = (TextView) convertView
				.findViewById(R.id.tvUserName);
		TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView contents = (TextView) convertView
				.findViewById(R.id.tvContents);

		//imageLoader = AppController.getInstance().getImageLoader();
		/*
		 * NetworkImageView thumbnail = (NetworkImageView) convertView
		 * .findViewById(R.id.imgThumbnail);
		 */

		// getting post data for the row
		Post p = (Post) postItems.get(position);
		Log.d("TAG", p.getUserName());
		Log.d("TAG", p.getTitle());
		Log.d("TAG", p.getContents());

		userName.setText(p.getUserName());
		title.setText(p.getTitle());
		contents.setText(p.getContents());

		// thumbnail image
		// thumbnail.setImageUrl(p.getThumbnailUrl(), imageLoader);

		return convertView;
	}
}
