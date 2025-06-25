package com.example.tk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterView = findViewById(R.id.enter);
        enterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
                startActivity(intent);
            }
        });
        videoView = findViewById(R.id.videoView);
        playVideo();
        // 设置视频循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start(); // 视频播放完成后重新开始
            }
        });

    }


    private void playVideo() {
        try {
            // 设置视频路径（示例为res/raw目录下的视频）
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/video");
            videoView.setVideoURI(videoUri);

            // 开始播放视频
            videoView.start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "视频播放失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}