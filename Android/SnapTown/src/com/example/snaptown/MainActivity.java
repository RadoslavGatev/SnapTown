package com.example.snaptown;

import com.example.snaptown.helpers.GcmHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends Activity {

	private Handler handler;
	private Runnable runnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Check device for Play Services APK.
	    if (GcmHelper.checkPlayServices(this)) {
	        // If this check succeeds, proceed with normal processing.
	        // Otherwise, prompt user to get valid Play Services APK.
	    	GcmHelper.registerDevice(this);
	    	
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
	    else {
	    	Log.i("GCM", "No valid Google Play Services APK found.");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		GcmHelper.checkPlayServices(this);
	}
	
	@Override
	public void onBackPressed() {
		// Do nothing
	}
}
