package com.jayboat.music.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayboat.music.R;

public class SongListHeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView playing;
    private TextView totalNum;

    public SongListHeadViewHolder(View itemView, int totalNum) {
        super(itemView);

        playing = itemView.findViewById(R.id.iv_song_list_play);
        this.totalNum = itemView.findViewById(R.id.tv_song_list_play_num);
        this.totalNum.setText("(共" + totalNum + "首)");
        playing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_song_list_play:
                // todo : go to playing
                break;
            default:
                break;
        }
    }
}
