package com.example.snaptown.utilities;

import com.example.snaptown.MainActivity;
import com.example.snaptown.R;
import com.example.snaptown.ViewTownActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				// sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				// sendNotification("Deleted messages on server: " +
				// extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(extras.getString("msg", "Received message"),
						Integer.parseInt(extras.getString("townId", "0")),
						extras.getString("townName", ""),
						extras.getString("senderName", ""));
				Log.i("GCM", "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg, int townId, String townName,
			String senderName) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = null;
		if (townId != 0) {
			intent = new Intent(this, ViewTownActivity.class);
			intent.putExtra(ViewTownActivity.EXTRA_TOWN_ID, townId);
			intent.putExtra(ViewTownActivity.EXTRA_TOWN_NAME, townName);

		} else {
			intent = new Intent(this, MainActivity.class);
		}
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(townName)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setLights(0xFFFF00FF, 1000, 1000).setAutoCancel(true)
				.setContentText(msg).setSound(alarmSound);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);
	}
}
