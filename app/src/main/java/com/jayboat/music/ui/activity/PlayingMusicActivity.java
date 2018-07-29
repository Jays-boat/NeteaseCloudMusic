package com.jayboat.music.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.jayboat.music.R;
import com.jayboat.music.utils.ToastUtils;

public class PlayingMusicActivity extends BaseActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PlayingMusicActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private ConstraintLayout mCtl;

    private String musicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);
        Intent intent = getIntent();
        musicId = intent.getStringExtra("musicId");
        initLocalView();

        if (musicId == null || musicId.equals("")){
            ToastUtils.show("未获取歌曲信息:(");
        } else {
            getData();
        }

    }

    private void getData(){

    }

    private void initLocalView(){
        mToolbar = findViewById(R.id.tb_playing);
        mCtl = findViewById(R.id.ctl_playing);
    }
}
