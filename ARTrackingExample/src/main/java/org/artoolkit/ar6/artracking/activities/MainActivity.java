package org.artoolkit.ar6.artracking.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.artoolkit.ar6.artracking.Constants;
import org.artoolkit.ar6.artracking.R;
import org.artoolkit.ar6.artracking.adapters.MarkersAdapter;
import org.artoolkit.ar6.artracking.helpers.MsgHelper;
import org.artoolkit.ar6.artracking.helpers.StorageHelper;
import org.artoolkit.ar6.artracking.models.MarkerG;
import org.artoolkit.ar6.artracking.models.Wifi;
import org.artoolkit.ar6.artracking.requests.MarkerGetRequest;
import org.artoolkit.ar6.artracking.requests.MarkersGetRequest;
import org.artoolkit.ar6.artracking.requests.Queue;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MarkersAdapter.OnItemClickListener {
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;

    private WifiManager mWifiManager;
    private WifiScanReceiver mWifiScanReceiver;
    private ProgressBar mMarkersPb;
    private final ArrayList<Wifi> mWifis = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MarkersAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            for (ScanResult scanResut : mWifiManager.getScanResults()) {
                Wifi wifi = new Wifi(scanResut);
                mWifis.add(wifi);
            }

            if(mWifis.size() > 0) {
                fetch();
            }
        }
    }

    private void fetch() {
        Request request = new MarkersGetRequest(mWifis, new Response.Listener<String[]>() {
            @Override
            public void onResponse(String[] response) {
                mAdapter.setDataset(response);
                LoadingFinish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MsgHelper.show(MainActivity.this, R.string.msg_error);
                LoadingFinish();
            }
        });
        LoadingStart();
        Queue.getInstance(this).add(request);
    }

    private void LoadingStart() {
        mMarkersPb.setVisibility(View.VISIBLE);
    }

    private void LoadingFinish() {
        mMarkersPb.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        mMarkersPb = (ProgressBar) findViewById(R.id.pb_markers);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_markers);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MarkersAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiScanReceiver = new WifiScanReceiver();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
        }

        FloatingActionButton markerFab = (FloatingActionButton) findViewById(R.id.fab_marker);
        markerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWifis.size() > 0) {
                    Intent intent = new Intent(MainActivity.this, MarkerActivity.class);
                    intent.putParcelableArrayListExtra(Constants.WIFIS_KEY, mWifis);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mWifiManager.startScan();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            unregisterReceiver(mWifiScanReceiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    mWifiManager.startScan();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    MsgHelper.show(this, R.string.msg_access_location_not_granted);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void ShowAR(String id) {
        //Bitmap b = StorageHelper.read(this, id);

        Intent intent = new Intent(this, ARTrackingActivity.class);
        intent.putExtra(Constants.MARKER_PATH_KEY, StorageHelper.buildPath(this, id));
        startActivity(intent);
    }

    @Override
    public void onItemClick(final String id) {
        if(StorageHelper.exists(this, id)) {
            ShowAR(id);
        }
        else {
            MarkerGetRequest request = new MarkerGetRequest(id, new Response.Listener<MarkerG>(){
                @Override
                public void onResponse(MarkerG response) {
                    StorageHelper.write(MainActivity.this, response);
                    ShowAR(id);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MsgHelper.show(MainActivity.this, R.string.msg_error);
                }
            });
            Queue.getInstance(this).add(request);
        }
    }
}
