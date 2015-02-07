package com.example.snaptown.utilities;

import java.lang.ref.WeakReference;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.snaptown.apiclients.MediaClient;

public class LoadPhotoTask extends AsyncTask<Integer, Void, Bitmap> {
	private final WeakReference<Hashtable<Integer, Bitmap>> bitmapsReference;
	private final DisplayMediaViewHolder viewHolder;
	private final int mediaId;

	public LoadPhotoTask(Hashtable<Integer, Bitmap> bitmaps, int mediaId,
			DisplayMediaViewHolder viewHolder) {
		this.bitmapsReference = new WeakReference<Hashtable<Integer, Bitmap>>(
				bitmaps);
		this.mediaId = mediaId;
		this.viewHolder = viewHolder;
	}

	protected void onPostExecute(Bitmap result) {
		if (bitmapsReference != null) {
			Hashtable<Integer, Bitmap> bitmaps = bitmapsReference.get();
			if (bitmaps != null && result != null) {
				bitmaps.put(mediaId, result);
				if (viewHolder != null) {
					viewHolder.mediaImage.setImageBitmap(result);
				}
			}
		}
	}

	@Override
	protected Bitmap doInBackground(Integer... params) {
		Bitmap media = MediaClient.getPhoto(params[0], 400, 400);
		return media;
	}
}
