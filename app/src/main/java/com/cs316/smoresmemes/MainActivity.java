package com.cs316.smoresmemes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
