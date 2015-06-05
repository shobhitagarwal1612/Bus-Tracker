package in.silive.bustracker.network;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParsing {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	static String data = "";
	GetStringFromStream gsfs = new GetStringFromStream();

	public JSONObject makeHttpRequestPOST(String url, JSONObject jsonsend) {
		// Making HTTP request
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");
			post.setEntity(new StringEntity(jsonsend.toString(), "UTF-8"));

			HttpResponse httpResponse = httpClient.execute(post);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			json = gsfs.getString(is);
			is.close();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			Log.d("POST id json ", "POST json" + json);
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		// return JSON String
		return jObj;

	}

	public String makeHttpRequestGET(String url) throws IOException {
		URL httpUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) httpUrl
				.openConnection();
		connection.connect();
		connection.setConnectTimeout(5000);
		InputStream in = connection.getInputStream();
		data = gsfs.getString(in);
		in.close();
		return data;
	}

}