package com.example.snaptown.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.snaptown.R;
import com.example.snaptown.ViewTownActivity;
import com.example.snaptown.apiclients.TownsClient;
import com.example.snaptown.apiclients.UserClient;
import com.example.snaptown.models.Town;

public class TownsAdapter extends ArrayAdapter<Town> {
	private static class TownViewHolder {
		public TextView townView;
		public ToggleButton subscribeButton;

		public TownViewHolder(View rowView) {
			townView = (TextView) rowView.findViewById(R.id.town_name_textview);
			subscribeButton = (ToggleButton) rowView
					.findViewById(R.id.subscribe_togglebutton);
		}
	}

	private final Context context;
	private final ArrayList<Town> values;

	public TownsAdapter(Context context, ArrayList<Town> values) {
		super(context, 0, values);
		this.context = context;
		this.values = values;
	}

	public void setDataSet(ArrayList<Town> towns) {
		values.clear();
		values.addAll(towns);
		this.notifyDataSetInvalidated();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TownViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.town_dropdown_item_toggle,
					parent, false);

			viewHolder = new TownViewHolder(convertView);
			// store
			convertView.setTag(viewHolder);
		} else {
			// just use the viewHolder
			viewHolder = (TownViewHolder) convertView.getTag();
		}

		final Town currentTown = values.get(position);

		viewHolder.subscribeButton.setChecked(currentTown.isSubscribed);
		viewHolder.subscribeButton
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

		convertView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent startViewTownActivity = new Intent(context,
						ViewTownActivity.class);
				startViewTownActivity.putExtra(ViewTownActivity.EXTRA_TOWN_ID,
						currentTown.townId);
				startViewTownActivity.putExtra(
						ViewTownActivity.EXTRA_TOWN_NAME, currentTown.name);

				context.startActivity(startViewTownActivity);
			}
		});

		viewHolder.townView.setText(currentTown.name);
		return convertView;
	}
}
