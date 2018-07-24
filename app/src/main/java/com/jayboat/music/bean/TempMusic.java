package com.jayboat.music.bean;

/**
 * Created by Hosigus on 2018/7/23.
 */

public class TempMusic {
    private String musicName;
    private String musicIconUrl;
    private String singerName;

    public TempMusic() {

    }

    public TempMusic(String musicName, String singerName, String musicIconUrl) {
        this.musicName = musicName;
        this.singerName = singerName;
        this.musicIconUrl = musicIconUrl;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicIconUrl() {
        return musicIconUrl;
    }

    public void setMusicIconUrl(String musicIconUrl) {
        this.musicIconUrl = musicIconUrl;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
}
