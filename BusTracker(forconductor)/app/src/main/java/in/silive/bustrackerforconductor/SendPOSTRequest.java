package in.silive.bustrackerforconductor;

/**
 * Created by Shobhit Agarwal on 24-04-2015.
 */

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import listener.NetworkResponseListener;

public class SendPOSTRequest extends AsyncTask<String, String, String> {
    static NetworkResponseListener nrl;
    private static String busId;

    public static void setNetworkResponseListener(NetworkResponseListener newNrl) {
        nrl = newNrl;
    }

    public static void setId(String id) {
        busId = id;
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
            data = URLEncoder.encode("bus_id", "UTF-8") + "="
                    + URLEncoder.encode(busId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            data += "&" + URLEncoder.encode("lat", "UTF-8") + "="
                    + URLEncoder.encode("12", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            data += "&" + URLEncoder.encode("long", "UTF-8")
                    + "=" + URLEncoder.encode("111", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            data += "&" + URLEncoder.encode("total_seats", "UTF-8")
                    + "=" + URLEncoder.encode("60", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            data += "&" + URLEncoder.encode("occupied_seats", "UTF-8")
                    + "=" + URLEncoder.encode("50", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String text = "";
        BufferedReader reader = null;

        try {

            URL url = new URL("http://silive.in/bustracker/conductor");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
