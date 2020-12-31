package com.fang.touhou_danmaku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fang.touhou_danmaku.service.MusicService;
import com.fang.touhou_danmaku.view.GameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setContentView(new GameView(this));
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    @Override
    protected void onStop() {
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
        super.onStop();
    }
}