package com.example.snaptown;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.snaptown.apiclients.TownsClient;
import com.example.snaptown.apiclients.UserClient;
import com.example.snaptown.models.Town;
import com.facebook.Session;
import com.example.snaptown.R;

public class TownsActivity extends Activity {

	private Button newsFeedButton;
	private Button searchButton;
	private ListView subscribedTownsListView;
	private EditText searchTownEditText;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_towns);

		searchButton = (Button) findViewById(R.id.search_button);
		subscribedTownsListView = (ListView) findViewById(R.id.subscribed_towns_listview);
		searchTownEditText = (EditText) findViewById(R.id.town_search_edittext);
		progressBar = (ProgressBar) findViewById(R.id.loading_indicator_progressbar);

		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String query = searchTownEditText.getText().toString();
				if (!query.isEmpty()) {
					new SearchTownTask().execute(query);
				} else {
					new GetSubscibedListTask().execute();
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
			new GetSubscibedListTask().execute();
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

	private class GetSubscibedListTask extends
			AsyncTask<Void, Void, List<Town>> {
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(List<Town> result) {
			subscribedTownsListView.setAdapter(new MySimpleArrayAdapter(
					TownsActivity.this, (ArrayList<Town>) result));

			progressBar.setVisibility(View.GONE);
		}

		@Override
		protected List<Town> doInBackground(Void... params) {
			List<Town> subscribed = TownsClient.getAllSubscriptions("123");
			return subscribed;
		}
	}

	private class SearchTownTask extends AsyncTask<String, Void, List<Town>> {
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(List<Town> result) {
			subscribedTownsListView.setAdapter(new MySimpleArrayAdapter(
					TownsActivity.this, (ArrayList<Town>) result));

			progressBar.setVisibility(View.GONE);
		}

		@Override
		protected List<Town> doInBackground(String... params) {
			List<Town> foundTowns = TownsClient.getAutocomplete(params[0], 10,
					UserClient.currentUser.getAuthToken());
			return foundTowns;
		}
	}

	public class MySimpleArrayAdapter extends ArrayAdapter<Town> {
		private final Context context;
		private final ArrayList<Town> values;

		public MySimpleArrayAdapter(Context context, ArrayList<Town> values) {
			super(context, 0, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.town_dropdown_item_toggle,
					parent, false);
			TextView textView = (TextView) rowView
					.findViewById(R.id.town_name_textview);
			ToggleButton subscribeButton = (ToggleButton) rowView
					.findViewById(R.id.subscribe_togglebutton);
			final Town currentTown = values.get(position);

			subscribeButton.setChecked(currentTown.isSubscribed);
			subscribeButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								final boolean isChecked) {

							new Thread(new Runnable() {
								public void run() {
									if (isChecked) {
										TownsClient.subscribeForTown(
												currentTown.townId,
												UserClient.currentUser
														.getAuthToken());
									} else {
										TownsClient.unsubscribeForTown(
												currentTown.townId,
												UserClient.currentUser
														.getAuthToken());
									}
								}
							}).start();
						}
					});

			rowView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent startViewTownActivity = new Intent(
							TownsActivity.this, ViewTownActivity.class);
					startViewTownActivity
							.putExtra("townId", currentTown.townId);
					startViewTownActivity
							.putExtra("townName", currentTown.name);

					startActivity(startViewTownActivity);
				}
			});

			textView.setText(currentTown.name);

			rowView.setTag(currentTown);
			return rowView;
		}
	}

}
