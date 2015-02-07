package com.example.snaptown;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.snaptown.adapters.TownsAdapter;
import com.example.snaptown.models.Town;
import com.example.snaptown.utilities.GetSubscribedListTask;
import com.example.snaptown.utilities.SearchTownTask;
import com.facebook.Session;
import com.example.snaptown.R;

public class TownsActivity extends Activity {

	private Button newsFeedButton;
	private Button searchButton;
	private ListView subscribedTownsListView;
	private EditText searchTownEditText;
	private ProgressBar progressBar;
	private TownsAdapter townsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_towns);

		searchButton = (Button) findViewById(R.id.search_button);
		subscribedTownsListView = (ListView) findViewById(R.id.subscribed_towns_listview);
		searchTownEditText = (EditText) findViewById(R.id.town_search_edittext);
		progressBar = (ProgressBar) findViewById(R.id.loading_indicator_progressbar);

		townsAdapter = new TownsAdapter(this, new ArrayList<Town>());
		subscribedTownsListView.setAdapter(townsAdapter);

		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String query = searchTownEditText.getText().toString();
				if (!query.isEmpty()) {
					new SearchTownTask(progressBar, townsAdapter)
							.execute(query);
				} else {
					new GetSubscribedListTask(progressBar, townsAdapter)
							.execute();
				}

			}
		});

		newsFeedButton = (Button) findViewById(R.id.news_feed_button);
		newsFeedButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TownsActivity.this,
						NewsFeedActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (searchTownEditText.getText().toString().isEmpty()) {
			new GetSubscribedListTask(progressBar, townsAdapter).execute();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		overridePendingTransition(0, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.towns, menu);
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
			Intent intent = new Intent(TownsActivity.this,
					FacebookLoginActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
