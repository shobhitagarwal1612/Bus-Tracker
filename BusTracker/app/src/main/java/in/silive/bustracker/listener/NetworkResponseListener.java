package in.silive.bustracker.listener;

public interface NetworkResponseListener {

	public void beforeRequest();
	
	
	public void postRequest(String result);
}
