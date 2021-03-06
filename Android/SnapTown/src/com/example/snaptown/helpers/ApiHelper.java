package com.example.snaptown.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class ApiHelper {
	public static final String ApiUrl = "http://10.255.5.7/snaptown/api";

	public static String get(String url) {
		return callService(new HttpGet(ApiUrl + "/" + url));
	}

	public static String put(String url) {
		return callService(new HttpPut(ApiUrl + "/" + url));
	}

	public static String post(String url) {
		return callService(new HttpPost(ApiUrl + "/" + url));
	}

	public static void post(final String url, final String jsonObject) {

		HttpPost post = new HttpPost(ApiUrl + "/" + url);
		try {
			StringEntity params = new StringEntity(jsonObject, HTTP.UTF_8);
			post.setEntity(params);
		} catch (Exception e) {
		}
		callService(post);

	}

	public static String delete(String url) {
		return callService(new HttpDelete(ApiUrl + "/" + url));
	}

	public static String callService(HttpUriRequest request) {
		InputStream inputStream = null;
		String result = "";
		try {
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json; charset=UTF-8");

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(request);

			// receive response as inputStream
			if (httpResponse.getEntity() != null) {
				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null
						&& httpResponse.getStatusLine().getStatusCode() == 200) {
					result = convertInputStreamToString(inputStream);
				}
			}

			// convert inputstream to string

		} catch (Exception e) {
			if (e.getMessage() != null) {
				Log.d("InputStream", e.getMessage());
			}
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

}
