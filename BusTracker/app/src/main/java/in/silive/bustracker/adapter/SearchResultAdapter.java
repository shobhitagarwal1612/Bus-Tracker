package in.silive.bustracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.LayoutRipple;

import java.util.ArrayList;

import in.silive.bustracker.Map;
import in.silive.bustracker.R;
import in.silive.bustracker.model.BusItem;
import in.silive.bustracker.network.CheckNetwork;

/**
 * Created by Shobhit Agarwal on 30-03-2015.
 */
public class SearchResultAdapter extends BaseAdapter {

    private Context mContext;
    private String[] busNames;
    private String[] busNo;
    private ArrayList<BusItem> mItems;

    private LayoutRipple lp;

    public SearchResultAdapter(Context mContext, ArrayList<BusItem> items) {
        this.mContext = mContext;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        BusItem currentItem = (BusItem) getItem(position);
        TextView busNameTxtView = (TextView) convertView.findViewById(R.id.item_bus_name);
        TextView busNoTxtView = (TextView) convertView.findViewById(R.id.item_bus_no);
        TextView srcTxtView = (TextView) convertView.findViewById(R.id.src);
        TextView destTxtView = (TextView) convertView.findViewById(R.id.dest);
        busNameTxtView.setText(currentItem.getBus_name());
        busNoTxtView.setText(currentItem.getBus_no());
        destTxtView.setText(currentItem.getDestination());
        srcTxtView.setText(currentItem.getSource());

        lp = (LayoutRipple) convertView.findViewById(R.id.lp);
        if (lp != null) {
            lp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckNetwork checkNetwork = new CheckNetwork(mContext);
                    if (checkNetwork.isGPSEnabled() && checkNetwork.isNetworkConnected()) {
                        BusItem currentItem = (BusItem) getItem(position);
                        Intent intent = new Intent("in.silive.bustracker.MAP");
                        Bundle bundle = new Bundle();
                        Map.SetPath(currentItem.getBus_routes());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
        }


        return convertView;

    }
}
