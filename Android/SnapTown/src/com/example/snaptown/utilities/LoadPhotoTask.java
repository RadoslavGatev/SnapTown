package com.example.snaptown.utilities;

import java.lang.ref.WeakReference;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.snaptown.apiclients.MediaClient;
import com.example.snaptown.helpers.CaptureHelper;

public class LoadPhotoTask extends AsyncTask<Integer, Void, Bitmap> {
	private final WeakReference<Hashtable<Integer, Bitmap>> bitmapsReference;
	private final DisplayMediaViewHolder viewHolder;
	private final int mediaId;
	private final Context context;

	public LoadPhotoTask(Hashtable<Integer, Bitmap> bitmaps, int mediaId,
			DisplayMediaViewHolder viewHolder, Context context) {
		this.bitmapsReference = new WeakReference<Hashtable<Integer, Bitmap>>(
				bitmaps);
		this.mediaId = mediaId;
		this.viewHolder = viewHolder;
		this.context = context;
	}

	protected void onPostExecute(Bitmap result) {
		if (bitmapsReference != null) {
			Hashtable<Integer, Bitmap> bitmaps = bitmapsReference.get();
			if (bitmaps != null && result != null) {
				String filePath = CaptureHelper.saveBitmapToFile(result, context);
				Bitmap bmp = CaptureHelper.rotateFile(filePath);
				bitmaps.put(mediaId, bmp);
				if (viewHolder != null) {
					viewHolder.mediaImage.setImageBitmap(bmp);
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
