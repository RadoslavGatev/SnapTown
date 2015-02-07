package com.example.snaptown;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.example.snaptown.TownsActivity.MySimpleArrayAdapter;
import com.example.snaptown.apiclients.MediaClient;
import com.example.snaptown.apiclients.TownsClient;
import com.example.snaptown.apiclients.UserClient;
import com.example.snaptown.models.Media;
import com.example.snaptown.models.Town;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ViewTownActivity extends Activity {

	public static final String EXTRA_TOWN_ID = "com.example.snaptown.townId";
	public static final String EXTRA_TOWN_NAME = "com.example.snaptown.townName";

	private Button newsFeedButton;
	private TextView townNameTextView;
	private ProgressBar loadProgress;
	private ListView townFeed;
	private int currentTownId;

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
	}

	@Override
	protected void onResume() {
		super.onResume();

		new LoadMediaTask().execute(currentTownId);
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

	static class ViewHolder {
		TextView userTextView;
		ImageView mediaImage;
		TextView descriptionTextView;
	}

	public class MediaArrayAdapter extends ArrayAdapter<Media> {
		private final Context context;
		private final ArrayList<Media> values;
		private final Hashtable<Integer, Bitmap> images;

		public MediaArrayAdapter(Context context, ArrayList<Media> values) {
			super(context, 0, values);
			this.context = context;
			this.values = values;
			images = new Hashtable<Integer, Bitmap>();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {

				// inflate the layout
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(
						R.layout.town_media_dropdown_item, parent, false);

				// well set up the ViewHolder
				viewHolder = new ViewHolder();

				viewHolder.userTextView = (TextView) convertView
						.findViewById(R.id.user_textview);
				viewHolder.mediaImage = (ImageView) convertView
						.findViewById(R.id.media_imageview);
				viewHolder.descriptionTextView = (TextView) convertView
						.findViewById(R.id.description_textview);
				// store
				convertView.setTag(viewHolder);
			} else {
				// just use the viewHolder
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// object item based on the position
			Media currentTown = values.get(position);
			viewHolder.userTextView.setText(currentTown.uploadedBy);
			viewHolder.descriptionTextView.setText(currentTown.description);
			Bitmap bitmap = images.get(currentTown.mediaId);

			if (bitmap == null) {
				viewHolder.mediaImage.setImageResource(R.drawable.blank);
				new LoadPhotoTask(images, currentTown.mediaId, viewHolder)
						.execute(currentTown.mediaId);
			} else {
				viewHolder.mediaImage.setImageBitmap(bitmap);
			}

			return convertView;
		}
	}

	private class LoadMediaTask extends AsyncTask<Integer, Void, List<Media>> {
		protected void onPreExecute() {
			loadProgress.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(List<Media> result) {
			townFeed.setAdapter(new MediaArrayAdapter(ViewTownActivity.this,
					(ArrayList<Media>) result));

			loadProgress.setVisibility(View.GONE);
		}

		@Override
		protected List<Media> doInBackground(Integer... params) {
			List<Media> media = MediaClient.getMediaForTown(params[0],
					UserClient.currentUser.getAuthToken());
			return media;
		}
	}

	private class LoadPhotoTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<Hashtable<Integer, Bitmap>> bitmapsReference;
		private final ViewHolder viewHolder;
		private final int mediaId;

		public LoadPhotoTask(Hashtable<Integer, Bitmap> bitmaps, int mediaId,
				ViewHolder viewHolder) {
			this.bitmapsReference = new WeakReference<Hashtable<Integer, Bitmap>>(
					bitmaps);
			this.mediaId = mediaId;
			this.viewHolder = viewHolder;
		}

		protected void onPostExecute(Bitmap result) {
			if (bitmapsReference != null) {
				Hashtable<Integer, Bitmap> bitmaps = bitmapsReference.get();
				if (bitmaps != null) {
					bitmaps.put(mediaId, result);
					if (viewHolder != null) {
						viewHolder.mediaImage.setImageBitmap(result);
					}
				}
			}
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap media = MediaClient.getPhoto(params[0], 400, 400);
			return media;
		}
	}
}
