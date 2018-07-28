package com.jayboat.music.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayboat.music.App;
import com.jayboat.music.R;
import com.jayboat.music.bean.SongList;
import com.jayboat.music.ui.activity.PlayingMusicActivity;

public class SongListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView orderNum;
    private TextView songName;
    private TextView songInfo;
    private String musicId;

    public SongListItemViewHolder(View itemView) {
        super(itemView);

        orderNum = itemView.findViewById(R.id.tv_song_list_num);
        songName = itemView.findViewById(R.id.tv_song_list_song_name);
        songInfo = itemView.findViewById(R.id.tv_song_list_singer);
        ImageView more = itemView.findViewById(R.id.iv_song_list_more);

        itemView.setOnClickListener(this);
        more.setOnClickListener(this);
    }


    public void initData(SongList.ResultBean.TracksBean mSong, int position) {
        orderNum.setText(position + "");
        songName.setText(mSong.getName());
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < mSong.getArtists().size(); i++) {
            info.append(mSong.getArtists().get(i).getName());
            info.append("/");
        }
        info = new StringBuilder(info.substring(0, info.length() - 1));
        info.append(" - ").append(mSong.getAlbum().getName());
        songInfo.setText(info.toString());

        musicId = String.valueOf(mSong.getId());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_song_list_more) {
            // todo : show a window to show the setting (ui
        } else {
            // todo : playing music
            Intent intent = new Intent(App.getAppContext(), PlayingMusicActivity.class);
            intent.putExtra("musicId",musicId);
            App.getAppContext().startActivity(intent);
        }
    }
}
