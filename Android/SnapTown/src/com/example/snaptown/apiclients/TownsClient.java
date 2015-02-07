package com.example.snaptown.apiclients;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.snaptown.helpers.ApiHelper;
import com.example.snaptown.models.Town;

public class TownsClient {

	public static final String AutoCompleteRoute = "autocomplete/towns/%s?$top=%d&authToken=%s";
	public static final String SubscribedTownsRoute = "towns/?authToken=%s";
	public static final String SubscribeRoute = "towns/%d/subscribe?authToken=%s";
	public static final String LocationRoute = "towns/location/%s;%s/";

	public static List<Town> getAutocomplete(String query, int maxResults,
			String authToken) {
		String routePath = String.format(AutoCompleteRoute, query, maxResults,
				authToken);

		String result = ApiHelper.get(routePath);
		return parseTowns(result);
	}

	public static List<Town> getAllSubscriptions(String authToken) {
		String routePath = String.format(SubscribedTownsRoute, authToken);

		String result = ApiHelper.get(routePath);
		return parseTowns(result);
	}

	public static void subscribeForTown(int townId, String authToken) {
		String routePath = String.format(SubscribeRoute, townId, authToken);

		ApiHelper.post(routePath);
	}

	public static void unsubscribeForTown(int townId, String authToken) {
		String routePath = String.format(SubscribeRoute, townId, authToken);

		ApiHelper.delete(routePath);
	}

	public static Town getTownOnLocation(double latitude, double longitude){
		String result = ApiHelper.get(String.format(LocationRoute, latitude, longitude));
		
		try {
			JSONObject entry = new JSONObject(result);
			Town town = new Town(entry.getInt("TownID"),
					entry.getString("Name"), entry.getString("Country"),
					entry.optBoolean("IsSubscribed"));
			
			return town;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private static List<Town> parseTowns(String json) {
		ArrayList<Town> towns = new ArrayList<Town>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject entry = (JSONObject) jsonArray.get(i);

				Town town = new Town(entry.getInt("TownID"),
						entry.getString("Name"), entry.getString("Country"),
						entry.getBoolean("IsSubscribed"));
				towns.add(town);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return towns;
	}

}
