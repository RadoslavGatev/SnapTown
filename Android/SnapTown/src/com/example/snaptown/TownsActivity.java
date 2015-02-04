package com.example.snaptown;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snaptown.apiclients.TownsClient;
import com.example.snaptown.models.Town;
import com.facebook.Session;
import com.example.snaptown.R;

public class TownsActivity extends Activity {

	private Button newsFeedButton;
	private Button searchButton;
	private ListView subscribedTownsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_towns);

		/*
		 * final DelayAutoCompleteTextView townAutocomplete =
		 * (DelayAutoCompleteTextView)
		 * findViewById(R.id.town_search_autocomplete);
		 * townAutocomplete.setThreshold(10); townAutocomplete.setAdapter(new
		 * TownsAutoCompleteAdapter(this)); // 'this' is Activity instance
		 * townAutocomplete.setLoadingIndicator( (android.widget.ProgressBar)
		 * findViewById(R.id.pb_loading_indicator));
		 * townAutocomplete.setOnItemClickListener(new
		 * AdapterView.OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> adapterView, View
		 * view, int position, long id) { Town town = (Town)
		 * adapterView.getItemAtPosition(position);
		 * townAutocomplete.setText(town.getName()); } });
		 */
		searchButton = (Button) findViewById(R.id.search_button);
		subscribedTownsListView = (ListView) findViewById(R.id.subscribed_towns_listview);

		/*
		 * searchButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * 
		 * } });
		 */
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
		
		new DownloadFilesTask().execute();
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

	private class DownloadFilesTask extends AsyncTask<Void, Void, List<Town>> {
		protected void onPostExecute(List<Town> result) {
			subscribedTownsListView.setAdapter(new MySimpleArrayAdapter(
					TownsActivity.this, (ArrayList<Town>) result));
		}

		@Override
		protected List<Town> doInBackground(Void... params) {
			List<Town> subscribed = TownsClient.getAutocomplete("go", 10);
			return subscribed;
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
			View rowView = inflater.inflate(
					R.layout.town_dropdown_item_toggle, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.text1);

			textView.setText(values.get(position).name);

			return rowView;
		}
	}

}
