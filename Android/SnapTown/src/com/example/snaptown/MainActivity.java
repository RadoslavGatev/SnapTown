package com.example.snaptown;

import com.facebook.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
						 FacebookLoginActivity.class);
				startActivity(intent);
			}

		};
		handler.postDelayed(runnable, 2000);
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
		// Do nothing
	}
}
