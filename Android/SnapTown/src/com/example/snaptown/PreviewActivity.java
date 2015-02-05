package com.example.snaptown;

import java.io.File;

import com.example.snaptown.apiclients.MediaClient;
import com.example.snaptown.apiclients.MediaClient.ContentType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class PreviewActivity extends Activity {

	public static final String EXTRA_IS_IMAGE = "com.example.snaptown.IsImage";
	public static final String EXTRA_FILE_PATH = "com.example.snaptown.FilePath";

	private ImageView capturedImageView;
	private VideoView capturedVideoView;
	private Button postButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);

		Bundle extras = getIntent().getExtras();

		capturedImageView = (ImageView) findViewById(R.id.captured_image_view);
		capturedVideoView = (VideoView) findViewById(R.id.captured_video_view);
		postButton = (Button) findViewById(R.id.preview_post_button);

		final boolean isImage = extras.getBoolean(EXTRA_IS_IMAGE);
		final String filePath = extras.getString(EXTRA_FILE_PATH);

		File file = new File(filePath);
		if (file.exists()) {
			if (isImage) {
				Bitmap imageBitmap = BitmapFactory.decodeFile(file
						.getAbsolutePath());
				capturedImageView.setImageBitmap(imageBitmap);
				capturedImageView.setVisibility(View.VISIBLE);
				capturedVideoView.setVisibility(View.GONE);
			} else {
				Uri videoUri = Uri.fromFile(file);
				capturedVideoView.setVideoURI(videoUri);
				capturedImageView.setVisibility(View.GONE);
				capturedVideoView.setVisibility(View.VISIBLE);
				capturedVideoView.setMediaController(new MediaController(this));
				capturedVideoView.requestFocus();
			}
		}
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Create Post
				if (filePath != null) {
					ContentType type = (isImage ? ContentType.JPEG
							: ContentType.MP4);
					MediaClient.uploadFile(filePath, type);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, NewsFeedActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		this.finish();
	}
}
