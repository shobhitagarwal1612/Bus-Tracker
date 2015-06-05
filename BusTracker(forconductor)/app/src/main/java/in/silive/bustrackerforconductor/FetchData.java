package in.silive.bustrackerforconductor;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import listener.NetworkResponseListener;

public class FetchData extends AsyncTask<String, String, String> {

    private static String URL;
    private static NetworkResponseListener mListener;

    public static void setURL(String url) {
        URL = url;
    }

    public static void setNetworkResponseListener(
            NetworkResponseListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.beforeRequest();
    }

    @Override
    protected String doInBackground(String... params) {
        String res = null;
        BufferedReader buffReader = null;
        HttpURLConnection httpUrlConn = null;
        try {
            java.net.URL httpURL = new java.net.URL(URL);
            httpUrlConn = (HttpURLConnection) httpURL.openConnection();
            httpUrlConn.connect();
            httpUrlConn.setConnectTimeout(5000);
            InputStream ipStream = httpUrlConn.getInputStream();
            buffReader = new BufferedReader(new InputStreamReader(ipStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = buffReader.readLine()) != null)
                sb.append(line);
            res = sb.toString();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (buffReader != null) {
                    buffReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            httpUrlConn.disconnect();
        }
        Log.d("log", res.toString());
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.postRequest(result);
    }
}
