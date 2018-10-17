package org.artoolkit.ar6.artracking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.artoolkit.ar6.artracking.Constants;
import org.artoolkit.ar6.artracking.R;
import org.artoolkit.ar6.artracking.helpers.Convert;
import org.artoolkit.ar6.artracking.helpers.MsgHelper;
import org.artoolkit.ar6.artracking.models.Id;
import org.artoolkit.ar6.artracking.models.Marker;
import org.artoolkit.ar6.artracking.models.MarkerP;
import org.artoolkit.ar6.artracking.models.SimilarMarkers;
import org.artoolkit.ar6.artracking.models.Wifi;
import org.artoolkit.ar6.artracking.requests.MarkerPostRequest;
import org.artoolkit.ar6.artracking.requests.Queue;
import org.artoolkit.ar6.artracking.requests.SimilarMarkersGetRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MarkerActivity extends AppCompatActivity {
    private static final int REQUEST_PICK_IMAGE = 1;

    private ArrayList<Wifi> mWifis;

    //form
    private Bitmap mImage;
    private ImageView mThumbIv;
    private TextView mImageErr;
    private TextInputLayout mMarkerNameLayout;
    private EditText mMarkerName;
    private TextInputLayout mMarkerTextLayout;
    private EditText mMarkerText;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        Intent intent = getIntent();
        mWifis = intent.getParcelableArrayListExtra(Constants.WIFIS_KEY);

        mImageErr = (TextView) findViewById(R.id.tv_image_error);
        mMarkerNameLayout = (TextInputLayout) findViewById(R.id.marker_name);
        mMarkerName = (EditText) findViewById(R.id.et_marker_name);
        mMarkerTextLayout = (TextInputLayout) findViewById(R.id.marker_text);
        mMarkerText = (EditText) findViewById(R.id.et_marker_text);
        mThumbIv = (ImageView) findViewById(R.id.iv_thumb);

        mThumbIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            }
        });

        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nameValid = validateName();
                boolean imageValid = validateImage();
                boolean textValid = validateText();

                if(nameValid && imageValid && textValid) {
                    addMarker();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadingStart() {
        mSubmitBtn.setEnabled(false);
    }

    private void LoadingEnd() {
        mSubmitBtn.setEnabled(true);
    }

    private void addMarker() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int quality = Integer.parseInt(sharedPref.getString(SettingsActivity.PREF_QUALITY, "100"));
        int size = Integer.parseInt(sharedPref.getString(SettingsActivity.PREF_SIZE, "320"));

        Marker marker = new MarkerP();
        marker.setWifiList(mWifis);
        marker.setName(mMarkerName.getText().toString());
        marker.setImage(mImage, quality, size);
        marker.setText(mMarkerText.getText().toString());

        MarkerPostRequest request = new MarkerPostRequest(marker,
            new Response.Listener<Id>() {
                @Override
                public void onResponse(Id response) {
                    checkSimilarMarkers(response.getId());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MsgHelper.show(MarkerActivity.this, R.string.msg_error);
                    LoadingEnd();
                }
            });
        LoadingStart();
        Queue.getInstance(MarkerActivity.this).add(request);
    }

    private void checkSimilarMarkers(String id) {
        SimilarMarkersGetRequest request = new SimilarMarkersGetRequest(id,
            new Response.Listener<SimilarMarkers>() {
                @Override
                public void onResponse(SimilarMarkers response) {
                    MsgHelper.show(MarkerActivity.this,
                            String.format(getResources().getString(R.string.msg_simimar_markers), response.getCount()));
                    LoadingEnd();
                    finish();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MsgHelper.show(MarkerActivity.this, R.string.msg_error);
                    LoadingEnd();
                }
            });
        Queue.getInstance(MarkerActivity.this).add(request);
    }

    private boolean validateName() {
        if(mMarkerName.getText().toString().trim().isEmpty()) {
            mMarkerNameLayout.setError(getString(R.string.err_marker_name));
            mMarkerNameLayout.setErrorEnabled(true);
            return false;
        }
        else {
            mMarkerNameLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateText() {
        if(mMarkerText.getText().toString().trim().isEmpty()) {
            mMarkerTextLayout.setError(getString(R.string.err_marker_text));
            mMarkerTextLayout.setErrorEnabled(true);
            return false;
        }
        else {
            mMarkerTextLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateImage() {
        if(mImage==null) {
            mImageErr.setVisibility(View.VISIBLE);
            return false;
        }
        else {
            mImageErr.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_PICK_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    InputStream input = getContentResolver().openInputStream(data.getData());
                    mImage = BitmapFactory.decodeStream(input);
                    mThumbIv.setImageBitmap(Convert.getThumb(mImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
