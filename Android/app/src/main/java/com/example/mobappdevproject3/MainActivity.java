package com.example.mobappdevproject3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        Timer t  = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                // counter++;
                counter = counter + 5;
                progressBar.setProgress(counter);

                if(counter == 100) {
                    t.cancel();
                    Intent i = new Intent(MainActivity.this,Screen2.class);
                    startActivity(i);
                }
            }
        };
        t.schedule(tt,0,100);
    }
}