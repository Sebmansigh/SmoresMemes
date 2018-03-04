package com.cs316.smoresmemes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
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

    public static Bitmap applyText(Bitmap myBitmap, String TopText, String BtmText, Context c) {

        myBitmap = myBitmap.copy(myBitmap.getConfig(), true);
        final int FONT_SIZE = myBitmap.getHeight() / 8;
        Canvas canvas = new Canvas(myBitmap);
        // new antialised Paint
        Paint paint = new Paint();
        Typeface T = ResourcesCompat.getFont(c, R.font.impact);
        paint.setTypeface(T);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(24);
        paint.setColor(Color.BLACK);
        paint.setTextSize(FONT_SIZE);
        paint.setShadowLayer(40f, 0f, 1f, Color.BLACK);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(TopText, 0, TopText.length(), bounds);
        int x = (myBitmap.getWidth() - bounds.width()) / 2;
        int y = FONT_SIZE;

        canvas.drawText(TopText, x, y, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawText(TopText, x, y, paint);

        // Bottom Text
        paint.getTextBounds(BtmText, 0, BtmText.length(), bounds);
        int x2 = (myBitmap.getWidth() - bounds.width()) / 2;
        int y2 = myBitmap.getHeight() - y / 2;

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(24);
        paint.setColor(Color.BLACK);
        canvas.drawText(BtmText, x2, y2, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawText(BtmText, x2, y2, paint);

        return myBitmap;
    }
}
