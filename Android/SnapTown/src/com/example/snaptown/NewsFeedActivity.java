package com.example.snaptown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.snaptown.controls.CaptureControlsView;
import com.example.snaptown.helpers.CaptureHelper;
import com.facebook.Session;

public class NewsFeedActivity extends Activity {

	private Button townsButton;
	private CaptureControlsView captureControls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);

		townsButton = (Button) findViewById(R.id.towns_button);
		townsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewsFeedActivity.this,
						TownsActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		captureControls = (CaptureControlsView) findViewById(R.id.capture_controls);
		captureControls.setPictureButtonOnClickListener(CaptureHelper
				.getImageOnClickListener(this));
		captureControls.setVideoButtonOnClickListener(CaptureHelper
				.getVideoOnClickListener(this));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		overridePendingTransition(0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_feed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_logout:
			Session session = Session.getActiveSession();
			if (session != null) {
				if (!session.isClosed()) {
					session.closeAndClearTokenInformation();
				}
			} else {
				session = new Session(this);
				Session.setActiveSession(session);
				session.closeAndClearTokenInformation();
			}
			Intent intent = new Intent(NewsFeedActivity.this,
					FacebookLoginActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case CaptureHelper.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			CaptureHelper.handleImageCaptured(this, resultCode, data);
			break;
		case CaptureHelper.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
			CaptureHelper.handleVideoCaptured(this, resultCode, data);
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}
}
