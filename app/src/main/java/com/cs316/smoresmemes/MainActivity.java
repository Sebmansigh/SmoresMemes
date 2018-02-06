package com.cs316.smoresmemes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Runnable c = new Runnable() {
            @Override
            public void run() {
                String X = MyHTTP.POST("HI!");
                System.out.print("RESULTS: ");
                System.out.println(X);
            }
        };
        System.out.println("RUNNING!");
        new Thread(c).start();
    }
}
