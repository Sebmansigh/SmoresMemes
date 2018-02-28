package com.cs316.smoresmemes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
        byte[] Bytes = getBitmapByteArray(B);
        //System.out.println("ByteArraySize:"+Bytes.length);
        String RetStr = "";
        for (byte b : Bytes) {
            RetStr += (char) b;
        }
        //System.out.println("ByteStringSize:"+RetStr.length());
        return RetStr;
    }

    public static byte[] getBitmapByteArray(Bitmap B) {
        Bitmap JPG = B.copy(B.getConfig(), false);
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        B.compress(Bitmap.CompressFormat.JPEG, 0, o);
        return o.toByteArray();
    }

    public static byte[] convertByteStringToArray(String S) {
        byte[] RetArr = new byte[S.length()];
        for (int i = 0; i < RetArr.length; i++) {
            RetArr[i] = (byte) S.charAt(i);
        }
        return RetArr;
    }
}
