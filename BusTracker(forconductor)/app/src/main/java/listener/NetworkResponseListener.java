package listener;

import org.json.JSONException;

public interface NetworkResponseListener {
    public void beforeRequest();

    public void postRequest(String result);
}
