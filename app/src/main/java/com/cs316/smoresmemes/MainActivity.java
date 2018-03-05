package com.cs316.smoresmemes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
<<<<<<< Updated upstream
=======
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
>>>>>>> Stashed changes

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    final BitmapRef BaseImage = new BitmapRef();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ET1 = (EditText) findViewById(R.id.topText);
        final EditText ET2 = (EditText) findViewById(R.id.bottomText);

        ImageButton btnCamera = (ImageButton) findViewById(R.id.takePhotoButton);
        final ImageView photoView = (ImageView) findViewById(R.id.memeView);
        BaseImage.Bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();

        TextWatcher T = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ApplyTextAndDisplay();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        ET1.addTextChangedListener(T);
        ET2.addTextChangedListener(T);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        ImageButton btnGallery = (ImageButton) findViewById(R.id.galleryButton);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //confirm that we have permissions to access photos
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
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

        ImageButton saveImage = (ImageButton) findViewById(R.id.saveButton);

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to read the contacts
                    }

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                    return;
                }
                Bitmap FinalImage = ImageMod.applyText(BaseImage.Bitmap,"TestTop","TestBtm",getApplicationContext());
                String ImagePath = MediaStore.Images.Media.insertImage(getContentResolver(),FinalImage,"generatedMeme","meme");
                System.out.println(ImagePath);
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
        /*
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
                        BaseImage.Bitmap = FromData;
                        ApplyTextAndDisplay();

                    }
                });
            }
        };
        new Thread(d).start();
        */
        //*/
    }

    private void ApplyTextAndDisplay() {
        final ImageView photoView = (ImageView) findViewById(R.id.memeView);
        final EditText ET1 = (EditText) findViewById(R.id.topText);
        final EditText ET2 = (EditText) findViewById(R.id.bottomText);
        Bitmap myBitmap = BaseImage.Bitmap;

        String TopText = ET1.getText().toString();
        String BtmText = ET2.getText().toString();

        myBitmap = ImageMod.applyText(myBitmap, TopText, BtmText, getApplicationContext());

        photoView.setImageBitmap(myBitmap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            ImageView imgView = (ImageView) findViewById(R.id.memeView);

            // Set the Image in ImageView after decoding the String
            Bitmap FromString = BitmapFactory.decodeFile(imgDecodableString);
            BaseImage.Bitmap = FromString;
            ApplyTextAndDisplay();
        } else if (requestCode == 1) {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        if (data != null && data.hasExtra("data")) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView photoView = (ImageView) findViewById(R.id.memeView);
            BaseImage.Bitmap = bitmap;
            ApplyTextAndDisplay();
        }
    }

    private class BitmapRef {
        public Bitmap Bitmap;
    }
}