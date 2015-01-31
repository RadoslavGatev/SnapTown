package com.example.snaptown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class PreviewActivity extends Activity {

	public static final String EXTRA_CAPTURED_DATA = "com.example.snaptown.CapturedData";
	public static final String EXTRA_IS_IMAGE = "com.example.snaptown.IsImage";
	
	private ImageView capturedImageView;
	private VideoView capturedVideoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		
		Bundle extras = getIntent().getExtras();
		
		capturedImageView = (ImageView) findViewById(R.id.captured_image_view);
		capturedVideoView = (VideoView)findViewById(R.id.captured_video_view);
		
		if (extras.getBoolean(EXTRA_IS_IMAGE)) {
			Bitmap imageBitmap = (Bitmap)extras.get(EXTRA_CAPTURED_DATA);
			capturedImageView.setImageBitmap(imageBitmap);
			capturedImageView.setVisibility(View.VISIBLE);
			capturedVideoView.setVisibility(View.GONE);
		}
		else {
			Uri videoUri = (Uri)extras.get(EXTRA_CAPTURED_DATA);
			capturedVideoView.setVideoURI(videoUri);
			capturedImageView.setVisibility(View.GONE);
			capturedVideoView.setVisibility(View.VISIBLE);
			capturedVideoView.setMediaController(new MediaController(this));
			capturedVideoView.requestFocus();
		}
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
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		this.finish();
	}
}
