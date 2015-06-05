package in.silive.bustracker;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    public static final String TAG_HOME = "home tag";
    private ActionBar mActionBar;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Resources mResources;
    private String tag;
    private Toolbar toolbar;
    private Fragment frag;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }
        mResources = getResources();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.content_frame, new Search()).setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right)
                .addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 1) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new Builder(this);
            builder.setMessage(
                    "Do you really want to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setTitle("Warning");
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
