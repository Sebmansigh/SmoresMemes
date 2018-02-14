package com.cs316.smoresmemes;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;

public final class ImageHandler {
    private ImageHandler()
    {}
    public static Bitmap cropImage(Bitmap image, int x, int y, int w, int h)
    {
        return Bitmap.createBitmap(image, x,y, w,h);
    }
    //    public static Bitmap combineImages(Bitmap background, Bitmap foreground)
//    {
//        int width = 0, height = 0;
//        Bitmap cs;
//
//        width = background.getWidth();
//        height = background.getHeight();
//
//        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas comboImage = new Canvas(cs);
//        background = Bitmap.createScaledBitmap(background, width, height, true);
//        comboImage.drawBitmap(background, 0, 0, null);
//        comboImage.drawBitmap(foreground, matrix, null);
//
//        return cs;
//    }
    public static void WriteImage(Bitmap image,String Filepath)
    {
        try
        {
            FileOutputStream o = new FileOutputStream(Filepath);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,b);
            o.write(b.toByteArray());
            o.close();
            System.out.println("WROTE IMAGE TO "+Filepath);
        }
        catch(IOException e)
        {
            System.out.println("Exception: "+e.getMessage());
        }
    }
}
