package com.cs316.smoresmemes;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by sebmansigh on 2/16/18.
 */

final class ImageMod {
    private ImageMod() {
    }

    public static String getBitmapByteString(Bitmap B) {
        Bitmap JPG = B.copy(B.getConfig(), true);
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        B.compress(Bitmap.CompressFormat.JPEG, 0, o);
        return new String(o.toByteArray());
    }
}
