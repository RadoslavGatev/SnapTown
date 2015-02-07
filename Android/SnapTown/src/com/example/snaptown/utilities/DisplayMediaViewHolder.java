package com.example.snaptown.utilities;

import com.example.snaptown.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMediaViewHolder {
	public DisplayMediaViewHolder(View convertView) {
		this.userTextView = (TextView) convertView
				.findViewById(R.id.user_textview);
		this.mediaImage = (ImageView) convertView
				.findViewById(R.id.media_imageview);
		this.descriptionTextView = (TextView) convertView
				.findViewById(R.id.description_textview);
		this.datePostedTextView = (TextView) convertView
				.findViewById(R.id.date_posted_textview);
	}

	public TextView userTextView;
	public ImageView mediaImage;
	public TextView descriptionTextView;
	public TextView datePostedTextView;
}