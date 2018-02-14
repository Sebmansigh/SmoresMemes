package com.cs316.smoresmemes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnCamera = (ImageButton) findViewById(R.id.imageButton2);
        ImageView photoView = (ImageView)findViewById(R.id.imageView);
        System.out.println("PV: "+photoView);

        btnCamera.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        /*
        final Map<String,String> Data = new HashMap<>();
        Data.put("arg1","?%=!");
        Data.put("arg2","Words");
        Data.put("arg3","?%=!");
        Data.put("arg4","WordsAgain!");
        Runnable c = new Runnable() {
            @Override
            public void run() {
                String X = MyHTTP.POST("test",Data);
                System.out.print("RESULTS: ");
                System.out.println(X);
            }
        };
        /*
        final Map<String,String> Data = new HashMap<>();
        Data.put("path", "IcanHAZmemes!");

        Runnable c = new Runnable() {
            @Override
            public void run() {
                String X = MyHTTP.POST("postmeme",Data);
            }
        };
        /*
        final Map<String, String> Data2 = new HashMap<>();
        Data2.put("id", "19");

        Runnable d = new Runnable() {
            @Override

            public void run() {
                String X = MyHTTP.POST("getmeme", Data2);
                System.out.print("Meme obtained:");
                System.out.println(X);
            }
        };
        //*/

        //To test the queries, uncomment one of the above comments and this one.
        /*
        System.out.println("RUNNING!");
        new Thread(c).start();
        new Thread(d).start();
        */
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra("data"))
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView photoView = (ImageView) findViewById(R.id.imageView);

            System.out.println("Width: "+ bitmap.getWidth());
            System.out.println("Height: "+ bitmap.getHeight());

            ImageHandler.WriteImage(bitmap,new File(context.getFilesDir(),"TestImage.jpg").getAbsolutePath());

            final int TARGET_WIDTH = 300, TARGET_HEIGHT = 240;

            int targetX = Math.max(0,bitmap.getWidth()/2-TARGET_WIDTH/2);
            int targetY = Math.max(0,bitmap.getHeight()/2-TARGET_HEIGHT/2);
            int targetW = Math.min(bitmap.getWidth(),TARGET_WIDTH);
            int targetH = Math.min(bitmap.getHeight(),TARGET_HEIGHT);

            bitmap = ImageHandler.cropImage(bitmap, targetX,targetY,targetW,targetH);
            photoView.setImageBitmap(bitmap);
            System.out.println("Width: "+ bitmap.getWidth());
            System.out.println("Height: "+ bitmap.getHeight());
        }
    }

}
