package com.cs316.smoresmemes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView photoView;
    private static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
        ImageButton btnCamera = (ImageButton) findViewById(R.id.imageButton2);
        ImageView photoView = (ImageView)findViewById(R.id.imageView);
        System.out.println("PV: "+photoView);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        ImageButton btnGallery = (ImageButton) findViewById(R.id.button_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //confirm that we have permissions to access photos
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    // Should we show an explanation for why we need these permissions?
                    //if (shouldShowRequestPermissionRationale(
                    //        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //    // Explain to the user why we need to read the contacts
                    //}
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique

                    return;
                }

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, 1);
            }
        });
        /*
        final Map<String,String> Data = new HashMap<>();
        Data.put("arg1","?%=!");
        Data.put("arg2","Words");
        Data.put("arg3","?%=!");
        Data.put("arg4","WordsAgain!");
        Runnable c = new Runnable()
        {
            @Override
            public void run()
            {
                String X = MyHTTP.POST("test",Data);
                System.out.print("RESULTS: ");
                System.out.println(X);
            }
        };
        //*/
        /*
        final Map<String,String> Data = new HashMap<>();
        Bitmap ImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smoredefault);
        final byte[] compare = ImageMod.getBitmapByteArray(ImageBitmap);
        final String BitmapString = ImageMod.getBitmapByteString(ImageBitmap);
        Data.put("imageData", BitmapString);
        Data.put("imageFormat", "JPG");
        Data.put("imageWidth", Integer.toString(ImageBitmap.getWidth()));
        Data.put("imageHeight", Integer.toString(ImageBitmap.getHeight()));

        Runnable c = new Runnable()
        {
            @Override
            public void run()
            {
                String X = MyHTTP.POST("postmeme",Data);
                System.out.println("Results: " + X.trim().length() + " vs " + BitmapString.trim().length());
            }
        };

        Thread t = new Thread(c);
        t.start();
        //*/
        ///*
        Bitmap ImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smoredefault);
        final byte[] compare = ImageMod.getBitmapByteArray(ImageBitmap);
        final String BitmapString = ImageMod.getBitmapByteString(ImageBitmap);
        //*/

        final Map<String, String> Data2 = new HashMap<>();
        Data2.put("id", "54");
        final ImageView FinalView = photoView;
        Runnable d = new Runnable() {
            @Override
            public void run() {
                final String X = MyHTTP.POST("getmeme", Data2);
                System.out.println("Meme obtained:");
                System.out.println(X);
                System.out.println(" --VS--");
                System.out.println(BitmapString);
                runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run() {
                        byte[] DataBytes = X.trim().getBytes(StandardCharsets.UTF_8);
                        System.out.println(DataBytes.length + " vs " + compare.length);
                        byte[] ToConvert = DataBytes;
                        Bitmap FromData = BitmapFactory.decodeByteArray(ToConvert, 0, ToConvert.length);
                        FinalView.setImageBitmap(FromData);
                        System.out.println("I DID IT!");

                    }
                });
            }
        };
        new Thread(d).start();
        //*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            ImageView imgView = (ImageView) findViewById(R.id.imageView);

            // Set the Image in ImageView after decoding the String
            imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
        }
        else if (requestCode == 1)
        {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        if (data != null && data.hasExtra("data")) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView photoView = (ImageView) findViewById(R.id.imageView);
            System.out.println("PV: " + photoView);
            photoView.setImageBitmap(bitmap);
        }
    }
}
