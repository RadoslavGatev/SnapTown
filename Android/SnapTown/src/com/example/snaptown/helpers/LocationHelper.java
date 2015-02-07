package com.example.snaptown.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

	public interface LocListener{
		void locationChanged(Location loc);
	}
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	private static LocationManager locManager;
	private static Location mLastLocation;
	private static List<LocListener> locationListeners;

	public static void initLocationHelper(Context context) {
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public static void startListening() {
		if (locManager != null) {
			if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						0, 0, listener);
			}
			if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0, listener);
			}
		}
	}

	public static void stopListening() {
		if (locManager != null) {
			locManager.removeUpdates(listener);
		}
	}

	public static Location getLatestLocation(Context context){
		if (mLastLocation != null) {
			return mLastLocation;
		}
		
		if (locManager == null) {
			initLocationHelper(context);
		}
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mLastLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		else if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			mLastLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		return mLastLocation;
	}
	
	public static void addLocListener(LocListener listener){
		if (locationListeners == null) {
			locationListeners = new ArrayList<LocListener>();
		}
		locationListeners.add(listener);
	}
	
	private static void notifyLocListeners(Location location){
		if (locationListeners != null) {
			for (LocListener listener : locationListeners) {
				if (listener != null) {
					listener.locationChanged(location);
				}
			}
		}
	}
	
	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	private static boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	private static LocationListener listener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			if (mLastLocation == null) {
				mLastLocation = location;
				notifyLocListeners(mLastLocation);
			} else {
				if (isBetterLocation(location, mLastLocation)) {
					mLastLocation = location;
					notifyLocListeners(mLastLocation);
				}
			}
		}
	};

}
