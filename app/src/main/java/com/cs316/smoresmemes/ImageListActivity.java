package com.cs316.smoresmemes;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ImageListActivity extends AppCompatActivity {

    private final StrongReference<String> FetchMethod = new StrongReference<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String[] fetchData = intent.getStringExtra(CODES.FETCH_METHOD).split(" ");

        final String method = fetchData[0];
        FetchMethod.set(method);

        final String calling;
        switch (method) {
            case "base": {
                calling = "getBaseMemeIDs";
                break;
            }
            case "full": {
                calling = "getFullMemeIDs";
                break;
            }

            default: {
                throw new IllegalStateException("Invalid parameter " + CODES.FETCH_METHOD);
            }
        }
        System.out.println("ABOUT TO CALL " + calling);
        final Map<String, String> Data = new HashMap<>();
        Data.put("amount", fetchData[1]);

        final StrongReference<String[]> Ref = new StrongReference<>();

        Runnable d = new Runnable() {
            @Override
            public void run() {
                String[] idArr = MyHTTP.POST(calling, Data).split("\n");
                Ref.set(idArr);
            }
        };
        Thread t = new Thread(d);
        t.start();
        //
        try {
            t.join();
        } catch (InterruptedException e) {
        }
        final LinearLayout OuterLayout = (LinearLayout) findViewById(R.id.outerLayout);
        for (final String IDStr : Ref.get()) {
            final LinearLayout InLayout = new LinearLayout(this);
            final LinearLayout.LayoutParams InLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 216);
            InLayout.setLayoutParams(InLayoutParams);
            ConstraintSet set = new ConstraintSet();
            int InLayoutID = View.generateViewId();
            InLayout.setId(InLayoutID);
            InLayout.setGravity(Gravity.CENTER_VERTICAL);

            final ImageView InImage = new ImageView(this);
            final LinearLayout.LayoutParams InImageParams = new LinearLayout.LayoutParams(300, 200);
            InImage.setLayoutParams(InImageParams);
            int InImageId = View.generateViewId();
            InImage.setId(InImageId);
            InImage.setImageResource(R.drawable.loading);
            InLayout.addView(InImage);

            final TextView InText = new TextView(this);
            InText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            final LinearLayout.LayoutParams InTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            InText.setPadding(16, 0, 16, 0);

            int InTextId = View.generateViewId();

            InText.setId(InTextId);
            InText.setText("Fetching image...");
            InLayout.addView(InText);

            OuterLayout.addView(InLayout);


            final Map<String, String> InData = new HashMap<>();
            InData.put("id", IDStr);

            final StrongReference<byte[]> InDataBytes = new StrongReference<>();

            Runnable InR = new Runnable() {
                @Override
                public void run() {
                    String X = null;
                    if (method.equals("base")) {
                        X = MyHTTP.POST("getBaseMemeImage", InData);
                    } else if (method.equals("full")) {
                        X = MyHTTP.POST("getFullMemeImage", InData);
                    }
                    byte[] DataBytes = Base64.decode(X.trim(), Base64.DEFAULT);
                    final Bitmap FromData = BitmapFactory.decodeByteArray(DataBytes, 0, DataBytes.length);

                    final String Y;
                    switch (method) {
                        case "base": {
                            Y = MyHTTP.POST("getBaseMemeDescription", InData);
                            break;
                        }
                        case "full": {
                            Y = "Uploaded by: " + MyHTTP.POST("getFullMemeUploadedBy", InData);
                            break;
                        }
                        default: {
                            Y = "ERROR!";
                        }
                    }
                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run() {
                            InImage.setImageBitmap(FromData);
                            InText.setText(Y);
                        }
                    });
                    InDataBytes.set(DataBytes);
                }
            };

            Thread InThread = new Thread(InR);
            InThread.start();


            if (method.equals("base")) {
                InLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(CODES.FETCH_RESULT, InDataBytes.get());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }

                });
            } else if (method.equals("full")) {
                final ImageListActivity thisActivity = this;
                final Toast IfSuccessful = Toast.makeText(this, "Meme Saved!", Toast.LENGTH_LONG);
                InLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setTitle("Save Meme?");

                        final TextView input = new TextView(thisActivity);
                        builder.setView(input);


                        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                Bitmap B = BitmapFactory.decodeByteArray(InDataBytes.get(), 0, InDataBytes.get().length);
                                String ImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), B, "generatedMeme", "meme");
                                System.out.println(ImagePath);
                                IfSuccessful.show();
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

                });
            }
        }
    }
}
