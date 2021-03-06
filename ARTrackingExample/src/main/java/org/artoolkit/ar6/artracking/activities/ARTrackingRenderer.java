/*
 *  SimpleRenderer.java
 *  ARToolKit6
 *
 *  Copyright 2015-2016 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Thorsten Bux, Julian Looser, Philip Lamb
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

import android.opengl.GLES20;

import org.artoolkit.ar6.base.ARToolKit;
import org.artoolkit.ar6.base.NativeInterface;
import org.artoolkit.ar6.base.rendering.ARRenderer;
import org.artoolkit.ar6.base.rendering.shader_impl.Cube;
import org.artoolkit.ar6.base.rendering.shader_impl.SimpleFragmentShader;
import org.artoolkit.ar6.base.rendering.shader_impl.SimpleShaderProgram;
import org.artoolkit.ar6.base.rendering.shader_impl.SimpleVertexShader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A very simple Renderer that adds a marker and draws a cube on it.
 */
class ARTrackingRenderer extends ARRenderer {

    private static final class Trackable {
        String path;
        float height;
        Trackable(String path, float height)
        {
            this.path = path;
            this.height = height;
        }
    }

    private final Trackable trackable;
    private int trackableUID;

    private Cube cube;

    public ARTrackingRenderer(String markerPath) {
        trackable = new Trackable(markerPath, 95.3f);
    }

    /**
     * Markers can be configured here.
     */
    @Override
    public boolean configureARScene() {
        trackableUID = ARToolKit.getInstance().addMarker("2d;" + trackable.path + ";" + trackable.height);
        if (trackableUID < 0) return false;
        NativeInterface.arwSetTrackerOptionInt(NativeInterface.ARW_TRACKER_OPTION_2D_MAX_IMAGES, 1);
        return true;
    }

    //Shader calls should be within a GL thread. GL threads are onSurfaceChanged(), onSurfaceCreated() or onDrawFrame()
    //As the cube instantiates the shader during setShaderProgram call we need to create the cube here.
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        this.shaderProgram = new SimpleShaderProgram(new SimpleVertexShader(), new SimpleFragmentShader());
        cube = new Cube(40.0f, 0.0f, 0.0f, 0.0f);
        cube.setShaderProgram(shaderProgram);
        super.onSurfaceCreated(unused, config);
    }

    /**
     * Override the draw function from ARRenderer.
     */
    @Override
    public void draw() {
        super.draw();

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glFrontFace(GLES20.GL_CCW);

        // If the marker is visible, apply its transformation, and render a cube
        if (ARToolKit.getInstance().queryMarkerVisible(trackableUID)) {
            float[] projectionMatrix = ARToolKit.getInstance().getProjectionMatrix();
            float[] modelViewMatrix = ARToolKit.getInstance().queryMarkerTransformation(trackableUID);
            cube.draw(projectionMatrix, modelViewMatrix);
        }
    }
}