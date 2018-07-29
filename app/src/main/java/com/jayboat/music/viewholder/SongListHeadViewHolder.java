package com.jayboat.music.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayboat.music.R;

public class SongListHeadViewHolder extends RecyclerView.ViewHolder {

    public SongListHeadViewHolder(View itemView, int totalNum) {
        super(itemView);

        TextView totalNum1 = itemView.findViewById(R.id.tv_song_list_play_num);
        totalNum1.setText("(共" + totalNum + "首)");
    }

}
