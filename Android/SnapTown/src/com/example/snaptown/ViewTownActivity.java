package com.example.snaptown;

import java.util.ArrayList;

import com.example.snaptown.adapters.MediaArrayAdapter;
import com.example.snaptown.models.Media;
import com.example.snaptown.utilities.LoadMediaTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewTownActivity extends Activity {

	public static final String EXTRA_TOWN_ID = "com.example.snaptown.townId";
	public static final String EXTRA_TOWN_NAME = "com.example.snaptown.townName";

	private Button newsFeedButton;
	private TextView townNameTextView;
	private ProgressBar loadProgress;
	private ListView townFeed;
	private int currentTownId;
	private MediaArrayAdapter mediaArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_town);

		newsFeedButton = (Button) findViewById(R.id.news_feed_button);
		newsFeedButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewTownActivity.this,
						NewsFeedActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		townNameTextView = (TextView) findViewById(R.id.town_name_textview);
		loadProgress = (ProgressBar) findViewById(R.id.load_progress);
		townFeed = (ListView) findViewById(R.id.townFeed_listView);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.currentTownId = extras.getInt(EXTRA_TOWN_ID);
			String name = extras.getString(EXTRA_TOWN_NAME);

			townNameTextView.setText(name);
		}

		mediaArrayAdapter = new MediaArrayAdapter(ViewTownActivity.this,
				new ArrayList<Media>());
		townFeed.setAdapter(mediaArrayAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

		new LoadMediaTask(loadProgress, mediaArrayAdapter)
				.execute(currentTownId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_town, menu);
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

}
