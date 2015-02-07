package com.example.snaptown.utilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.snaptown.adapters.MediaArrayAdapter;
import com.example.snaptown.apiclients.MediaClient;
import com.example.snaptown.apiclients.UserClient;
import com.example.snaptown.models.Media;

public class LoadMediaTask extends AsyncTask<Integer, Void, List<Media>> {
	private final WeakReference<ProgressBar> loadProgressReference;
	private final WeakReference<MediaArrayAdapter> adapterReference;

	public LoadMediaTask(ProgressBar loadProgress,
			MediaArrayAdapter arrayAdapter) {
		loadProgressReference = new WeakReference<ProgressBar>(loadProgress);
		adapterReference = new WeakReference<MediaArrayAdapter>(arrayAdapter);
	}

	protected void onPreExecute() {
		ProgressBar loadProgress = loadProgressReference.get();
		if (loadProgress != null) {
			loadProgress.setVisibility(View.VISIBLE);
		}
	}

	protected void onPostExecute(List<Media> result) {
		MediaArrayAdapter adapter = adapterReference.get();
		if (adapter != null) {
			adapter.setDataSet((ArrayList<Media>) result);
		}

		ProgressBar loadProgress = loadProgressReference.get();
		if (loadProgress != null) {
			loadProgress.setVisibility(View.GONE);
		}
	}

	@Override
	protected List<Media> doInBackground(Integer... params) {
		Log.d("CurrentUser-LoadMedia", UserClient.currentUser.toString());
		List<Media> media = MediaClient.getMediaForTown(params[0],
				UserClient.currentUser.getAuthToken());
		return media;
	}
}
