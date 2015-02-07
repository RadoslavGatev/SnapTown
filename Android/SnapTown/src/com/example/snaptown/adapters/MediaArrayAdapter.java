package com.example.snaptown.adapters;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.snaptown.R;
import com.example.snaptown.ViewTownActivity;
import com.example.snaptown.models.Media;
import com.example.snaptown.utilities.DisplayMediaViewHolder;
import com.example.snaptown.utilities.LoadPhotoTask;

public class MediaArrayAdapter extends ArrayAdapter<Media> {

	private ArrayList<Media> values;
	private final Context context;
	private final Hashtable<Integer, Bitmap> images;
	private boolean isNewsFeed = false;

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
		final Media currentTown = values.get(position);
		if (this.isNewsFeed) {
			viewHolder.userTextView.setText(currentTown.uploadedBy
					+ " uploaded in " + currentTown.townName);
			convertView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent startViewTownActivity = new Intent(context,
							ViewTownActivity.class);
					startViewTownActivity.putExtra(
							ViewTownActivity.EXTRA_TOWN_ID, currentTown.townId);
					startViewTownActivity.putExtra(
							ViewTownActivity.EXTRA_TOWN_NAME,
							currentTown.townName);
					context.startActivity(startViewTownActivity);
				}
			});
		} else {
			viewHolder.userTextView.setText(currentTown.uploadedBy);
		}
		if (currentTown.uploadedOn != null) {
			viewHolder.datePostedTextView.setText(DateFormat.format(
					"dd MMM yyyy HH:mm", currentTown.uploadedOn));
		}
		
		viewHolder.descriptionTextView.setText(currentTown.description);
		Bitmap bitmap = images.get(currentTown.mediaId);

		if (bitmap == null) {
			// viewHolder.mediaImage.setImageResource(R.drawable.blank);
			new LoadPhotoTask(images, currentTown.mediaId, viewHolder)
					.execute(currentTown.mediaId);
		} else {
			viewHolder.mediaImage.setImageBitmap(bitmap);
		}

		return convertView;
	}

	public void isNewsFeed(boolean isNewsFeed) {
		this.isNewsFeed = isNewsFeed;
	}
}
