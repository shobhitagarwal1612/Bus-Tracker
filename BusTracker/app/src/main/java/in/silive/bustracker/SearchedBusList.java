package in.silive.bustracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.silive.bustracker.adapter.SearchResultAdapter;
import in.silive.bustracker.model.BusItem;
import in.silive.bustracker.network.CheckNetwork;

/**
 * Created by Shobhit Agarwal on 30-03-2015.
 */
public class SearchedBusList extends Fragment {

    private ListView mListView;
    private SearchResultAdapter mSearchResultAdapter;
    private String[] busNames;
    private ArrayList<BusItem> mNewsFeedItems;
    private String[] busNo;
    private String data;
    private TextView SourceTV, DestTV;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listview_layout, container, false);
        SourceTV = (TextView) view.findViewById(R.id.sourceTV);
        DestTV = (TextView) view.findViewById(R.id.destinationTV);
        mNewsFeedItems = new ArrayList<>();
        mListView = (ListView) view.findViewById(R.id.bus_name_list);
        mSearchResultAdapter = new SearchResultAdapter(getActivity(), mNewsFeedItems);

        Bundle bundle = getArguments();
        if (bundle != null) {
            SourceTV.setText(bundle.getString("from"));
            DestTV.setText(bundle.getString("to"));
            data = bundle.getString("data");
        } else {
//            Toast.makeText(getActivity(), "Bundle Null", Toast.LENGTH_SHORT).show();
        }
        try {
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i = i + 2) {
                JSONArray array = arr.getJSONArray(i);
                JSONObject obj = null;
                if (array != null) {
                    obj = array.getJSONObject(0);
                }
                Log.d("NRL", array.toString());

                JSONArray array2 = arr.getJSONArray(i + 1);
                Log.d("NRL2", array2.toString());
                BusItem item = new BusItem();
                item.setBus_routes(array2);
                item.setBus_name("test");
                item.setBus_no("123");
                item.setSource(obj.getString("source"));
                item.setDestination(obj.getString("destination"));
                mNewsFeedItems.add(item);
            }
            mSearchResultAdapter.notifyDataSetChanged();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        mListView.setAdapter(mSearchResultAdapter);
        return view;
    }
}
