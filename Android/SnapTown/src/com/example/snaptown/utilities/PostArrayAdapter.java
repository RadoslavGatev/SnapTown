package com.example.snaptown.utilities;

import java.util.List;

import com.example.snaptown.R;
import com.example.snaptown.models.PostModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostArrayAdapter extends ArrayAdapter<PostModel> {

	public PostArrayAdapter(Context context, List<PostModel> objects) {
		super(context, R.layout.post_list_item, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.post_list_item, parent);
		}

		TextView userNameTextView = (TextView) convertView
				.findViewById(R.id.post_item_user_name);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.post_item_image);
		TextView metaTextView = (TextView) convertView.findViewById(R.id.post_item_meta);
		
		userNameTextView.setText(getItem(position).postedBy);
		imageView.setImageBitmap((Bitmap)getItem(position).data);
		metaTextView.setText(DateFormat.format("dd-MM-yyyy", getItem(position).postedOn));

		return convertView;
	}
}
