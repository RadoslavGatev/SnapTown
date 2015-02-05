package com.example.snaptown.apiclients;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.example.snaptown.helpers.ApiHelper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MediaClient {

	public static enum ContentType {
		JPEG, MP4
	}

	private static final String UPLOAD_SERVER_URI = ApiHelper.ApiUrl
			+ "/Media?authToken=%d&townId=%d";

	private static String lineEnd = "\r\n";
	private static String twoHyphens = "--";
	private static String boundary = "-------------------------acebdf13572468";
	private static int maxBufferSize = 1 * 1024 * 1024;

	public static void uploadFile(final String sourceFileUri,
			final ContentType type) {
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
								1234, 3));
						// Open a HTTP connection to the URL
						conn = initConnection(fileName, boundary, url);

						dos = new DataOutputStream(conn.getOutputStream());

						writeToStream(type, sourceFile.getName(), dos, fileInputStream);

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

	private static HttpURLConnection initConnection(String fileName,
			String boundary, URL url) throws IOException, ProtocolException {
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true); // Allow Inputs
		conn.setDoOutput(true); // Allow Outputs
		conn.setUseCaches(false); // Don't use a Cached Copy
		conn.setRequestMethod("POST");
//		conn.setRequestProperty("Connection", "Keep-Alive");
//		conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ boundary);
//		conn.setRequestProperty("uploaded_file", fileName);
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
}
