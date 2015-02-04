package com.example.snaptown.apiclients;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.snaptown.helpers.ApiHelper;
import com.example.snaptown.models.Town;

public class TownsClient {

	public static final String AutoCompleteRoute = "autocomplete/towns/%s?$top=%d";
	public static final String TownsRoute = "towns/%s";

	public static List<Town> getAutocomplete(String query, int maxResults) {
		String routePath = String.format(AutoCompleteRoute, query, maxResults);

		String result = ApiHelper.get(routePath);
		return parseTowns(result);
	}

	public static List<Town> getSubscribed(String query, String authToken) {
		String routePath = String.format(TownsRoute, query);

		String result = ApiHelper.get(routePath);
		return parseTowns(result);
	}

	private static List<Town> parseTowns(String json) {
		ArrayList<Town> towns = new ArrayList<Town>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject entry = (JSONObject) jsonArray.get(i);

				Town town = new Town(entry.getInt("TownID"),
						entry.getString("Name"), entry.getString("Country"));
				towns.add(town);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return towns;
	}

}
