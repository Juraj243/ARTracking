package org.artoolkit.ar6.artracking.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * Created by duda on 5.11.2017.
 */

public class Convert {
    private static final int THUMB_SIZE = 160;

    public static String toBase64(Bitmap bitmap, int quality, int size) {
        Bitmap resizedBitmap = resize(bitmap, size);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
    }

    public static byte[] toByte(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }

    public static Bitmap toBitmap(FileInputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }

    public static Bitmap getThumb(Bitmap bitmap) {
        return resize(bitmap, THUMB_SIZE);
    }

    public static Bitmap toBitmap(String base64) {
        byte[] b = toByte(base64);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    private static Bitmap resize(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
