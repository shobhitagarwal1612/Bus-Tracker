package in.silive.bustracker;

/**
 * Created by Shobhit Agarwal on 24-04-2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import in.silive.bustracker.listener.NetworkResponseListener;

public class SendPOSTRequest extends AsyncTask<String, String, String> {
    private static URL httpUrl;
    private static NetworkResponseListener mListener;
    InputStream is;
    static JSONObject jSonObject;
    static NetworkResponseListener nrl;
    private static String src, dest;

    public static void setNetworkResponseListener(NetworkResponseListener newNrl) {
        nrl = newNrl;
    }

    public static void setUrl(URL url, String source, String destination) {
        httpUrl = url;
        src = source;
        dest = destination;
    }

    @Override
    protected void onPreExecute() {
        nrl.beforeRequest();
        Log.d("tag", "pre execute");
    }

    @Override
    protected String doInBackground(String... params) {
        String data = null;
        try {
            data = URLEncoder.encode("source", "UTF-8") + "="
                    + URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            data += "&" + URLEncoder.encode("destination", "UTF-8") + "="
                    + URLEncoder.encode(dest, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String text = "";
        BufferedReader reader = null;

        try {
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (Exception ex) {

        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }
        return text;
    }

    @Override
    protected void onPostExecute(String s) {
        nrl.postRequest(s);
        Log.d("tag", "post execute");
    }

}
