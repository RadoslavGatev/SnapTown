package com.example.snaptown.apiclients;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.snaptown.helpers.ApiHelper;
import com.example.snaptown.models.Town;

public class TownsClient {

	public static final String Route = "autocomplete/towns/%s";

	public static Iterable<Town> getAutocomplete(String query) {
		String routePath = String.format(Route, query);

		String result = ApiHelper.get(routePath);

		ArrayList<Town> towns = new ArrayList<Town>();
		try {
			JSONArray jsonArray = null;
			jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject entry = (JSONObject) jsonArray.get(i);
				
				Town town = new Town(entry.getInt("TownId"),
						entry.getString("Name"), entry.getString("Country"));
				towns.add(town);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		
		return towns;
	}
}