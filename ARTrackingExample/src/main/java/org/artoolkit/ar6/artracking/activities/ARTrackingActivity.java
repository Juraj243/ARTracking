/*
 *  ARSimple.java
 *  ARToolKit6
 *
 *  Copyright 2015-2016 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.artoolkit.ar6.artracking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.artoolkit.ar6.artracking.Constants;
import org.artoolkit.ar6.artracking.R;
import org.artoolkit.ar6.base.ARActivity;
import org.artoolkit.ar6.base.rendering.ARRenderer;

/**
 * A very simple example of extending ARActivity to create a new AR application.
 */
public class ARTrackingActivity extends ARActivity {
    private String mMarkerPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Calls ARActivity's ctor, abstract class of AR6J
        setContentView(R.layout.activity_ar_tracking);

        Intent intent = getIntent();
        mMarkerPath = intent.getStringExtra(Constants.MARKER_PATH_KEY);
    }

    /**
     * Provide our own ARTrackingRenderer.
     */
    @Override
    protected ARRenderer supplyRenderer() {
        return new ARTrackingRenderer(mMarkerPath);
    }

    /**
     * Use the FrameLayout in this Activity's UI.
     */
    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.mainFrameLayout);
    }
}