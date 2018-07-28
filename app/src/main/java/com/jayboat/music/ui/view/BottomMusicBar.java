package com.jayboat.music.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jayboat.music.R;
import com.jayboat.music.bean.TempMusic;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hosigus on 2018/7/23.
 */

public class BottomMusicBar extends LinearLayout {

    private PagerAdapter adapter;

    private PlayControlCallback playControlCallback;

    private PlayProgressControlButton playControlButton;

    private List<TempMusic> musicList = new ArrayList<>();

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

        ViewPager vp = findViewById(R.id.vp_bottom_music);
        adapter = new MusicPageAdapter();
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

    public void setMusicList(List<TempMusic> musicList) {
        this.musicList = musicList;
        adapter.notifyDataSetChanged();
    }

    public interface PlayControlCallback {
        void play();

        void pause();

        void changeMusic(int pos);
    }

    private class MusicPageAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_info_bottom_music_bar, container, false);
            TempMusic music = musicList.get(position);
            Glide.with(getContext())
                    .load(music.getMusicIconUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_default_bottom_music_icon))
                    .into((RoundedImageView) v.findViewById(R.id.iv_music_icon_bottom));
            TextView musicName = v.findViewById(R.id.tv_music_name);
            TextView musicSingerName = v.findViewById(R.id.tv_music_singer_name);
            musicName.setText(music.getMusicName());
            musicSingerName.setText(music.getSingerName());
            container.addView(v);
            return v;
        }
    }
}
