package com.example.snaptown;

import com.example.snaptown.controls.CaptureControlsView;
import com.example.snaptown.helpers.CaptureHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Handler handler;
	private Runnable runnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(MainActivity.this,
						NewsFeedActivity.class);
				startActivity(intent);
			}

		};
		handler.postDelayed(runnable, 5000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}
	}
	
	@Override
	public void onBackPressed() {
	}
}
