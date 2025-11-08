package org.happysanta.gd.API;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import static org.happysanta.gd.Helpers.getAppVersion;
import static org.happysanta.gd.Helpers.logDebug;

public class Request {

	private List<NameValuePair> params;
	private ResponseHandler handler;
	private AsyncRequestTask task;
	private String apiURL;

	public Request(String method, List<NameValuePair> params, ResponseHandler handler, boolean useDebugURL) {
		construct(method, params, handler, useDebugURL ? API.DEBUG_URL : API.URL);
	}

	public Request(String method, List<NameValuePair> params, ResponseHandler handler) {
		construct(method, params, handler, API.URL);
	}

	private void construct(String method, List<NameValuePair> params, ResponseHandler handler, String apiURL) {
		this.apiURL = apiURL;

		params.add(new BasicNameValuePair("v", String.valueOf(API.VERSION)));
		params.add(new BasicNameValuePair("method", method));
		params.add(new BasicNameValuePair("app_version", getAppVersion()));
		params.add(new BasicNameValuePair("app_lang", Locale.getDefault().getDisplayLanguage()));

		this.params = params;
		this.handler = handler;

		go();
	}

	private void go() {
		task = new AsyncRequestTask();
		task.execute(apiURL);
	}

	public void cancel() {
		if (task != null) {
			task.cancel(true);
			task = null;
		}
	}

	private void onDone(String result) {
		Response response;
		logDebug("API.Request.onDone ()");

		try {
			response = new Response(result);
		} catch (APIException e) {
			handler.onError(e);
			return;
		} catch (Exception e) {
			// e.printStackTrace();
			handler.onError(new APIException(result == null ? "Network error" : "JSON parsing error"));
			return;
			// exception = new Exception();
		}

		// handler.onResponse(response);

		if (response != null)
			handler.onResponse(response);
		else
			handler.onError(new APIException("JSON parsing error"));
	}

	protected class AsyncRequestTask extends AsyncTask<String, Void, String> {

		private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
			StringBuilder result = new StringBuilder();
			boolean first = true;

			for (NameValuePair pair : params) {
				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			}

			return result.toString();
		}

		@Override
		protected String doInBackground(String... objects) {
			String urlString = objects[0];
			HttpURLConnection urlConnection = null;
			String result = null;
			InputStream is = null;

			try {
				URL url = new URL(urlString);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("POST");
				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);

				OutputStream os = urlConnection.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write(getQuery(params));
				writer.flush();
				writer.close();
				os.close();

				urlConnection.connect();

				is = urlConnection.getInputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;
				while ((line = reader.readLine()) != null) {
					if (isCancelled()) break;
					sb.append(line + "\n");
				}
				result = sb.toString();
			} catch (java.lang.Exception e) {
				logDebug("API request failed: " + e.getMessage());
				// e.printStackTrace();
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				try {
					if (is != null) is.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}

			return result;
		}

		@Override
		public void onPostExecute(String result) {
			onDone(result);
		}

	}

}