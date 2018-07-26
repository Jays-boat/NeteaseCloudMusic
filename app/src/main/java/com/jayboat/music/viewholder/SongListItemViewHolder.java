package com.jayboat.music.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayboat.music.R;
import com.jayboat.music.bean.Song;

public class SongListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView orderNum;
    private TextView songName;
    private TextView songInfo;
    private ImageView more;
    private Song.ResultBean.TracksBean song;

    public SongListItemViewHolder(View itemView) {
        super(itemView);

        orderNum = itemView.findViewById(R.id.tv_song_list_num);
        songName = itemView.findViewById(R.id.tv_song_list_song_name);
        songInfo = itemView.findViewById(R.id.tv_song_list_singer);
        more = itemView.findViewById(R.id.iv_song_list_more);

        itemView.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    public void initData(Song.ResultBean.TracksBean mSong, int position) {
        this.song = mSong;
        orderNum.setText(position + "");
        songName.setText(song.getName());
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < song.getArtists().size(); i++) {
            info.append(song.getArtists().get(i).getName());
            info.append("/");
        }
        info = new StringBuilder(info.substring(0, info.length() - 1));
        info.append(" - ").append(song.getAlbum().getName());
        songInfo.setText(info.toString());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_song_list_more) {
            // todo : show a window to show the setting (ui
        } else {
            // todo : playing music
        }
    }
}
