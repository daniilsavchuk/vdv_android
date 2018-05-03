package com.its.vdv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {
    public static byte [] iconToBytes(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    public static Bitmap iconFromBytes(byte [] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
    }

    public static Bitmap rotate(Bitmap src, float angle) {
        Matrix matrix = new Matrix(); {
            matrix.postRotate(angle);
        }

        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap centeredCrop(Bitmap src, int width, int height) {
        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int top = (src.getHeight() - height) / 2;
        int left = (src.getWidth() - width) / 2;
        int bottom = src.getHeight() - top;
        int right = src.getWidth() - left;

        new Canvas(dest).drawBitmap(
                src,
                new Rect(left, top, right, bottom),
                new Rect(0, 0, width, height),
                new Paint()
        );

        return dest;
    }
}