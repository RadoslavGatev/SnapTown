package com.example.snaptown.utilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.snaptown.adapters.TownsAdapter;
import com.example.snaptown.apiclients.TownsClient;
import com.example.snaptown.apiclients.UserClient;
import com.example.snaptown.models.Town;

public class GetSubscribedListTask extends AsyncTask<Void, Void, List<Town>> {
	private WeakReference<ProgressBar> progressBarReference;
	private WeakReference<TownsAdapter> townsAdapterReference;

	public GetSubscribedListTask(ProgressBar progressBar,
			TownsAdapter townsAdapter) {
		progressBarReference = new WeakReference<ProgressBar>(progressBar);
		townsAdapterReference = new WeakReference<TownsAdapter>(townsAdapter);
	}

	protected void onPreExecute() {
		ProgressBar progressBar = progressBarReference.get();
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	protected void onPostExecute(List<Town> result) {
		TownsAdapter townsAdapter = townsAdapterReference.get();
		if (townsAdapter != null) {
			townsAdapter.setDataSet((ArrayList<Town>) result);
		}

		ProgressBar progressBar = progressBarReference.get();
		if (progressBar != null) {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	protected List<Town> doInBackground(Void... params) {
		List<Town> subscribed = TownsClient
				.getAllSubscriptions(UserClient.currentUser.getAuthToken());
		return subscribed;
	}
}
