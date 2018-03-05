package com.cs316.smoresmemes;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MemeShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_share);

        Button btnUploadBase = (Button) findViewById(R.id.uploadBaseButton);
        Button btnUploadMeme = (Button) findViewById(R.id.uploadMemeButton);
        Button btnDownloadFull = (Button) findViewById(R.id.browseButton);

        btnUploadBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODES.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    return;
                }

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, CODES.GET_LOCAL_BASE_IMAGE);
            }
        });

        btnUploadMeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODES.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    return;
                }

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, CODES.GET_LOCAL_FULL_MEME);
            }
        });

        final Intent intent = new Intent(this, ImageListActivity.class);
        btnDownloadFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int NUM_IMAGES = 20;
                intent.putExtra(CODES.FETCH_METHOD, "full " + NUM_IMAGES);
                startActivityForResult(intent, CODES.GET_DATABASE_BASE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODES.GET_LOCAL_BASE_IMAGE: {
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
                    ReadyBaseBitmap(FromString);

                } else {
                    //Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case CODES.GET_LOCAL_FULL_MEME: {
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
                    ReadyFullBitmap(FromString);

                } else {
                    //Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    private void ReadyBaseBitmap(final Bitmap B) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Description For Image:");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UploadBaseImage(B, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void UploadBaseImage(Bitmap B, String Description) {
        final Map<String, String> Data = new HashMap<>();
        byte[] DataBytes = ImageMod.getBitmapByteArray(B);
        Data.put("imageData", Base64.encodeToString(DataBytes, Base64.DEFAULT));
        Data.put("imageFormat", "JPG");
        Data.put("imageWidth", Integer.toString(B.getWidth()));
        Data.put("imageHeight", Integer.toString(B.getHeight()));
        Data.put("description", Description);

        final Toast IfSuccessful = Toast.makeText(this, "Image Uploaded!", Toast.LENGTH_LONG);
        Runnable c = new Runnable() {
            @Override
            public void run() {
                String X = MyHTTP.POST("postbasememe", Data);
                IfSuccessful.show();
            }
        };

        Thread t = new Thread(c);
        t.start();
    }

    private void ReadyFullBitmap(final Bitmap B) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter your name:");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UploadFullImage(B, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void UploadFullImage(Bitmap B, String Name) {
        final Map<String, String> Data = new HashMap<>();
        byte[] DataBytes = ImageMod.getBitmapByteArray(B);
        Data.put("imageData", Base64.encodeToString(DataBytes, Base64.DEFAULT));
        Data.put("imageFormat", "JPG");
        Data.put("imageWidth", Integer.toString(B.getWidth()));
        Data.put("imageHeight", Integer.toString(B.getHeight()));
        Data.put("name", Name);

        final Toast IfSuccessful = Toast.makeText(this, "Meme Uploaded!", Toast.LENGTH_LONG);

        Runnable c = new Runnable() {
            @Override
            public void run() {
                String X = MyHTTP.POST("postfullmeme", Data);
                IfSuccessful.show();
            }
        };

        Thread t = new Thread(c);
        t.start();
    }
}
