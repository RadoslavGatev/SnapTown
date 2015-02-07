package com.example.snaptown.adapters;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.snaptown.R;
import com.example.snaptown.models.Media;
import com.example.snaptown.utilities.DisplayMediaViewHolder;
import com.example.snaptown.utilities.LoadPhotoTask;

public class MediaArrayAdapter extends ArrayAdapter<Media> {

	private ArrayList<Media> values;
	private final Context context;
	private final Hashtable<Integer, Bitmap> images;

	public MediaArrayAdapter(Context context, ArrayList<Media> values) {
		super(context, 0, values);
		this.context = context;
		this.values = values;
		images = new Hashtable<Integer, Bitmap>();
	}

	public void setDataSet(ArrayList<Media> mediaData) {
		this.values.addAll(mediaData);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DisplayMediaViewHolder viewHolder;
		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.town_media_dropdown_item,
					parent, false);

			// well set up the ViewHolder
			viewHolder = new DisplayMediaViewHolder(convertView);

			// store
			convertView.setTag(viewHolder);
		} else {
			// just use the viewHolder
			viewHolder = (DisplayMediaViewHolder) convertView.getTag();
		}
		
		viewHolder.mediaImage.setImageResource(R.drawable.blank);

		// object item based on the position
		Media currentTown = values.get(position);
		viewHolder.userTextView.setText(currentTown.uploadedBy);
		viewHolder.descriptionTextView.setText(currentTown.description);
		Bitmap bitmap = images.get(currentTown.mediaId);

		if (bitmap == null) {
			//viewHolder.mediaImage.setImageResource(R.drawable.blank);
			new LoadPhotoTask(images, currentTown.mediaId, viewHolder)
					.execute(currentTown.mediaId);
		} else {
			viewHolder.mediaImage.setImageBitmap(bitmap);
		}

		return convertView;
	}
}
