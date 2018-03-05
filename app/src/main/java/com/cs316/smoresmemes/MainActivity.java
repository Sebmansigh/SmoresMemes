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

public class MainActivity extends AppCompatActivity {

    public static final String FETCH_TYPE = "com.cs316.smoresmemes.FETCHMETHOD";

    final BitmapRef BaseImage = new BitmapRef();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ET1 = (EditText) findViewById(R.id.topText);
        final EditText ET2 = (EditText) findViewById(R.id.bottomText);

        ImageButton btnCamera = (ImageButton) findViewById(R.id.takePhotoButton);
        ImageButton btnDownload = (ImageButton) findViewById(R.id.downloadBaseButton);
        ImageButton btnGallery = (ImageButton) findViewById(R.id.galleryButton);
        ImageButton btnSave = (ImageButton) findViewById(R.id.saveButton);
        ImageButton btnShare = (ImageButton) findViewById(R.id.shareButton);
        final ImageView photoView = (ImageView) findViewById(R.id.memeView);
        BaseImage.Bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();

        TextWatcher T = new MemeWatcher();

        ET1.addTextChangedListener(T);
        ET2.addTextChangedListener(T);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CODES.GET_IMAGE_FROM_CAMERA);
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoDownloadBaseImage();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMemeShare();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
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
                            CODES.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique

                    return;
                }

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, CODES.GET_LOCAL_IMAGE);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
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
                            CODES.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                    return;
                }
                final EditText ET1 = (EditText) findViewById(R.id.topText);
                final EditText ET2 = (EditText) findViewById(R.id.bottomText);
                Bitmap FinalImage = ImageMod.applyText(BaseImage.Bitmap, ET1.getText().toString(), ET2.getText().toString(), getApplicationContext());
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
        switch (requestCode) {
            case CODES.GET_IMAGE_FROM_CAMERA: {
                if (resultCode == RESULT_OK && data != null && data.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageView photoView = (ImageView) findViewById(R.id.memeView);
                    BaseImage.Bitmap = bitmap;
                    ApplyTextAndDisplay();
                }
                break;
            }
            case CODES.GET_LOCAL_IMAGE: {
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
                    BaseImage.Bitmap = FromString;
                    ApplyTextAndDisplay();
                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case CODES.DB_FETCH_IMAGE: {
                if (resultCode == RESULT_OK && data != null) {
                    byte[] DataBytes = (byte[]) data.getExtras().get(ImageListActivity.FETCH_RESULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(DataBytes, 0, DataBytes.length);
                    ImageView photoView = (ImageView) findViewById(R.id.memeView);
                    BaseImage.Bitmap = bitmap;
                    ApplyTextAndDisplay();
                }
                break;
            }
            default: {
                System.out.println("Unknown request code:" + requestCode);
            }
        }
    }

    public void gotoDownloadBaseImage() {
        final int NUM_IMAGES = 20;
        Intent intent = new Intent(this, ImageListActivity.class);
        intent.putExtra(FETCH_TYPE, "base 20");
        startActivityForResult(intent, CODES.DB_FETCH_IMAGE);
    }

    public void gotoMemeShare() {
        Intent intent = new Intent(this, MemeShareActivity.class);
        startActivity(intent);
    }

    private class MemeWatcher implements TextWatcher {
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
    }
}

class BitmapRef {
    public Bitmap Bitmap;
}

class ObjectHolder<T> {
    private T _me;

    public ObjectHolder() {
        set(null);
    }

    public ObjectHolder(T obj) {
        set(obj);
    }

    public void set(T obj) {
        _me = obj;
    }

    public T get() {
        return _me;
    }
}
