package com.jayboat.music.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.ui.activity.PlayingMusicActivity;

/**
 * Created by Hosigus on 2018/7/23.
 */

public class BottomMusicBar extends LinearLayout {

    private ViewPager vp;

    private PagerAdapter adapter;

    private PlayControlCallback playControlCallback;

    private PlayProgressControlButton playControlButton;

    public BottomMusicBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.view_bottom_music_bar, this, true);
        playControlButton = findViewById(R.id.btn_bottom_play_control);
        playControlButton.setOnClickListener(v -> {
            playControlButton.switchPlayStatus();
            if (playControlCallback == null) {
                return;
            }
            if (playControlButton.isPlaying()) {
                playControlCallback.play();
            } else {
                playControlCallback.pause();
            }
        });

        findViewById(R.id.btn_bottom_show_list).setOnClickListener(v -> {
            // TODO 弹出底部播放列表
        });
    }

    public void setAdapter(PagerAdapter adapter) {
        vp = findViewById(R.id.vp_bottom_music);
        this.adapter = adapter;
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                setProgress(0);
                if (playControlCallback != null) {
                    playControlCallback.changeMusic(position);
                }
            }
        });
    }

    public void setProgress(float progress) {
        this.playControlButton.setProgress(progress);
    }

    public void setOnPlayControlCallback(PlayControlCallback playControlCallback) {
        this.playControlCallback = playControlCallback;
    }

    public void chooseMusic(int pos) {
        vp.setCurrentItem(pos);
    }

    public void refresh() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setPlayStatus(boolean isPlaying) {
        if (playControlButton.isPlaying() != isPlaying) {
            playControlButton.switchPlayStatus();
        }
    }

    public interface PlayControlCallback {
        void play();

        void pause();

        void changeMusic(int pos);
    }

}
