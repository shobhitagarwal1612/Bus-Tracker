package in.silive.bustracker;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;

import java.net.MalformedURLException;
import java.net.URL;

import in.silive.bustracker.listener.NetworkResponseListener;

public class Search extends Fragment implements NetworkResponseListener {
    AutoCompleteTextView actv1;
    AutoCompleteTextView actv2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        ButtonFlat search = (ButtonFlat) view.findViewById(R.id.button1);
        actv1 = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);
        actv2 = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView2);

        String[] bus_stops = getResources().
                getStringArray(R.array.bus_stops);
        ArrayAdapter adapter = new ArrayAdapter
                (getActivity(), android.R.layout.simple_list_item_1, bus_stops);
        actv1.setAdapter(adapter);
        actv1.setTextColor(Color.BLACK);
        actv2.setAdapter(adapter);
        actv1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Toast.makeText(getActivity(), actv1.getText().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), actv1.getText().toString() + actv2.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SendPOSTRequest.setNetworkResponseListener(Search.this);
                try {
                    SendPOSTRequest.setUrl(new URL("http://silive.in/bustracker/buses"), actv1.getText().toString(), actv2.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                SendPOSTRequest data = new SendPOSTRequest();
                data.execute();
            }

        });
        return view;
    }

    public ProgressDialog pDialog;

    @Override
    public void beforeRequest() {
        pDialog = ProgressDialog.show(getActivity(), "", "Logging in....");

    }

    @Override
    public void postRequest(String result) {
        Log.d("NFF", "response is " + result);
        pDialog.dismiss();
        Fragment fragment = new SearchedBusList();
        Bundle bundle = new Bundle();
        bundle.putString("from", actv1.getText().toString());
        bundle.putString("to", actv2.getText().toString());
        bundle.putString("data", result);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment).
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack(null).commit();
    }
}
