package org.artoolkit.ar6.artracking.helpers;

import android.content.Context;

import android.graphics.Bitmap;

import org.artoolkit.ar6.artracking.models.Marker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by duda on 7.11.2017.
 */

public class StorageHelper {
    private static String buildFileName(String id) {
        return String.format("%s.jpg", id);
    }

    public static boolean write(Context context, Marker marker) {
        return write(context, buildFileName(marker.getId()), Convert.toByte(marker.getImage()));
    }

    private static boolean write(Context context, String name, byte[] data) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap read(Context context, String id) {
        File file = new File(context.getFilesDir(), buildFileName(id));

        try {
            FileInputStream stream = new FileInputStream(file);
            return Convert.toBitmap(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean exists(Context context, String id) {
        File file = new File(context.getFilesDir(), buildFileName(id));
        return file.exists();
    }

    public static String buildPath(Context context, String id) {
        return String.format("%s/%s.jpg", context.getFilesDir().getPath(), id);
    }
}
