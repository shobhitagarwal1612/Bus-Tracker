package in.silive.bustrackerforconductor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonIcon;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.json.JSONArray;
import org.json.JSONException;

import listener.NetworkResponseListener;

/**
 * Created by Shobhit Agarwal on 04-04-2015.
 */

public class ConductorActivity extends ActionBarActivity implements NetworkResponseListener {
    @Nullable
    private final String USER_AGENT = "Mozilla/5.0";
    ButtonFlat bLogout, bSubmit;
    ButtonFloat bAdd, bRemove;
    ButtonIcon bRefresh, bMap;
    public TextView curLoc, schTime, actTime, status, totPassenger;
    Intent intent;
    int i = 0;
    private Toolbar toolbar;
    ProgressBarCircularIndeterminate progressBarCircularIndeterminate;
    public static String busId;

    public static void setId(String id) {
        busId = id;
    }

    public static String getId() {
        return busId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conductor_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        bRefresh = (ButtonIcon) findViewById(R.id.refresh);
        bLogout = (ButtonFlat) findViewById(R.id.logout);
        bAdd = (ButtonFloat) findViewById(R.id.bAdd);
        bRemove = (ButtonFloat) findViewById(R.id.bRemove);
        bSubmit = (ButtonFlat) findViewById(R.id.submit);
        curLoc = (TextView) findViewById(R.id.curLoc);
        schTime = (TextView) findViewById(R.id.schTime);
        actTime = (TextView) findViewById(R.id.actTime);
        status = (TextView) findViewById(R.id.currStatus);
        totPassenger = (TextView) findViewById(R.id.totPassenger);
        intent = new Intent(this, UpdateLocationService.class);
        progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        progressBarCircularIndeterminate.setVisibility(View.INVISIBLE);
        startService(intent);

        bMap = (ButtonIcon) findViewById(R.id.map);

        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork check = new CheckNetwork(ConductorActivity.this);
                bRefresh.setVisibility(View.GONE);
                progressBarCircularIndeterminate.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        bRefresh.setVisibility(View.VISIBLE);
                        progressBarCircularIndeterminate.setVisibility(View.GONE);
                    }
                }, 1000);
                curLoc.setText(check.currentLocation());


            }
        });
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totPassenger.setText((++i) + "/60");
            }
        });
        bRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totPassenger.setText((--i) + "/60");
            }
        });
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to logout?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService(intent);
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setTitle("Warning");
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void postRequest(String result) {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        JSONArray arr = null;
        if (checkNetwork.isGPSEnabled() && checkNetwork.isNetworkConnected()) {

            try {
                arr = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Map.SetPath(arr);
            this.startActivity(new Intent("in.silive.bustracker.MAP"));
        }

    }

}
