package com.example.snaptown.helpers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.snaptown.PreviewActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class CaptureHelper {

	// For scaling images
	// http://developer.android.com/training/camera/photobasics.html#TaskScalePhoto

	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

	private static String currentImageFilePath = null;
	private static String currentVideoFilePath = null;

	public static OnClickListener getImageOnClickListener(
			final Activity activity) {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (intent.resolveActivity(activity.getPackageManager()) != null) {
					File photoFile = null;
					currentImageFilePath = null;
					try {
						photoFile = createImageFile(activity);
					} catch (IOException ex) {
						// Error occurred while creating the File
					}
					if (photoFile != null) {
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(photoFile));
						activity.startActivityForResult(intent,
								CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
					}
				}
			}
		};

		return listener;
	}

	public static OnClickListener getVideoOnClickListener(
			final Activity activity) {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				if (intent.resolveActivity(activity.getPackageManager()) != null) {
					File videoFile = null;
					currentVideoFilePath = null;
					try {
						videoFile = createVideoFile(activity);
					} catch (Exception e) {
						// TODO: handle exception
					}
					if (videoFile != null) {
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(videoFile));
						activity.startActivityForResult(intent,
								CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
					}
				}
			}
		};

		return listener;
	}

	public static void handleImageCaptured(Context context, int resultCode,
			Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(context, PreviewActivity.class);
			intent.putExtra(PreviewActivity.EXTRA_IS_IMAGE, true);
			intent.putExtra(PreviewActivity.EXTRA_FILE_PATH,
					currentImageFilePath);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			// User cancelled
		} else {
			// Image capture failed
			Toast.makeText(context, "Image capture failed.", Toast.LENGTH_LONG)
					.show();
		}
	}

	public static void handleVideoCaptured(Context context, int resultCode,
			Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(context, PreviewActivity.class);
			intent.putExtra(PreviewActivity.EXTRA_IS_IMAGE, false);
			intent.putExtra(PreviewActivity.EXTRA_FILE_PATH,
					currentVideoFilePath);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			// User cancelled
		} else {
			// Video capture failed
			Toast.makeText(context, "Video capture failed.", Toast.LENGTH_LONG)
					.show();
		}
	}

	private static File createImageFile(Context context) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp;
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);

		currentImageFilePath = image.getAbsolutePath();
		return image;
	}

	private static File createVideoFile(Context context) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		String videoFileName = "MP4_" + timeStamp;
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		File video = File.createTempFile(videoFileName, ".mp4", storageDir);

		currentVideoFilePath = video.getAbsolutePath();
		return video;
	}
}
