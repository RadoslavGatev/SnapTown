package com.example.snaptown;

import java.io.File;

import com.example.snaptown.apiclients.MediaClient;
import com.example.snaptown.apiclients.MediaClient.ContentType;
import com.example.snaptown.apiclients.TownsClient;
import com.example.snaptown.helpers.CaptureHelper;
import com.example.snaptown.helpers.LocationHelper;
import com.example.snaptown.models.Town;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PreviewActivity extends Activity implements
		LocationHelper.LocListener {

	public static final String EXTRA_IS_IMAGE = "com.example.snaptown.IsImage";
	public static final String EXTRA_FILE_PATH = "com.example.snaptown.FilePath";

	private ImageView capturedImageView;
	private VideoView capturedVideoView;
	private Button postButton;
	private EditText descriptionEditText;
	private EditText locationEditText;
	private ImageButton locationButton;
	private boolean hasLocation = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);

		LocationHelper.addLocListener(this);

		Bundle extras = getIntent().getExtras();

		capturedImageView = (ImageView) findViewById(R.id.captured_image_view);
		capturedVideoView = (VideoView) findViewById(R.id.captured_video_view);
		postButton = (Button) findViewById(R.id.preview_post_button);
		locationEditText = (EditText) findViewById(R.id.location_edit_text);
		locationButton = (ImageButton) findViewById(R.id.location_button);
		descriptionEditText = (EditText) findViewById(R.id.description_edittext);

		locationEditText.setText("");
		hasLocation = false;
		setLocationText(LocationHelper.getLatestLocation(this));

		final boolean isImage = extras.getBoolean(EXTRA_IS_IMAGE);
		final String filePath = extras.getString(EXTRA_FILE_PATH);

		File file = new File(filePath);
		if (file.exists()) {
			if (isImage) {
				try {
					Bitmap imageBitmap = CaptureHelper.rotateFile(file
							.getAbsolutePath());

					capturedImageView.setImageBitmap(imageBitmap);
					capturedImageView.setVisibility(View.VISIBLE);
					capturedVideoView.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				Uri videoUri = Uri.fromFile(file);
				capturedVideoView.setVideoURI(videoUri);
				capturedImageView.setVisibility(View.GONE);
				capturedVideoView.setVisibility(View.VISIBLE);
				capturedVideoView.setMediaController(new MediaController(this));
				capturedVideoView.requestFocus();
			}
		}

		postButton.setEnabled(false);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Create Post
				if (filePath != null) {
					ContentType type = (isImage ? ContentType.JPEG
							: ContentType.MP4);
					String description = descriptionEditText.getText()
							.toString();
					MediaClient.uploadFile(filePath, type, description);
					Toast.makeText(
							PreviewActivity.this.getApplicationContext(),
							"Your post was successful", Toast.LENGTH_SHORT)
							.show();
					onBackPressed();
				}
			}
		});

		locationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hasLocation = false;
				setLocationText(LocationHelper
						.getLatestLocation(PreviewActivity.this));
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
		LocationHelper.stopListening();
		Intent intent = new Intent(this, NewsFeedActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		this.finish();
	}

	private void setLocationText(Location loc) {
		if (loc != null && !hasLocation) {
			hasLocation = true;
			double latitude = loc.getLatitude();
			double longitude = loc.getLongitude();
			new GetTownTask().execute(latitude, longitude);
		}
	}

	@Override
	public void locationChanged(Location loc) {
		setLocationText(loc);
	}

	private class GetTownTask extends AsyncTask<Double, Void, Town> {
		@Override
		protected Town doInBackground(Double... params) {
			return TownsClient.getTownOnLocation(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Town town) {
			if (town != null && locationEditText != null && postButton != null) {
				locationEditText.setText(town.getName());
				MediaClient.TownId = town.townId;
				postButton.setEnabled(true);
			}
		}
	}
}
