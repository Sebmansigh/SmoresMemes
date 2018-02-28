package com.cs316.smoresmemes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText ET1;
    EditText ET2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ET1 = (EditText) findViewById(R.id.editText);
        final EditText ET2 = (EditText) findViewById(R.id.editText2);
        ET1.setDrawingCacheEnabled(true);
        ET2.setDrawingCacheEnabled(true);

        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
        ImageButton btnCamera = (ImageButton) findViewById(R.id.imageButton2);
        final ImageView photoView = (ImageView)findViewById(R.id.imageView);
        ImageButton textBtn = (ImageButton) findViewById(R.id.imageButton4);
        System.out.println("PV: "+photoView);
        Button Cnvrt = (Button) findViewById(R.id.button2);

        Cnvrt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                final int FONT_SIZE = 100;

                Bitmap myBitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
                myBitmap = myBitmap.copy(myBitmap.getConfig(),true);
                //Bitmap myBitmap = Bitmap.createBitmap(photoView.getWidth(), photoView.getHeight(), Bitmap.Config.ARGB_8888);
                String TopText = ET1.getText().toString();
                String BtmText = ET2.getText().toString();

                Canvas canvas = new Canvas(myBitmap);
                // new antialised Paint
                Paint paint = new Paint();
                Typeface T = Typeface.create("Impact", Typeface.BOLD);
                paint.setTypeface(T);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(24);
                paint.setColor(Color.BLACK);
                paint.setTextSize(FONT_SIZE);
                paint.setShadowLayer(40f, 0f, 1f, Color.BLACK);

                // draw text to the Canvas center
                Rect bounds = new Rect();
                paint.getTextBounds(TopText, 0, TopText.length(), bounds);
                int x = (myBitmap.getWidth() - bounds.width())/2;
                int y = FONT_SIZE+10;

                canvas.drawText(TopText, x, y, paint);

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawText(TopText, x, y, paint);
                photoView.setImageBitmap(myBitmap);
            }
        });

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
                //System.out.println("Results: " + X.trim().length() + " vs " + BitmapString.trim().length());
                System.out.println("LENGTH: "+X);
            }
        };

        Thread t = new Thread(c);
        t.start();
        //*/
        final Map<String, String> Data2 = new HashMap<>();
        Data2.put("id", "65");
        final ImageView FinalView = photoView;
        Runnable d = new Runnable() {
            @Override
            public void run() {
                final String X = MyHTTP.POST("getmeme", Data2);
                runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run() {
                        byte[] DataBytes = Base64.decode(X.trim(), Base64.DEFAULT);
                        Bitmap FromData = BitmapFactory.decodeByteArray(DataBytes, 0, DataBytes.length);
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
