package com.cs316.smoresmemes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageListActivity extends AppCompatActivity {
    public static final String FETCH_RESULT = "com.cs316.smoresmemes.FETCHRESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String[] fetchData = intent.getStringExtra(MainActivity.FETCH_TYPE).split(" ");

        String method = fetchData[0];
        switch (method) {
            case "base": {
                method = "getBaseMemeIDs";
                break;
            }
            default: {
                throw new IllegalStateException("Invalid fetch type parameter");
            }
        }
        System.out.println("ABOUT TO CALL " + method);
        final String inMethod = method;
        final Map<String, String> Data = new HashMap<>();
        Data.put("amount", fetchData[1]);
        final String[][] ArrayRef = new String[1][]; //Exists because Java closures are dumb and I don't want to write a pointer class.
        Runnable d = new Runnable() {
            @Override
            public void run() {
                String[] idArr = MyHTTP.POST(inMethod, Data).split("\n");
                ArrayRef[0] = idArr;
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
        for (final String IDStr : ArrayRef[0]) {
            final LinearLayout InLayout = new LinearLayout(this);
            final LinearLayout.LayoutParams InLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 216);
            InLayout.setLayoutParams(InLayoutParams);
            ConstraintSet set = new ConstraintSet();
            int InLayoutID = View.generateViewId();
            InLayout.setId(InLayoutID);

            final ImageView InImage = new ImageView(this);
            final LinearLayout.LayoutParams InImageParams = new LinearLayout.LayoutParams(300, 200);
            InImage.setLayoutParams(InImageParams);
            int InImageId = View.generateViewId();
            InImage.setId(InImageId);
            InLayout.addView(InImage);


            final Map<String, String> InData = new HashMap<>();
            InData.put("id", IDStr);
            final byte[][] InDataBytes = new byte[1][];
            Runnable InR = new Runnable() {
                @Override
                public void run() {
                    final String X = MyHTTP.POST("getbasememeimage", InData);
                    byte[] DataBytes = Base64.decode(X.trim(), Base64.DEFAULT);
                    final Bitmap FromData = BitmapFactory.decodeByteArray(DataBytes, 0, DataBytes.length);
                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run() {
                            InImage.setImageBitmap(FromData);
                        }
                    });
                    InData.put("description", MyHTTP.POST("getbasememedescription", InData));
                    InDataBytes[0] = DataBytes;
                }
            };

            Thread InThread = new Thread(InR);
            InThread.start();

            final TextView InText = new TextView(this);
            final LinearLayout.LayoutParams InTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            InText.setGravity(Gravity.CENTER_VERTICAL);
            int InTextId = View.generateViewId();
            InText.setId(InTextId);
            InLayout.addView(InText);

            OuterLayout.addView(InLayout);

            //set.applyTo(InLayout);
            try {
                InThread.join();
            } catch (InterruptedException e) {
            }
            InText.setText(InData.get("description"));

            System.out.println("CLICK SET!");
            InLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("CLICK GET!");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(FETCH_RESULT, InDataBytes[0]);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

            });
        }
    }
}
