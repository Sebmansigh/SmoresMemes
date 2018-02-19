package com.cs316.smoresmemes;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by sebmansigh on 2/16/18.
 */

final class ImageMod {
    private ImageMod() {
    }

    public static String getBitmapByteString(Bitmap B) {
        return new String(getBitmapByteArray(B), StandardCharsets.UTF_8);
    }

    public static byte[] getBitmapByteArray(Bitmap B) {
        Bitmap JPG = B.copy(B.getConfig(), false);
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        B.compress(Bitmap.CompressFormat.JPEG, 0, o);
        return o.toByteArray();
    }
}
