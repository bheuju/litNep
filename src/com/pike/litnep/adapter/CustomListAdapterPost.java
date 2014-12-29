package com.pike.litnep.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pike.litnep.R;
import com.pike.litnep.app.AppController;
import com.pike.litnep.model.Post;
import com.pike.litnep.util.GeneralFunctions;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapterPost extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Post> postItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapterPost(Context context, ArrayList<Post> postItems) {
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
			convertView = inflater.inflate(R.layout.list_item_custom_post,
					parent, false);
			imageLoader = AppController.getInstance().getImageLoader();
		}

		// ImageView thumbnail = (ImageView) convertView
		// .findViewById(R.id.imgThumbnail);

		NetworkImageView thumbnail = (NetworkImageView) convertView
				.findViewById(R.id.imgThumbnail);
		TextView userName = (TextView) convertView
				.findViewById(R.id.tvUserName);
		TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView createdAt = (TextView) convertView
				.findViewById(R.id.tvCreatedAt);
		TextView contents = (TextView) convertView
				.findViewById(R.id.tvContents);
		TextView likeValue = (TextView) convertView
				.findViewById(R.id.tvLikeValue);

		// getting post data for the row
		Post p = (Post) postItems.get(position);

		// Log.d("TAG", p.getUserName());
		// Log.d("TAG", p.getTitle());
		// Log.d("TAG", p.getContents());

		String strfirstName = p.getfirstName();
		String strlastName = p.getlastName();
		String strThumbnailUrl = p.getThumbnailUrl();
		String strTitle = p.getTitle();
		String strContent = p.getContent();
		String strCreatedAt = p.getCreatedAt();
		int intLikeValue = p.getLikeValue();

		// parsing date to suitable format
		strCreatedAt = GeneralFunctions.getInstance().dateParser(strCreatedAt,
				"MMM dd");

		userName.setText(strfirstName + " " + strlastName);
		title.setText(strTitle);
		contents.setText(strContent);
		createdAt.setText(strCreatedAt);
		thumbnail.setImageUrl(strThumbnailUrl, imageLoader);
		likeValue.setText((intLikeValue != 0) ? String.valueOf(intLikeValue)
				: "");

		return convertView;
	}
}
