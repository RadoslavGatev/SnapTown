package com.example.snaptown.apiclients;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.snaptown.helpers.ApiHelper;
import com.example.snaptown.models.Media;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MediaClient {

	public static enum ContentType {
		JPEG, MP4
	}

	public static int TownId = 0;
	
	private static final String UPLOAD_SERVER_URI = ApiHelper.ApiUrl
			+ "/Media?authToken=%s&townId=%d&description=%s";

	private static final String GetMediaRoute = ApiHelper.ApiUrl + "/Media/%d";
	private static final String MediaByTownRoute = "media/%d?authToken=%s";

	private static String lineEnd = "\r\n";
	private static String twoHyphens = "--";
	private static String boundary = "-------------------------acebdf13572468";
	private static int maxBufferSize = 1 * 1024 * 1024;

	public static void uploadFile(final String sourceFileUri,
			final ContentType type) {
		if (TownId > 0) {
			new Thread(new Runnable() {
				public void run() {
					String fileName = sourceFileUri;
					HttpURLConnection conn = null;
					DataOutputStream dos = null;
					int serverResponseCode = 0;
					File sourceFile = new File(sourceFileUri);

					if (!sourceFile.isFile()) {
						Log.e("uploadFile", "Source File not exist :"
								+ sourceFileUri);
					} else {
						try {
							// open a URL connection to the Servlet
							FileInputStream fileInputStream = new FileInputStream(
									sourceFile);
							URL url = new URL(String.format(UPLOAD_SERVER_URI,
									UserClient.currentUser.getAuthToken(), TownId,
									"uploaded:)"));
							// Open a HTTP connection to the URL
							conn = initConnection(fileName, boundary, url);

							dos = new DataOutputStream(conn.getOutputStream());

							writeToStream(type, sourceFile.getName(), dos,
									fileInputStream);

							// Responses from the server (code and message)
							serverResponseCode = conn.getResponseCode();
							String serverResponseMessage = conn
									.getResponseMessage();

							Log.i("uploadFile", "HTTP Response is : "
									+ serverResponseMessage + ": "
									+ serverResponseCode);

							if (serverResponseCode == 200) {
								// File Upload Completed
							}

							// close the streams //
							fileInputStream.close();
							dos.flush();
							dos.close();

						} catch (MalformedURLException ex) {
							ex.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	private static HttpURLConnection initConnection(String fileName,
			String boundary, URL url) throws IOException, ProtocolException {
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true); // Allow Inputs
		conn.setDoOutput(true); // Allow Outputs
		conn.setUseCaches(false); // Don't use a Cached Copy
		conn.setRequestMethod("POST");
		// conn.setRequestProperty("Connection", "Keep-Alive");
		// conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		// conn.setRequestProperty("uploaded_file", fileName);
		return conn;
	}

	private static void writeToStream(ContentType type, String fileName,
			DataOutputStream dos, FileInputStream fileInputStream)
			throws IOException {
		int bytesRead;
		int bytesAvailable;
		int bufferSize;
		byte[] buffer;
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		String contentDisposition = String.format(
				"Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"",
				"uploadedfile", fileName);
		dos.writeBytes(contentDisposition + lineEnd);
		String contentType = getContentTypeString(type);
		if (contentType != null) {
			dos.writeBytes("Content-Type: " + contentType + lineEnd);
		}
		dos.writeBytes(lineEnd);

		// create a buffer of maximum size
		bytesAvailable = fileInputStream.available();

		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		buffer = new byte[bufferSize];

		// read file and write it into form...
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		while (bytesRead > 0) {

			dos.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		}

		// send multipart form data necesssary after file data...
		dos.writeBytes(lineEnd);
		dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	}

	private static String getContentTypeString(ContentType type) {
		String contentType = null;
		switch (type) {
		case JPEG:
			contentType = "image/jpeg";
			break;
		case MP4:
			contentType = "video/mp4";
			break;
		}
		return contentType;
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static List<Media> getMediaForTown(int townId, String authToken) {
		String routePath = String.format(MediaByTownRoute, townId, authToken);

		String result = ApiHelper.get(routePath);
		return parseMedia(result);
	}

	public static Bitmap getPhoto(int mediaId, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
		try {
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(String
					.format(GetMediaRoute, mediaId)));

			// receive response as inputStream
			if (httpResponse.getEntity() != null) {
				InputStream inputStream = null;
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
						httpResponse.getEntity());

				int buffersize = 16 * 1024;
				inputStream = new BufferedInputStream(
						bufHttpEntity.getContent(), buffersize);

				if (inputStream != null
						&& httpResponse.getStatusLine().getStatusCode() == 200) {

					// First decode with inJustDecodeBounds=true to check
					// dimensions
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(inputStream, null, options);

					// Calculate inSampleSize
					options.inSampleSize = calculateInSampleSize(options,
							reqWidth, reqHeight);

					inputStream.reset();

					// Decode bitmap with inSampleSize set
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(inputStream, null,
							options);
					inputStream.close();
					// bitmap = BitmapFactory.decodeStream(inputStream);
				}
			}
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return bitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	private static List<Media> parseMedia(String json) {
		ArrayList<Media> towns = new ArrayList<Media>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject entry = (JSONObject) jsonArray.get(i);

				String stringDate = entry.getString("UploadedOn");
				SimpleDateFormat format = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm:ss");
				Date date = null;
				try {
					date = format.parse(stringDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Media media = new Media(entry.getInt("MediaId"),
						entry.getString("Description"), date,
						entry.getString("UploadedBy"));
				towns.add(media);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return towns;
	}
}
