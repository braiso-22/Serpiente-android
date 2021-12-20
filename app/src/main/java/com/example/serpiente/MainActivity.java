package com.example.serpiente;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    SnakeEngine snakeEngine;
    public static MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        mp = MediaPlayer.create(this, R.raw.musica);
        mp.start();
        mp.setLooping(true);
        Point size = new Point();
        display.getSize(size);
        snakeEngine = new SnakeEngine(this, size);

        setContentView(snakeEngine);
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeEngine.resume();
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.pause();
        mp.pause();
    }
}